package br.com.firstingressos.dashboard.sdkdemo.activities.fluxoCaixa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import br.com.firstingressos.dashboard.sdkdemo.activities.BaseTransactionActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.Vendas;
import br.com.firstingressos.dashboard.sdkdemo.activities.ingressos.Evento;
import br.com.firstingressos.dashboard.sdkdemo.activities.pagamentoRealizado.PagamentoRealizadoActivity;
import br.com.stone.posandroid.providers.PosPrintProvider;
import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.ingressos.ProdutoAdapter;
import br.com.firstingressos.dashboard.sdkdemo.activities.menu.MenuActivity;
import stone.application.interfaces.StoneCallbackInterface;

public class FluxoCaixaActivity  extends AppCompatActivity {
    GridView listView;
    private FirebaseAuth mAuth;
    Button confirmarSenha, btnVoltarSenha;
    String email;
    LinearLayout containerOpcoes, containerSenha;
    private EditText editTextSenha;
    private ArrayList<Evento> itemList = new ArrayList<>();
    private ArrayList<String> categorias = new ArrayList<>();
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_fluxo_caixa);

        listView = (GridView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.total);
        mAuth = FirebaseAuth.getInstance();

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
                            double total = 0.0;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.get("fluxoCaixa").toString().contains("0")) {
                                    itemList.add(new Evento("", document.get("nome").toString(), document.get("data").toString().substring(0, 10), document.get("valor").toString(), document.get("data").toString().substring(13, document.get("data").toString().length()), "", "", "", "", "", "", "", ""));

                                    try {
                                        total = total + Float.parseFloat(document.get("valor").toString().replace(",", "."));
                                    } catch (Exception e) {
                                        try {
                                            total = total + Float.parseFloat(document.get("valor").toString().substring(2,document.get("valor").toString().length()).replace(",", "."));
                                        } catch (Exception es) {
                                        }
                                    }
                                }
                            }

                            textView.setText("Total R$ " + String.valueOf(String.format("%.2f", total)));

                            FluxoCaixaAdapter arrayAdapter = new FluxoCaixaAdapter(FluxoCaixaActivity.this, R.layout.list_row_fluxo_caixa, itemList);
                            listView.setAdapter(arrayAdapter);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        Button relatoriobtn = findViewById(R.id.relatorio);
        relatoriobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(FluxoCaixaActivity.this);
                                    View view1 = LayoutInflater.from(FluxoCaixaActivity.this).inflate(R.layout.bottom_sheet_layout, null);

                                    bottomSheetDialog.setContentView(view1);
                                    bottomSheetDialog.show();
                                    Button dismissBtn = view1.findViewById(R.id.dismiss);
                                    Button ingressos = view1.findViewById(R.id.ingressos);
                                    Button fichas = view1.findViewById(R.id.fichas);
                                    containerOpcoes = view1.findViewById(R.id.containerOpcoes);
                                    containerSenha = view1.findViewById(R.id.containerSenha);

                                    confirmarSenha = view1.findViewById(R.id.btnConfirmarSenha);
                                    btnVoltarSenha = view1.findViewById(R.id.btnVoltarSenha);

                                    btnVoltarSenha.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            containerOpcoes.setVisibility(View.VISIBLE);
                                            containerSenha.setVisibility(View.GONE);
                                        }
                                    });

                                    dismissBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            bottomSheetDialog.dismiss();
                                        }
                                    });

                                    ingressos.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ingressos(view, false, view1, bottomSheetDialog);
                                        }
                                    });

                                    fichas.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            fichas(view, false, view1, bottomSheetDialog);
                                        }
                                    });

                                    bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {

                                        }
                                    });
                                }
                            });
        Button fechamentbtn = findViewById(R.id.fechamento);
        fechamentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(FluxoCaixaActivity.this);
                                    View view1 = LayoutInflater.from(FluxoCaixaActivity.this).inflate(R.layout.bottom_sheet_layout, null);

                                    bottomSheetDialog.setContentView(view1);
                                    bottomSheetDialog.show();
                                    Button dismissBtn = view1.findViewById(R.id.dismiss);
                                    Button ingressos = view1.findViewById(R.id.ingressos);
                                    Button fichas = view1.findViewById(R.id.fichas);
                                    containerOpcoes = view1.findViewById(R.id.containerOpcoes);
                                    containerSenha = view1.findViewById(R.id.containerSenha);

                                    confirmarSenha = view1.findViewById(R.id.btnConfirmarSenha);
                                    btnVoltarSenha = view1.findViewById(R.id.btnVoltarSenha);

                                    btnVoltarSenha.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            containerOpcoes.setVisibility(View.VISIBLE);
                                            containerSenha.setVisibility(View.GONE);
                                        }
                                    });
                                    dismissBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            bottomSheetDialog.dismiss();
                                        }
                                    });

                                    ingressos.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ingressos(view, true, view1, bottomSheetDialog);
                                        }
                                    });

                                    fichas.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            fichas(view, true, view1, bottomSheetDialog);
                                        }
                                    });

                                    bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {

                                        }
                                    });
                                }
                            });
    }


    public void ingressos(View view, Boolean print, View view1, BottomSheetDialog bottomSheetDialog) {

        containerSenha.setVisibility(View.VISIBLE);
        containerOpcoes.setVisibility(View.GONE);
        confirmarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final String email = user.getEmail();

            EditText editTextSenha = view1.findViewById(R.id.senha);

            String pass = editTextSenha.getText().toString();
            if (!pass.isEmpty()) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("promotores/"+email+"/vendas").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentReference ref = db.collection("promotores").document(email);
                            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String operador = documentSnapshot.get("operador").toString();
                                    String valor = documentSnapshot.get("valor").toString();

                                    double total = 0.0;
                                    double totalDinheiro = 0.0;
                                    double totalQrCode = 0.0;
                                    double totalCrtCredito = 0.0;
                                    double totalCrtDebito = 0.0;
                                    double totalPix = 0.0;
                                    double totalVoucher = 0.0;

                                    PosPrintProvider customPosPrintProvider = new PosPrintProvider(FluxoCaixaActivity.this);
                                    customPosPrintProvider.addLine("\n----------------------------");
                                    customPosPrintProvider.addLine("FECHAMENTO DE CAIXA - INGRESSOS\n\n");
                                    customPosPrintProvider.addLine("OPERADOR: " + operador);
                                    customPosPrintProvider.addLine("ABERTURA: " + valor);
                                    customPosPrintProvider.addLine("DATA/HORA: " + new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(Calendar.getInstance().getTime()));
                                    customPosPrintProvider.addLine("ID: " + documentSnapshot.get("uuid").toString());
                                    customPosPrintProvider.addLine("\nITENS ---------------- \n");
                                    int qtdDinheiro = 0;
                                    int qtdQrCode = 0;
                                    int qtdCrtCredito = 0;
                                    int qtdCrtDebito = 0;
                                    int qtdVoucher = 0;
                                    int qtdPix = 0;
                                    int count = 1;

                                    if (print) {
                                        DocumentReference ref = db.collection("promotores").document(email);
                                        ref.update("operador", "");
                                    }

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.get("fluxoCaixa").toString().contains("0") && document.get("tipo").toString().contains("ingresso")) {
                                            if (print){
                                                DocumentReference refItem = db.collection("promotores/" + email + "/vendas").document(document.getId());
                                                refItem.update("fluxoCaixa", "1");
                                            }

                                            customPosPrintProvider.addLine("1 "+document.get("nome").toString()+" "+document.get("valor").toString());
                                            count = count + 1;

                                            if(document.get("formaPagamento").toString().contains("Dinheiro")){
                                                qtdDinheiro = qtdDinheiro + 1;
                                                try {
                                                    totalDinheiro = totalDinheiro + Float.parseFloat(document.get("valor").toString().replace(",", "."));
                                                } catch (Exception e) {
                                                    try {
                                                        totalDinheiro = totalDinheiro + Float.parseFloat(document.get("valor").toString().substring(2,document.get("valor").toString().length()).replace(",", "."));
                                                    } catch (Exception es) {
                                                    }
                                                }
                                            }
                                            if(document.get("formaPagamento").toString().contains("QrCode")){
                                                qtdQrCode = qtdQrCode + 1;
                                                try {
                                                    totalQrCode = totalQrCode + Float.parseFloat(document.get("valor").toString().replace(",", "."));
                                                } catch (Exception e) {
                                                    try {
                                                        totalQrCode = totalQrCode + Float.parseFloat(document.get("valor").toString().substring(2,document.get("valor").toString().length()).replace(",", "."));
                                                    } catch (Exception es) {
                                                    }
                                                }
                                            }
                                            if(document.get("formaPagamento").toString().contains("Cartão de débito")){
                                                qtdCrtDebito = qtdCrtDebito + 1;
                                                try {
                                                    totalCrtDebito = totalCrtDebito + Float.parseFloat(document.get("valor").toString().replace(",", "."));
                                                } catch (Exception e) {
                                                    try {
                                                        totalCrtDebito = totalCrtDebito + Float.parseFloat(document.get("valor").toString().substring(2,document.get("valor").toString().length()).replace(",", "."));
                                                    } catch (Exception es) {
                                                    }
                                                }
                                            }
                                            if(document.get("formaPagamento").toString().contains("Voucher")){
                                                qtdVoucher = qtdVoucher + 1;
                                                try {
                                                    totalVoucher = totalVoucher + Float.parseFloat(document.get("valor").toString().replace(",", "."));
                                                } catch (Exception e) {
                                                    try {
                                                        totalVoucher = totalVoucher + Float.parseFloat(document.get("valor").toString().substring(2,document.get("valor").toString().length()).replace(",", "."));
                                                    } catch (Exception es) {
                                                    }
                                                }
                                            }
                                            if(document.get("formaPagamento").toString().contains("Pix")){
                                                qtdPix = qtdPix + 1;
                                                try {
                                                    totalPix = totalPix + Float.parseFloat(document.get("valor").toString().replace(",", "."));
                                                } catch (Exception e) {
                                                    try {
                                                        totalPix = totalPix + Float.parseFloat(document.get("valor").toString().substring(2,document.get("valor").toString().length()).replace(",", "."));
                                                    } catch (Exception es) {
                                                    }
                                                }
                                            }
                                            if(document.get("formaPagamento").toString().contains("Cartão de crédito")){
                                                qtdCrtCredito = qtdCrtCredito + 1;
                                                try {
                                                    totalCrtCredito = totalCrtCredito + Float.parseFloat(document.get("valor").toString().replace(",", "."));
                                                } catch (Exception e) {
                                                    try {
                                                        totalCrtCredito = totalCrtCredito + Float.parseFloat(document.get("valor").toString().substring(2,document.get("valor").toString().length()).replace(",", "."));
                                                    } catch (Exception es) {
                                                    }
                                                }
                                            }

                                            try {
                                                total = total + Float.parseFloat(document.get("valor").toString().replace(",", "."));
                                            } catch (Exception e) {
                                                try {
                                                    total = total + Float.parseFloat(document.get("valor").toString().substring(2,document.get("valor").toString().length()).replace(",", "."));
                                                } catch (Exception es) {
                                                }
                                            }
                                        }
                                    }
                                    customPosPrintProvider.addLine("\n\n---------------------------\nTOTAL: R$" + String.valueOf(String.format("%.2f", total)));
                                    customPosPrintProvider.addLine("Qtd Cartão de débito: " + String.valueOf(qtdCrtDebito) + " - R$ " + String.valueOf(String.format("%.2f", totalCrtDebito)));
                                    customPosPrintProvider.addLine("Qtd Cartão de crédito: " + String.valueOf(qtdCrtCredito) + " - R$ " + String.valueOf(String.format("%.2f", totalCrtCredito)));
                                    customPosPrintProvider.addLine("Qtd Voucher: " + String.valueOf(qtdVoucher) + " - R$ " + String.valueOf(String.format("%.2f", totalVoucher)));
                                    customPosPrintProvider.addLine("Qtd PIX: " + String.valueOf(qtdPix) + " - R$ " + String.valueOf(String.format("%.2f", totalPix)));
                                    customPosPrintProvider.addLine("Qtd QrCode: " + String.valueOf(qtdQrCode) + " - R$ " + String.valueOf(String.format("%.2f", totalQrCode)));
                                    customPosPrintProvider.addLine("Qtd Dinheiro: " + String.valueOf(qtdDinheiro) + " - R$ " + String.valueOf(String.format("%.2f", totalDinheiro)));

                                    customPosPrintProvider.addLine("\nASSINATURAS\n\n");
                                    customPosPrintProvider.addLine("________________________________");
                                    customPosPrintProvider.addLine("\nCAIXA\n");
                                    customPosPrintProvider.addLine("________________________________");
                                    customPosPrintProvider.addLine("SUPERVISOR\n");
                                    customPosPrintProvider.addLine("________________________________");
                                    customPosPrintProvider.addLine("GERENTE\n");

                                    customPosPrintProvider.addLine("\n\nCaixa fechado");
                                    customPosPrintProvider.addLine("Operador " + operador);
                                    customPosPrintProvider.addLine("Fechado " + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));

                                    customPosPrintProvider.setConnectionCallback(new StoneCallbackInterface() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(FluxoCaixaActivity.this, "Comprovante impresso", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onError() {
                                            Toast.makeText(FluxoCaixaActivity.this, "Erro ao imprimir: " + customPosPrintProvider.getListOfErrors(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    customPosPrintProvider.execute();

                                    bottomSheetDialog.dismiss();
                                }
                            });
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());

                            bottomSheetDialog.dismiss();
                        }
                    }
                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("err", e.toString());
                                Toast.makeText(FluxoCaixaActivity.this, "Erro ao confirmar identidade", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                editTextSenha.setError("Senha não pode estar vazia");
            }
        }
            }
        });
    }

    public void fichas(View view, Boolean print, View view1, BottomSheetDialog bottomSheetDialog) {

        containerSenha.setVisibility(View.VISIBLE);
        containerOpcoes.setVisibility(View.GONE);
        confirmarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final String email = user.getEmail();

            EditText editTextSenha = view1.findViewById(R.id.senha);

            String pass = editTextSenha.getText().toString();
            if (!pass.isEmpty()) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("promotores/"+email+"/vendas").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentReference ref = db.collection("promotores").document(email);
                            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String operador = documentSnapshot.get("operador").toString();
                                    String valor = documentSnapshot.get("valor").toString();

                                    double total = 0.0;
                                    double totalDinheiro = 0.0;
                                    double totalQrCode = 0.0;
                                    double totalCrtCredito = 0.0;
                                    double totalCrtDebito = 0.0;
                                    double totalPix = 0.0;
                                    double totalVoucher = 0.0;

                                    PosPrintProvider customPosPrintProvider = new PosPrintProvider(FluxoCaixaActivity.this);
                                    customPosPrintProvider.addLine("\n----------------------------");
                                    customPosPrintProvider.addLine("FECHAMENTO DE CAIXA - FICHAS\n\n");
                                    customPosPrintProvider.addLine("OPERADOR: " + operador);
                                    customPosPrintProvider.addLine("ABERTURA: " + valor);
                                    customPosPrintProvider.addLine("DATA/HORA: " + new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(Calendar.getInstance().getTime()));
                                    customPosPrintProvider.addLine("ID: " + documentSnapshot.get("uuid").toString());
                                    customPosPrintProvider.addLine("\nITENS ---------------- \n");
                                    int qtdDinheiro = 0;
                                    int qtdQrCode = 0;
                                    int qtdCrtCredito = 0;
                                    int qtdCrtDebito = 0;
                                    int qtdVoucher = 0;
                                    int qtdPix = 0;

                                    int count = 1;

                                    if (print) {
                                        DocumentReference ref = db.collection("promotores").document(email);
                                        ref.update("operador", "");
                                    }

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.get("fluxoCaixa").toString().contains("0") && document.get("tipo").toString().contains("ficha")) {
                                            if (print) {
                                                DocumentReference refItem = db.collection("promotores/" + email + "/vendas").document(document.getId());
                                                refItem.update("fluxoCaixa", "1");
                                            }

                                            customPosPrintProvider.addLine("1 "+document.get("nome").toString()+" "+document.get("valor").toString());
                                            count = count + 1;
                                            if(document.get("formaPagamento").toString().contains("Dinheiro")){
                                                qtdDinheiro = qtdDinheiro + 1;
                                                try {
                                                    totalDinheiro = totalDinheiro + Float.parseFloat(document.get("valor").toString().replace(",", "."));
                                                } catch (Exception e) {
                                                    try {
                                                        totalDinheiro = totalDinheiro + Float.parseFloat(document.get("valor").toString().substring(2,document.get("valor").toString().length()).replace(",", "."));
                                                    } catch (Exception es) {
                                                    }
                                                }
                                            }
                                            if(document.get("formaPagamento").toString().contains("QrCode")){
                                                qtdQrCode = qtdQrCode + 1;
                                                try {
                                                    totalQrCode = totalQrCode + Float.parseFloat(document.get("valor").toString().replace(",", "."));
                                                } catch (Exception e) {
                                                    try {
                                                        totalQrCode = totalQrCode + Float.parseFloat(document.get("valor").toString().substring(2,document.get("valor").toString().length()).replace(",", "."));
                                                    } catch (Exception es) {
                                                    }
                                                }
                                            }
                                            if(document.get("formaPagamento").toString().contains("Cartão de débito")){
                                                qtdCrtDebito = qtdCrtDebito + 1;
                                                try {
                                                    totalCrtDebito = totalCrtDebito + Float.parseFloat(document.get("valor").toString().replace(",", "."));
                                                } catch (Exception e) {
                                                    try {
                                                        totalCrtDebito = totalCrtDebito + Float.parseFloat(document.get("valor").toString().substring(2,document.get("valor").toString().length()).replace(",", "."));
                                                    } catch (Exception es) {
                                                    }
                                                }
                                            }
                                            if(document.get("formaPagamento").toString().contains("Voucher")){
                                                qtdVoucher = qtdVoucher + 1;
                                                try {
                                                    totalVoucher = totalVoucher + Float.parseFloat(document.get("valor").toString().replace(",", "."));
                                                } catch (Exception e) {
                                                    try {
                                                        totalVoucher = totalVoucher + Float.parseFloat(document.get("valor").toString().substring(2,document.get("valor").toString().length()).replace(",", "."));
                                                    } catch (Exception es) {
                                                    }
                                                }
                                            }
                                            if(document.get("formaPagamento").toString().contains("Pix")){
                                                qtdPix = qtdPix + 1;
                                                try {
                                                    totalPix = totalPix + Float.parseFloat(document.get("valor").toString().replace(",", "."));
                                                } catch (Exception e) {
                                                    try {
                                                        totalPix = totalPix + Float.parseFloat(document.get("valor").toString().substring(2,document.get("valor").toString().length()).replace(",", "."));
                                                    } catch (Exception es) {
                                                    }
                                                }
                                            }
                                            if(document.get("formaPagamento").toString().contains("Cartão de crédito")){
                                                qtdCrtCredito = qtdCrtCredito + 1;
                                                try {
                                                    totalCrtCredito = totalCrtCredito + Float.parseFloat(document.get("valor").toString().replace(",", "."));
                                                } catch (Exception e) {
                                                    try {
                                                        totalCrtCredito = totalCrtCredito + Float.parseFloat(document.get("valor").toString().substring(2,document.get("valor").toString().length()).replace(",", "."));
                                                    } catch (Exception es) {
                                                    }
                                                }
                                            }

                                            try {
                                                total = total + Float.parseFloat(document.get("valor").toString().replace(",", "."));
                                            } catch (Exception e) {
                                                try {
                                                    total = total + Float.parseFloat(document.get("valor").toString().substring(2,document.get("valor").toString().length()).replace(",", "."));
                                                } catch (Exception es) {
                                                }
                                            }
                                        }
                                    }
                                    customPosPrintProvider.addLine("\n\n---------------------------\nTOTAL: R$" + String.valueOf(String.format("%.2f", total)));
                                    customPosPrintProvider.addLine("Qtd Cartão de débito: " + String.valueOf(qtdCrtDebito) + " - R$ " + String.valueOf(String.format("%.2f", totalCrtDebito)));
                                    customPosPrintProvider.addLine("Qtd Cartão de crédito: " + String.valueOf(qtdCrtCredito) + " - R$ " + String.valueOf(String.format("%.2f", totalCrtCredito)));
                                    customPosPrintProvider.addLine("Qtd Voucher: " + String.valueOf(qtdVoucher) + " - R$ " + String.valueOf(String.format("%.2f", totalVoucher)));
                                    customPosPrintProvider.addLine("Qtd PIX: " + String.valueOf(qtdPix) + " - R$ " + String.valueOf(String.format("%.2f", totalPix)));
                                    customPosPrintProvider.addLine("Qtd QrCode: " + String.valueOf(qtdQrCode) + " - R$ " + String.valueOf(String.format("%.2f", totalQrCode)));
                                    customPosPrintProvider.addLine("Qtd Dinheiro: " + String.valueOf(qtdDinheiro) + " - R$ " + String.valueOf(String.format("%.2f", totalDinheiro)));


                                    customPosPrintProvider.addLine("\nASSINATURAS\n\n");
                                    customPosPrintProvider.addLine("________________________________");
                                    customPosPrintProvider.addLine("\nCAIXA\n");
                                    customPosPrintProvider.addLine("________________________________");
                                    customPosPrintProvider.addLine("SUPERVISOR\n");
                                    customPosPrintProvider.addLine("________________________________");
                                    customPosPrintProvider.addLine("GERENTE\n");

                                    customPosPrintProvider.addLine("\n\nCaixa fechado");
                                    customPosPrintProvider.addLine("Operador " + operador);
                                    customPosPrintProvider.addLine("Fechado " + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
                                    customPosPrintProvider.setConnectionCallback(new StoneCallbackInterface() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(FluxoCaixaActivity.this, "Comprovante impresso", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onError() {
                                            Toast.makeText(FluxoCaixaActivity.this, "Erro ao imprimir: " + customPosPrintProvider.getListOfErrors(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    customPosPrintProvider.execute();

                                    bottomSheetDialog.dismiss();
                                }
                            });
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());

                            bottomSheetDialog.dismiss();
                        }
                    }
                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("err", e.toString());
                                Toast.makeText(FluxoCaixaActivity.this, "Erro ao confirmar identidade", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                editTextSenha.setError("Senha não pode estar vazia");
            }
        }
    }
});
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void voltar(View view){
        startActivity( new Intent( this, MenuActivity.class));
        finish();
    }
}