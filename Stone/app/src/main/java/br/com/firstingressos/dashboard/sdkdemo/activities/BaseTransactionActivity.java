package br.com.firstingressos.dashboard.sdkdemo.activities;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
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
import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.ingressos.Evento;
import br.com.firstingressos.dashboard.sdkdemo.activities.ingressos.ProdutoAdapter;
import br.com.firstingressos.dashboard.sdkdemo.activities.menuPrintType.MenuPrintTypeActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.pagamentoRealizado.PagamentoRealizadoActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.qrCode.QrCodeActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.senha.SenhaActivity;
import br.com.stone.posandroid.providers.PosPrintProvider;
import stone.application.enums.Action;
import stone.application.enums.ErrorsEnum;
import stone.application.enums.InstalmentTransactionEnum;
import stone.application.enums.TypeOfTransactionEnum;
import stone.application.interfaces.StoneActionCallback;
import stone.application.interfaces.StoneCallbackInterface;
import stone.database.transaction.TransactionObject;
import stone.providers.BaseTransactionProvider;
import stone.user.UserModel;
import stone.utils.Stone;

public abstract class BaseTransactionActivity<T extends BaseTransactionProvider> extends AppCompatActivity implements StoneActionCallback {

    private FirebaseAuth mAuth;
    private BaseTransactionProvider transactionProvider;
    protected final TransactionObject transactionObject = new TransactionObject();
    String transactionTypeBtn = "credit";
    TextView amountEditText, logTextView, typePayment;
    EditText valueText;
    Button cancelTransactionButton;
    AppCompatImageButton creditBtn, debitBtn, voucherBtn, pixBtn, qrCodeBtn, dinheiroBtn;

    Dialog builder;


    private List<String> nameList = new ArrayList<>();
    private List<String> valorList = new ArrayList<>();
    private List<String> taxaList = new ArrayList<>();
    private List<String> capaList = new ArrayList<>();
    private List<String> enderecoList = new ArrayList<>();
    private List<String> descricaoList = new ArrayList<>();
    private List<String> classificacaoList = new ArrayList<>();
    private List<String> tipoList = new ArrayList<>();
    private List<String> eventoList = new ArrayList<>();
    TextView textView;
    private EditText ip, port;
    QRGEncoder qrgEncoder;
    String email = "oi@gmail.com";
    String paymentMethod = "";
    static Context context;

    private ArrayList<Evento> itemList = new ArrayList<>();
    ListView listView;
    ProdutoAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);

        BaseTransactionActivity.context = getApplicationContext();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_carrinho);

        amountEditText = findViewById(R.id.textTotal);
        listView = findViewById(R.id.dynamic_list);

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
            double total = 0.0;
            for(int j=0; j<valorList.size(); j++){
                total = total + Float.parseFloat(valorList.get(j).substring(3, valorList.get(j).length()).replace(".", "").replace(",", "."));
                if(!taxaList.get(j).contains("-")) {
                    total = total + Float.parseFloat(taxaList.get(j).substring(3, taxaList.get(j).length()).replace(".", "").replace(",", "."));
                }
                itemList.add(new Evento(capaList.get(j), nameList.get(j), enderecoList.get(j), valorList.get(j), taxaList.get(j), "", "", "", "", eventoList.get(j), descricaoList.get(j), classificacaoList.get(j), tipoList.get(j)));
            }
            arrayAdapter = new ProdutoAdapter(this, R.layout.carrinho_card_view, itemList);
            listView.setAdapter(arrayAdapter);
            amountEditText.setText("R$ " + String.valueOf(String.format("%.2f", total)));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    makeText(BaseTransactionActivity.this, nameList.get(i) + " removido do carrinho", LENGTH_SHORT).show();
                    nameList.remove(i);
                    valorList.remove(i);
                    taxaList.remove(i);
                    capaList.remove(i);
                    itemList.remove(i);
                    enderecoList.remove(i);
                    descricaoList.remove(i);
                    eventoList.remove(i);
                    arrayAdapter = new ProdutoAdapter(BaseTransactionActivity.this, R.layout.carrinho_card_view, itemList);
                    listView.setAdapter(arrayAdapter);

                    double total = 0.0;
                    for(int j=0; j<valorList.size(); j++){
                        total = total + Float.parseFloat(valorList.get(j).substring(3, valorList.get(j).length()).replace(".", "").replace(",", "."));
                        total = total + Float.parseFloat(taxaList.get(j).substring(3, taxaList.get(j).length()).replace(".", "").replace(",", "."));
                    }
                    amountEditText.setText("R$ " + String.valueOf(String.format("%.2f", total)));
                }
            });
        }



        Button pagarbtn = findViewById(R.id.pagar);
        pagarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(BaseTransactionActivity.this, R.style.AppBottomSheetDialogTheme);
                View view1 = LayoutInflater.from(BaseTransactionActivity.this).inflate(R.layout.activity_transaction, null);

                CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        800);
                view1.setLayoutParams(layoutParams);

                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();
                Button dismissBtn = view1.findViewById(R.id.cancelTransactionButton);
                Button btnConfirmarDinheiro = view1.findViewById(R.id.btnConfirmarDinheiro);
                Button btnVoltarDinheiro = view1.findViewById(R.id.btnVoltarDinheiro);

                LinearLayout containerStone = view1.findViewById(R.id.containerStone);
                LinearLayout containerSenha = view1.findViewById(R.id.containerSenha);
                dismissBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                    }
                });

                btnConfirmarDinheiro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        entrar(view1);
                    }
                });
                btnVoltarDinheiro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        containerStone.setVisibility(View.VISIBLE);
                        containerSenha.setVisibility(View.GONE);
                    }
                });

                typePayment = view1.findViewById(R.id.typePayment);
                amountEditText = view1.findViewById(R.id.amountEditText);
                logTextView = view1.findViewById(R.id.logTextView);
                cancelTransactionButton = view1.findViewById(R.id.cancelTransactionButton);

                creditBtn = view1.findViewById(R.id.creditButton);
                debitBtn = view1.findViewById(R.id.debitButton);
                voucherBtn = view1.findViewById(R.id.voucherButton);
                pixBtn = view1.findViewById(R.id.pixButton);
                qrCodeBtn = view1.findViewById(R.id.qrCodeButton);
                dinheiroBtn = view1.findViewById(R.id.dinheiroButton);

                creditBtn.setOnClickListener(v -> {
                    creditBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_green));
                    transactionTypeBtn = "credit";
                    typePayment.setText("credit");
                    sendTransaction(view1);
                });
                debitBtn.setOnClickListener(v -> {
                    debitBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_green));
                    transactionTypeBtn = "debit";
                    typePayment.setText("debit");
                    sendTransaction(view1);
                });
                voucherBtn.setOnClickListener(v -> {
                    voucherBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_green));
                    transactionTypeBtn = "voucher";
                    typePayment.setText("voucher");
                    sendTransaction(view1);
                });
                pixBtn.setOnClickListener(v -> {
                    pixBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_green));
                    transactionTypeBtn = "pix";
                    typePayment.setText("pix");
                    sendTransaction(view1);
                });
                qrCodeBtn.setOnClickListener(v -> {
                    qrCodeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_green));
                    transactionTypeBtn = "qrCode";
                    typePayment.setText("qrCode");
                    sendTransaction(view1);

                });
                dinheiroBtn.setOnClickListener(v -> {
//                    dinheiroBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_green));
                    transactionTypeBtn = "dinheiro";
                    typePayment.setText("dinheiro");
                    containerStone.setVisibility(View.GONE);
                    containerSenha.setVisibility(View.VISIBLE);
                });
                cancelTransactionButton.setOnClickListener(v -> {
                    try {
                        transactionProvider.abortPayment();
                    } catch(Exception e){

                    }
                    bottomSheetDialog.dismiss();
                });


                builder = new Dialog(BaseTransactionActivity.this);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                textView = (TextView) view1.findViewById(R.id.amountEditText);
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
                    double total = 0.0;
                    for(int i=0; i<valorList.size(); i++){
                        total = total + Float.parseFloat(valorList.get(i).substring(3, valorList.get(i).length()).replace(".", "").replace(",", "."));
                        if(!taxaList.get(i).contains("-")) {
                            total = total + Float.parseFloat(taxaList.get(i).substring(3, taxaList.get(i).length()).replace(".", "").replace(",", "."));
                        }
                    }
                    textView.setText(String.valueOf(String.format("%.2f", total)));
                }
            }
        });
    }

    public void sendTransaction(View view1){
        InstalmentTransactionEnum installmentsEnum = InstalmentTransactionEnum.getAt(0);

        // Informa a quantidade de parcelas.
        transactionObject.setInstalmentTransaction(InstalmentTransactionEnum.getAt(0));

        // Verifica a forma de pagamento selecionada.
        TypeOfTransactionEnum transactionType;
        switch (transactionTypeBtn) {
            case "credit":
                transactionType = TypeOfTransactionEnum.CREDIT;
                break;
            case "debit":
                transactionType = TypeOfTransactionEnum.DEBIT;
                break;
            case "voucher":
                transactionType = TypeOfTransactionEnum.VOUCHER;
                break;
            case "pix":
                transactionType = TypeOfTransactionEnum.PIX;
                break;
            case "dinheiro":
                return;
            case "qrCode":
                qrCode(view1);
                return;
            default:
                transactionType = TypeOfTransactionEnum.CREDIT;
        }

        transactionObject.setInitiatorTransactionKey(null);
        transactionObject.setTypeOfTransaction(transactionType);
        transactionObject.setCapture(false);
        transactionObject.setAmount(amountEditText.getText().toString().replace(",", "").replace(".", ""));

        transactionProvider = buildTransactionProvider();
        transactionProvider.setConnectionCallback(this);
        transactionProvider.execute();
    }

    public void voltar(View view){
        Intent intent = new Intent(BaseTransactionActivity.this, MenuPrintTypeActivity.class);
        startActivity(intent);
        finish();
    }

    public void qrCode(View view1){
        TextView editText =(TextView) view1.findViewById(R.id.amountEditText);
        String value = editText.getText().toString();

        Bundle bundle2 = new Bundle();
        Intent intent = new Intent(this, QrCodeActivity.class);
        bundle2.putString("value", value);
        bundle2.putString("paymentMethod", "QrCode");
        bundle2.putStringArrayList("nameList", (ArrayList<String>)nameList);
        bundle2.putStringArrayList("valorList", (ArrayList<String>)valorList);
        bundle2.putStringArrayList("taxaList", (ArrayList<String>)taxaList);
        bundle2.putStringArrayList("capaList", (ArrayList<String>)capaList);
        bundle2.putStringArrayList("enderecoList", (ArrayList<String>)enderecoList);
        bundle2.putStringArrayList("eventoList", (ArrayList<String>)eventoList);
        bundle2.putStringArrayList("descricaoList", (ArrayList<String>)descricaoList);
        bundle2.putStringArrayList("classificacaoList", (ArrayList<String>)classificacaoList);
        bundle2.putStringArrayList("tipoList", (ArrayList<String>)tipoList);
        intent.putExtras(bundle2);
        startActivity(intent);
        finish();
    }

    protected String getAuthorizationMessage() {
        return transactionProvider.getMessageFromAuthorize();
    }

    protected T buildTransactionProvider() {
        return null;
    }

    protected boolean providerHasErrorEnum(ErrorsEnum errorsEnum) {
        return transactionProvider.theListHasError(errorsEnum);
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {
        runOnUiThread(() -> Toast.makeText(BaseTransactionActivity.this, "Erro: " + transactionProvider.getListOfErrors(), Toast.LENGTH_SHORT).show());
        Bundle bundle2 = new Bundle();
        Intent intent = new Intent(BaseTransactionActivity.this, PosTransactionActivity.class);
        bundle2.putStringArrayList("nameList", (ArrayList<String>)nameList);
        bundle2.putStringArrayList("valorList", (ArrayList<String>)valorList);
        bundle2.putStringArrayList("taxaList", (ArrayList<String>)taxaList);
        bundle2.putStringArrayList("capaList", (ArrayList<String>)capaList);
        bundle2.putStringArrayList("enderecoList", (ArrayList<String>)enderecoList);
        bundle2.putStringArrayList("eventoList", (ArrayList<String>)eventoList);
        bundle2.putStringArrayList("descricaoList", (ArrayList<String>)descricaoList);
        bundle2.putStringArrayList("classificacaoList", (ArrayList<String>)classificacaoList);
        bundle2.putStringArrayList("tipoList", (ArrayList<String>)tipoList);
        intent.putExtras(bundle2);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStatusChanged(final Action action) {
        runOnUiThread(() -> logTextView.append(action.name() + "\n"));

        if (action == Action.TRANSACTION_WAITING_QRCODE_SCAN) {
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(transactionObject.getQRCode());

            runOnUiThread(() -> {
                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                builder.show();
            });
        } else {
            runOnUiThread(() -> builder.dismiss());
        }
    }

    protected BaseTransactionProvider getTransactionProvider() {
        return transactionProvider;
    }

    protected UserModel getSelectedUserModel() {
        return Stone.getUserModel(0);
    }



    public void entrar(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final String email = user.getEmail();

            EditText editTextSenha = view.findViewById(R.id.senha);

            String pass = editTextSenha.getText().toString();
            if (!pass.isEmpty()) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                try{

                                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                                        String tipo = "ficha";
                                        try {
                                            if (!taxaList.get(0).contains("-")) {
                                                tipo = "ingresso";
                                            }
                                        } catch (Exception e) {
                                            Toast.makeText(BaseTransactionActivity.this, "Erro no bundle", Toast.LENGTH_SHORT).show();
                                        }


                                        for (int j = 0; j < nameList.size(); j++) {
                                            try {
                                                db.collection("promotores/" + email + "/vendas/").add(new Vendas(valorList.get(j), email, new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(Calendar.getInstance().getTime()), tipo, "Dinheiro", "0", nameList.get(j)));
                                            } catch (Exception e) {
                                                Toast.makeText(BaseTransactionActivity.this, "Erro no Firebase", Toast.LENGTH_SHORT).show();
                                                db.collection("promotores/" + email + "/vendas/").add(new Vendas(valorList.get(j), email, new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(Calendar.getInstance().getTime()), tipo, "Dinheiro", "0", "Item"));
                                            }
                                        }

                                        try {
                                            imprimir(view);
                                            Intent intent = new Intent(BaseTransactionActivity.this, PagamentoRealizadoActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } catch (Exception e) {
                                            Toast.makeText(BaseTransactionActivity.this, "Erro ao preparar impressão", Toast.LENGTH_SHORT).show();
                                        }
                                } catch (Exception e) {
                                    Toast.makeText(BaseTransactionActivity.this, "Erro ao salvar dados", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("err", e.toString());
                                Toast.makeText(BaseTransactionActivity.this, "Erro ao confirmar identidade", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                editTextSenha.setError("Senha não pode estar vazia");
            }
        }
    }

    public void imprimir(View view) throws IOException {
        try {
            if (!taxaList.get(0).contains("-")) {
                ingressos(view);
            } else {
                fichas(view);
            }
        }catch(Exception e){
            Toast.makeText(BaseTransactionActivity.this, "Erro ao verificar tipo de impressão", Toast.LENGTH_SHORT).show();
        }
    }

    public void ingressos(View view) throws IOException {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                email = user.getEmail();

                db.collection("promotores/" + email + "/vendas").get()
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

                                    Toast.makeText(BaseTransactionActivity.this, "Provider", Toast.LENGTH_SHORT).show();
                                    PosPrintProvider customPosPrintProvider = new PosPrintProvider(BaseTransactionActivity.this);

                                    for (int j = 0; j < valorList.size(); j++) {
                                        t10.setText(nameList.get(j).toUpperCase());
                                        t11.setText("VALOR: R$ " + valorList.get(j));
                                        t12.setText("TAXA: R$ " + taxaList.get(j));
                                        t125.setText(descricaoList.get(j));
                                        t33.setText("CLASSIFICAÇÃO - " + classificacaoList.get(j));
                                        t34.setText(tipoList.get(j));
                                        t13.setText("VÁLIDO ATÉ: " + new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
                                        t14.setText("ENDEREÇO: " + enderecoList.get(j));
                                        t45.setText("FORMA DE PAGAMENTO - Dinheiro");
                                        t330.setText("Camarote");
                                        t107.setText("__________________ 1/" + String.valueOf(itemList.size()) + " __________________");
                                        t108.setText("__________________ 1/" + String.valueOf(itemList.size()) + " __________________");

                                        t15.setText(new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss").format(Calendar.getInstance().getTime()));
                                        i2.setImageBitmap(Bitmap.createBitmap(qrgEncoder.getBitmap(), 100, 100, 600, 600));
                                        v2.setDrawingCacheEnabled(true);
                                        v2.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                                        v2.layout(0, 0, 680, v2.getMeasuredHeight());
                                        v2.buildDrawingCache(true);
                                        Bitmap b = Bitmap.createBitmap(v2.getDrawingCache());
                                        b = Bitmap.createScaledBitmap(b, 350, b.getHeight() - 430, true);
                                        customPosPrintProvider.addBitmap(b);
                                        customPosPrintProvider.addLine("\n\n\n----------------------------");
                                    }

                                    customPosPrintProvider.setConnectionCallback(new StoneCallbackInterface() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(BaseTransactionActivity.this, "Ingresso impresso", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onError() {
                                            Toast.makeText(BaseTransactionActivity.this, "Erro ao imprimir: " + customPosPrintProvider.getListOfErrors(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(BaseTransactionActivity.this, "Erro ao imprimir", Toast.LENGTH_SHORT).show();
        }
    }

    public void fichas(View view) throws IOException {
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

                                PosPrintProvider customPosPrintProvider = new PosPrintProvider(BaseTransactionActivity.this);

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
                                    t6.setText("FORMA DE PAGAMENTO - Dinheiro");
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
                                }
                                customPosPrintProvider.setConnectionCallback(new StoneCallbackInterface() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(BaseTransactionActivity.this, "Produto impresso", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError() {
                                        Toast.makeText(BaseTransactionActivity.this, "Erro ao imprimir: " + customPosPrintProvider.getListOfErrors(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                customPosPrintProvider.execute();



                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(BaseTransactionActivity.this, "Erro ao imprimir", Toast.LENGTH_SHORT).show();
        }
    }

    public void pulseiras(View view) throws IOException {
        if(ip.getText().toString().isEmpty()){
            Toast.makeText(BaseTransactionActivity.this, "Endereço IP inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if(port.getText().toString().isEmpty()){
            Toast.makeText(BaseTransactionActivity.this, "Porta inválida", Toast.LENGTH_SHORT).show();
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
