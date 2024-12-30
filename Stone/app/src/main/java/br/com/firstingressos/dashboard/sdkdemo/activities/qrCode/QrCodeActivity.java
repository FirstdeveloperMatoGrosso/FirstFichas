package br.com.firstingressos.dashboard.sdkdemo.activities.qrCode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import br.com.firstingressos.dashboard.sdkdemo.activities.PosTransactionActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.Vendas;
import br.com.firstingressos.dashboard.sdkdemo.activities.pagamentoRealizado.PagamentoRealizadoActivity;
import br.com.stone.posandroid.providers.PosPrintProvider;
import br.com.firstingressos.dashboard.R;
import stone.application.interfaces.StoneCallbackInterface;

public class QrCodeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextSenha;

    private List<String> nameList = new ArrayList<>();
    private List<String> valorList = new ArrayList<>();
    private List<String> taxaList = new ArrayList<>();
    private List<String> capaList = new ArrayList<>();
    private List<String> enderecoList = new ArrayList<>();
    private List<String> descricaoList = new ArrayList<>();
    private List<String> eventoList = new ArrayList<>();
    private List<String> classificacaoList = new ArrayList<>();
    private List<String> tipoList = new ArrayList<>();
    QRGEncoder qrgEncoder;
    private EditText ip, port;
    String email = "oi@gmail.com";
    String paymentMethod = "";
    String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_qrcode);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }

        editTextSenha = findViewById(R.id.senha);
    }

    public void voltar(View view){
        Bundle bundle = getIntent().getExtras();
        String paymentMethod = "";
        if (bundle != null) {
            paymentMethod = bundle.getString("paymentMethod");
            nameList = bundle.getStringArrayList("nameList");
            valorList = bundle.getStringArrayList("valorList");
            taxaList = bundle.getStringArrayList("taxaList");
            capaList = bundle.getStringArrayList("capaList");
            enderecoList = bundle.getStringArrayList("enderecoList");
            descricaoList = bundle.getStringArrayList("descricaoList");
            eventoList = bundle.getStringArrayList("eventoList");
            classificacaoList = bundle.getStringArrayList("classificacaoList");
            tipoList = bundle.getStringArrayList("tipoList");
        }

        Bundle bundle2 = new Bundle();
        Intent intent = new Intent(QrCodeActivity.this, PosTransactionActivity.class);
        bundle2.putString("paymentMethod", paymentMethod);
        bundle2.putStringArrayList("nameList", (ArrayList<String>)nameList);
        bundle2.putStringArrayList("valorList", (ArrayList<String>)valorList);
        bundle2.putStringArrayList("taxaList", (ArrayList<String>)taxaList);
        bundle2.putStringArrayList("capaList", (ArrayList<String>)capaList);
        bundle2.putStringArrayList("enderecoList", (ArrayList<String>)enderecoList);
        bundle2.putStringArrayList("descricaoList", (ArrayList<String>)descricaoList);
        bundle2.putStringArrayList("eventoList", (ArrayList<String>)eventoList);
        bundle2.putStringArrayList("classificacaoList", (ArrayList<String>)classificacaoList);
        bundle2.putStringArrayList("tipoList", (ArrayList<String>)tipoList);
        intent.putExtras(bundle2);
        startActivity(intent);
        finish();
    }

    public void entrar(View view) throws IOException {
        String email = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();

        String pass = editTextSenha.getText().toString();
        if (!pass.isEmpty()) {
            String finalEmail = email;
            String finalEmail1 = email;
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            try {
                                Bundle bundle = getIntent().getExtras();
                                String value = "";
                                String paymentMethod = "";
                                if (bundle != null) {
                                    paymentMethod = bundle.getString("paymentMethod");
                                    value = bundle.getString("value");
                                    nameList = bundle.getStringArrayList("nameList");
                                    valorList = bundle.getStringArrayList("valorList");
                                    taxaList = bundle.getStringArrayList("taxaList");
                                    capaList = bundle.getStringArrayList("capaList");
                                    enderecoList = bundle.getStringArrayList("enderecoList");
                                    descricaoList = bundle.getStringArrayList("descricaoList");
                                    eventoList = bundle.getStringArrayList("eventoList");
                                    classificacaoList = bundle.getStringArrayList("classificacaoList");
                                    tipoList = bundle.getStringArrayList("tipoList");

                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    String tipo = "ficha";

                                    try {
                                        if (!taxaList.get(0).contains("-")) {
                                            tipo = "ingresso";
                                        }
                                    } catch (Exception e) {
                                    }

                                    for (int j = 0; j < nameList.size(); j++) {
                                        try {
                                            db.collection("promotores/" + finalEmail + "/vendas/").add(new Vendas(valorList.get(j), finalEmail, new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(Calendar.getInstance().getTime()), tipo, "QrCode", "0", nameList.get(j)));
                                        } catch (Exception e) {
                                            db.collection("promotores/" + finalEmail + "/vendas/").add(new Vendas(valorList.get(j), finalEmail, new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(Calendar.getInstance().getTime()), tipo, "QrCode", "0", "Item"));
                                        }
                                    }

                                    try {
                                        imprimir(view);
                                    } catch (Exception e) {
                                        Toast.makeText(QrCodeActivity.this, "Erro ao preparar impressão", Toast.LENGTH_SHORT).show();
                                    }

                                    Intent intent = new Intent(QrCodeActivity.this, PagamentoRealizadoActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (Exception e) {
                                Toast.makeText(QrCodeActivity.this, "Erro na impressão", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("err", e.toString());
                            Toast.makeText(QrCodeActivity.this, "Erro ao confirmar identidade", Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        } else {
            editTextSenha.setError("Senha não pode estar vazia");
        }
    }

    public void imprimir(View view) throws IOException {
        try{
            if(!taxaList.get(0).contains("-")) {
                ingressos(view);
            }
            else {
                fichas(view);
            }
        }catch(Exception e){
            Toast.makeText(QrCodeActivity.this, "Erro ao preparar impressão", Toast.LENGTH_SHORT).show();
        }
    }

    public void ingressos(View view) throws IOException {
        try{
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

                                    TextView t10 = findViewById(R.id.t10);
                                    TextView t11 = findViewById(R.id.t11);
                                    TextView t12 = findViewById(R.id.t12);
                                    TextView t125 = findViewById(R.id.t125);
                                    TextView t13 = findViewById(R.id.t13);
                                    TextView t14 = findViewById(R.id.t14);
                                    TextView t15 = findViewById(R.id.t15);
                                    TextView t45 = findViewById(R.id.t45);
                                    TextView t107 = findViewById(R.id.t107);
                                    TextView t108 = findViewById(R.id.t108);
                                    TextView t33 = findViewById(R.id.t33);
                                    TextView t34 = findViewById(R.id.t34);
                                    TextView t330 = findViewById(R.id.t330);

                                    ImageView i2 = findViewById(R.id.i2);
                                    View v2 = findViewById(R.id.l2);
                                    Toast.makeText(QrCodeActivity.this, "Provider", Toast.LENGTH_SHORT).show();
                                    PosPrintProvider customPosPrintProvider = new PosPrintProvider(QrCodeActivity.this);

                                    for (int j = 0; j < valorList.size(); j++) {
                                        t10.setText(nameList.get(j).toUpperCase());
                                        t11.setText("VALOR: R$ " + valorList.get(j));
                                        t12.setText("TAXA: R$ " + taxaList.get(j));
                                        t125.setText(descricaoList.get(j));
                                        t33.setText("CLASSIFICAÇÃO - " + classificacaoList.get(j));
                                        t34.setText(tipoList.get(j));
                                        t13.setText("VÁLIDO ATÉ: " + new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
                                        t14.setText("ENDEREÇO: " + enderecoList.get(j));
                                        t45.setText("FORMA DE PAGAMENTO - QrCode");
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
                                            Toast.makeText(QrCodeActivity.this, "Ingresso impresso", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onError() {
                                            Toast.makeText(QrCodeActivity.this, "Erro ao imprimir: " + customPosPrintProvider.getListOfErrors(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(QrCodeActivity.this, "Erro ao imprimir", Toast.LENGTH_SHORT).show();
        }
    }

    public void fichas(View view) throws IOException {
        try {
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

                            PosPrintProvider customPosPrintProvider = new PosPrintProvider(QrCodeActivity.this);

                            TextView t0 = findViewById(R.id.t0);
                            TextView t1 = findViewById(R.id.t1);
                            TextView t2 = findViewById(R.id.t2);
                            TextView t3 = findViewById(R.id.t3);
                            TextView t4 = findViewById(R.id.t4);
                            TextView t5 = findViewById(R.id.t5);
                            TextView t6 = findViewById(R.id.t6);
                            TextView t7 = findViewById(R.id.t7);
                            TextView t107 = findViewById(R.id.t107);
                            TextView t108 = findViewById(R.id.t108);
                            ImageView i1 = findViewById(R.id.i1);
                            View v = findViewById(R.id.l1);

                            for(int j=0; j<valorList.size(); j++){
                                t0.setText(eventoList.get(j).toUpperCase());
                                t1.setText(nameList.get(j).toUpperCase());
                                t2.setText(valorList.get(j));
                                t3.setText("VÁLIDO ATÉ: " + new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
                                t4.setText(new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss").format(Calendar.getInstance().getTime()));
                                t5.setText(email);
                                t6.setText("FORMA DE PAGAMENTO - QrCode");
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
                                    Toast.makeText(QrCodeActivity.this, "Produto impresso", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError() {
                                    Toast.makeText(QrCodeActivity.this, "Erro ao imprimir: " + customPosPrintProvider.getListOfErrors(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            customPosPrintProvider.execute();



                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        }catch(Exception e){
            Toast.makeText(QrCodeActivity.this, "Erro ao imprimir", Toast.LENGTH_SHORT).show();
        }
    }

    public void pulseiras(View view) throws IOException {
        if(ip.getText().toString().isEmpty()){
            Toast.makeText(QrCodeActivity.this, "Endereço IP inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if(port.getText().toString().isEmpty()){
            Toast.makeText(QrCodeActivity.this, "Porta inválida", Toast.LENGTH_SHORT).show();
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
