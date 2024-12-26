package br.com.firstingressos.dashboard.sdkdemo.activities;

import static android.widget.Toast.makeText;

import static com.google.android.material.internal.ContextUtils.getActivity;
import static br.com.gertec.epwp.Main.context;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import br.com.firstingressos.dashboard.sdkdemo.activities.pagamentoRealizado.PagamentoRealizadoActivity;
import br.com.firstingressos.dashboard.sdkdemo.controller.PrintController;
import br.com.stone.posandroid.providers.PosPrintProvider;
import br.com.stone.posandroid.providers.PosPrintReceiptProvider;
import br.com.stone.posandroid.providers.PosTransactionProvider;
import br.com.firstingressos.dashboard.R;
import stone.application.enums.Action;
import stone.application.enums.ErrorsEnum;
import stone.application.enums.ReceiptType;
import stone.application.enums.TransactionStatusEnum;
import stone.application.interfaces.StoneCallbackInterface;

public class PosTransactionActivity extends BaseTransactionActivity<PosTransactionProvider> {
    private List<String> nameList = new ArrayList<>();
    private List<String> valorList = new ArrayList<>();
    private List<String> taxaList = new ArrayList<>();
    private List<String> capaList = new ArrayList<>();
    private List<String> enderecoList = new ArrayList<>();
    private List<String> descricaoList = new ArrayList<>();
    private List<String> eventoList = new ArrayList<>();
    private List<String> classificacaoList = new ArrayList<>();
    private List<String> tipoList = new ArrayList<>();
    private EditText ip, port;
    QRGEncoder qrgEncoder;
    String email = "oi@gmail.com";
    String paymentMethod = "";

    @Override
    protected PosTransactionProvider buildTransactionProvider() {
        return new PosTransactionProvider(this, transactionObject, getSelectedUserModel());
    }

    protected PosTransactionProvider getTransactionProvider() {
        return (PosTransactionProvider) super.getTransactionProvider();
    }

    @Override
    public void onSuccess() {
        if (transactionObject.getTransactionStatus() == TransactionStatusEnum.APPROVED) {

            final PrintController printMerchant =
                    new PrintController(PosTransactionActivity.this,
                            new PosPrintReceiptProvider(this.getApplicationContext(),
                                    transactionObject, ReceiptType.MERCHANT));

            printMerchant.print();

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Transação aprovada! Deseja imprimir a via do cliente?");

            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final PrintController printClient =
                    new PrintController(PosTransactionActivity.this,
                            new PosPrintReceiptProvider(getApplicationContext(),
                                    transactionObject, ReceiptType.CLIENT));
                    printClient.print();
                    try {
                        checkout();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        checkout();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    builder.show();

                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    makeText(
                            getApplicationContext(),
                            "Erro na transação: \"" + getAuthorizationMessage() + "\"",
                            Toast.LENGTH_LONG
                    ).show();
                    Intent intent = new Intent(PosTransactionActivity.this, BaseTransactionActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    public void checkout() throws IOException {
        try {
            View view = View.inflate(PosTransactionActivity.context, R.layout.activity_transaction, null);
            TextView editText =(TextView) view.findViewById(R.id.amountEditText);
            String value = editText.getText().toString();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                email = user.getEmail();
            }

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                nameList = bundle.getStringArrayList("nameList");
                valorList = bundle.getStringArrayList("valorList");
                taxaList = bundle.getStringArrayList("taxaList");
                capaList = bundle.getStringArrayList("capaList");
                enderecoList = bundle.getStringArrayList("enderecoList");
                eventoList = bundle.getStringArrayList("eventoList");
                descricaoList = bundle.getStringArrayList("descricaoList");
                classificacaoList = bundle.getStringArrayList("classificacaoList");
                tipoList = bundle.getStringArrayList("tipoList");

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                String tipo = "ficha";

                if(!taxaList.get(0).contains("-")) {
                    tipo = "ingresso";
                }

                TextView typePayment = view.findViewById(R.id.typePayment);
                String text = (String) typePayment.getText();
                switch (text) {
                    case "credit":
                        paymentMethod = "Cartão de crédito";
                        break;
                    case "debit":
                        paymentMethod = "Cartão de débito";
                        break;
                    case "voucher":
                        paymentMethod = "Voucher";
                        break;
                    case "pix":
                        paymentMethod = "PIX";
                        break;
                    case "dinheiro":
                        paymentMethod = "Dinheiro";
                        break;
                    case "qrCode":
                        paymentMethod = "QrCode";
                        break;
                    default:
                        paymentMethod = "Cartão de crédito";
                }

                for(int j=0; j<nameList.size(); j++){
                    try {
                        db.collection("promotores/"+email+"/vendas/").add(new Vendas(valorList.get(j), email, new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(Calendar.getInstance().getTime()), tipo, paymentMethod, "0", nameList.get(j)));
                    } catch (Exception e) {
                        db.collection("promotores/"+email+"/vendas/").add(new Vendas(valorList.get(j), email, new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(Calendar.getInstance().getTime()), tipo, paymentMethod, "0", "Item"));
                    }
                }

                try{
                    imprimir2(view);
                } catch (Exception e) {
                    makeText(PosTransactionActivity.this, "Erro ao preparar impressão (Checkout)", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(PosTransactionActivity.this, PagamentoRealizadoActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            makeText(PosTransactionActivity.this, "Erro na impressão", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError() {
        super.onError();
        if (providerHasErrorEnum(ErrorsEnum.DEVICE_NOT_COMPATIBLE)) {
            makeText(
                    this,
                    "Dispositivo não compatível ou dependência relacionada não está presente",
                    Toast.LENGTH_SHORT
            ).show();
            Intent intent = new Intent(PosTransactionActivity.this, BaseTransactionActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onStatusChanged(final Action action) {
        super.onStatusChanged(action);

        runOnUiThread(() -> {

            switch (action) {
                case TRANSACTION_WAITING_PASSWORD:
                    makeText(
                            PosTransactionActivity.this,
                            "Pin tries remaining to block card: ${transactionProvider?.remainingPinTries}",
                            Toast.LENGTH_LONG
                    ).show();
                    break;
                case TRANSACTION_TYPE_SELECTION:
                    List<String> options = getTransactionProvider().getTransactionTypeOptions();
                    showTransactionTypeSelectionDialog(options);
            }

        });
    }


    private void showTransactionTypeSelectionDialog(final List<String> optionsList) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione o tipo de transação");
        String[] options = new String[optionsList.size()];
        optionsList.toArray(options);
        builder.setItems(
                options,
                (dialog, which) -> getTransactionProvider().setTransactionTypeSelected(which)
        );
        builder.show();
    }

    public void imprimir2(View view) throws IOException {
        try{
            if(!taxaList.get(0).contains("-")) {
                ingressos2(view);
            }
            else {
                fichas2(view);
            }
        }catch(Exception e){
            makeText(PosTransactionActivity.this, "Erro ao preparar impressão", Toast.LENGTH_SHORT).show();
        }
    }

    public void ingressos2(View view) throws IOException {
        try{
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                email = user.getEmail();

            db.collection("promotores/"+email+"/vendas").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> itemList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                itemList.add(document.get("data").toString());
                            }

                            int width = 800;
                            int height = 800;
                            int dimen = width < height ? width : height;
                            qrgEncoder = new QRGEncoder(email, null, QRGContents.Type.TEXT, dimen);

                            TextView t10 = view.findViewById(R.id.t10);
                            TextView t11 = view.findViewById(R.id.t11);
                            TextView t12 = view.findViewById(R.id.t12);
                            TextView t125 = view.findViewById(R.id.t125);
                            TextView t13 = view.findViewById(R.id.t13);
                            TextView t14 = view.findViewById(R.id.t14);
                            TextView t15 = view.findViewById(R.id.t15);
                            TextView t45 = view.findViewById(R.id.t45);
                            TextView t107 = view.findViewById(R.id.t107);
                            TextView t108 = view.findViewById(R.id.t108);
                            TextView t33 = view.findViewById(R.id.t33);
                            TextView t34 = view.findViewById(R.id.t34);
                            TextView t330 = view.findViewById(R.id.t330);

                            ImageView i2 = view.findViewById(R.id.i2);
                            View v2 = view.findViewById(R.id.l2);
                            makeText(PosTransactionActivity.this, "Provider", Toast.LENGTH_SHORT).show();
                            PosPrintProvider customPosPrintProvider = new PosPrintProvider(PosTransactionActivity.this);

                            for(int j=0; j<valorList.size(); j++){
                                t10.setText(nameList.get(j).toUpperCase());
                                t11.setText("VALOR: R$ " + valorList.get(j));
                                t12.setText("TAXA: R$ " + taxaList.get(j));
                                t125.setText(descricaoList.get(j));
                                t33.setText("CLASSIFICAÇÃO - " + classificacaoList.get(j));
                                t34.setText(tipoList.get(j));
                                t13.setText("VÁLIDO ATÉ: " + new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
                                t14.setText("ENDEREÇO: " + enderecoList.get(j));
                                t45.setText("FORMA DE PAGAMENTO - " + paymentMethod);
                                t330.setText("Camarote");
                                t107.setText("__________________ 1/" + String.valueOf(itemList.size()) + " __________________");
                                t108.setText("__________________ 1/" + String.valueOf(itemList.size()) + " __________________");

                                t15.setText(new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss").format(Calendar.getInstance().getTime()));
                                i2.setImageBitmap(Bitmap.createBitmap(qrgEncoder.getBitmap(), 100, 100, 600, 600));
                                v2.setDrawingCacheEnabled(true);
                                v2.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                                v2.layout(0, 0,  680, v2.getMeasuredHeight());
                                v2.buildDrawingCache(true);
                                Bitmap b = Bitmap.createBitmap(v2.getDrawingCache());
                                b = Bitmap.createScaledBitmap(b, 350, b.getHeight() - 430, true);
                                customPosPrintProvider.addBitmap(b);
                                customPosPrintProvider.addLine("\n\n\n----------------------------");
                            }
                            customPosPrintProvider.setConnectionCallback(new StoneCallbackInterface() {
                                @Override
                                public void onSuccess() {
                                    makeText(PosTransactionActivity.this, "Ingresso impresso", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError() {
                                    makeText(PosTransactionActivity.this, "Erro ao imprimir: " + customPosPrintProvider.getListOfErrors(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            customPosPrintProvider.execute();




                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
            }
        }catch(Exception e){
            makeText(PosTransactionActivity.this, "Erro ao imprimir", Toast.LENGTH_SHORT).show();
        }
    }

    public void fichas2(View view) throws IOException {
        try{
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                email = user.getEmail();
            }

            db.collection("promotores/"+email+"/vendas").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> itemList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                itemList.add(document.get("data").toString());
                            }

                            int width = 350;
                            int height = 350;
                            int dimen = width < height ? width : height;
                            qrgEncoder = new QRGEncoder(email, null, QRGContents.Type.TEXT, dimen);

                            PosPrintProvider customPosPrintProvider = new PosPrintProvider(PosTransactionActivity.this);

                            TextView t0 = view.findViewById(R.id.t0);
                            TextView t1 = view.findViewById(R.id.t1);
                            TextView t2 = view.findViewById(R.id.t2);
                            TextView t3 = view.findViewById(R.id.t3);
                            TextView t4 = view.findViewById(R.id.t4);
                            TextView t5 = view.findViewById(R.id.t5);
                            TextView t6 = view.findViewById(R.id.t6);
                            TextView t7 = view.findViewById(R.id.t7);
                            TextView t107 = view.findViewById(R.id.t107);
                            TextView t108 = view.findViewById(R.id.t108);
                            ImageView i1 = view.findViewById(R.id.i1);
                            View v = view.findViewById(R.id.l1);

                            for(int j=0; j<valorList.size(); j++){
                                t0.setText(eventoList.get(j).toUpperCase());
                                t1.setText(nameList.get(j).toUpperCase());
                                t2.setText(valorList.get(j));
                                t3.setText("VÁLIDO ATÉ: " + new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
                                t4.setText(new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss").format(Calendar.getInstance().getTime()));
                                t5.setText(email);
                                t6.setText("FORMA DE PAGAMENTO - " + paymentMethod);
                                t7.setText(UUID.randomUUID().toString());
                                t107.setText("__________________ 1/" + String.valueOf(itemList.size()) + " __________________");
                                t108.setText("__________________ 1/" + String.valueOf(itemList.size()) + " __________________");

                                i1.setImageBitmap(Bitmap.createBitmap(qrgEncoder.getBitmap(), 45, 45, 260, 260));
                                v.setDrawingCacheEnabled(true);
                                v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                                v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
                                v.buildDrawingCache(true);
                                Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
                                b = Bitmap.createScaledBitmap(b, 360, b.getHeight()-300, true);

                                customPosPrintProvider.addBitmap(b);
                                customPosPrintProvider.addLine("\n\n\n----------------------------");

                    //            ImageView i5 = findViewById(R.id.i5);
                    //            i5.setImageBitmap(b);
                            }
                            customPosPrintProvider.setConnectionCallback(new StoneCallbackInterface() {
                                @Override
                                public void onSuccess() {
                                    makeText(PosTransactionActivity.this, "Produto impresso", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError() {
                                    makeText(PosTransactionActivity.this, "Erro ao imprimir: " + customPosPrintProvider.getListOfErrors(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            customPosPrintProvider.execute();



                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        }catch(Exception e){
            makeText(PosTransactionActivity.this, "Erro ao imprimir", Toast.LENGTH_SHORT).show();
        }
    }

    public void pulseiras2(View view) throws IOException {
        if(ip.getText().toString().isEmpty()){
            makeText(PosTransactionActivity.this, "Endereço IP inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if(port.getText().toString().isEmpty()){
            makeText(PosTransactionActivity.this, "Porta inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        Socket client = new Socket(ip.getText().toString(), Integer.parseInt(port.getText().toString()));

        PrintStream oStream = new PrintStream(client.getOutputStream(), true, "UTF-8");

        oStream.println("-------------------------------------------------\r\n");
        oStream.println(" NAME     : DEMO CLIENT\r\n");
        oStream.println(" CODE  : 00000234242\r\n");
        oStream.println(" ADDRESS   : Street 52\r\n");
        oStream.println(" Phone : 2310-892345\r\n");
        oStream.println("-------------------------------------------------\r\n");

        oStream.flush();

        oStream.close();
        client.close();
    }
}
