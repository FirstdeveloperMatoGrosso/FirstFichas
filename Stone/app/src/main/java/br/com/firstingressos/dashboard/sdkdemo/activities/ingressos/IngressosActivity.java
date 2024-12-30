package br.com.firstingressos.dashboard.sdkdemo.activities.ingressos;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.firstingressos.dashboard.sdkdemo.activities.PosTransactionActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.ValidationActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.menuIngressos.MenuIngressosActivity;
import br.com.firstingressos.dashboard.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class IngressosActivity extends AppCompatActivity {
    int qtdItensCarrinho;
    TextView textView;
    ListView listView;
    String email;

    private ArrayList<Evento> itemList = new ArrayList<>();
    private List<Evento> carrinhoList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();
    private List<String> valorList = new ArrayList<>();
    private List<String> taxaList = new ArrayList<>();
    private List<String> capaList = new ArrayList<>();
    private List<String> enderecoList = new ArrayList<>();
    private List<String> eventoList = new ArrayList<>();
    private List<String> descricaoList = new ArrayList<>();
    private List<String> classificacaoList = new ArrayList<>();
    private List<String> tipoList = new ArrayList<>();
    TextView amountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_ingressos);

        qtdItensCarrinho = 0;
        itemList = new ArrayList<>();
        carrinhoList = new ArrayList<>();
        nameList = new ArrayList<>();
        valorList = new ArrayList<>();
        taxaList = new ArrayList<>();
        capaList = new ArrayList<>();
        enderecoList = new ArrayList<>();
        descricaoList = new ArrayList<>();
        eventoList = new ArrayList<>();
        classificacaoList = new ArrayList<>();
        tipoList = new ArrayList<>();

        textView = (TextView) findViewById(R.id.viewQtdItensCarrinho);
        amountEditText = findViewById(R.id.textTotal);
        listView = findViewById(R.id.dynamic_list);

        textView.setText(String.valueOf(qtdItensCarrinho));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }

        db.collection("promotores/"+email+"/eventos").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                itemList.add(new Evento(document.get("capa").toString(), document.get("nome").toString(), document.get("local").toString(), document.get("valor").toString(), document.get("taxa").toString(), email, document.get("data").toString() + " " + document.get("hora").toString(), document.get("tipo").toString(), "", "", document.get("descricao").toString(), document.get("classificacao").toString(), document.get("tipo").toString()));
                            }
                            EventoAdapter arrayAdapter = new EventoAdapter(IngressosActivity.this, R.layout.card_view, itemList);
                            listView.setAdapter(arrayAdapter);

                            listView.setOnItemClickListener((adapterView, view, i, l) -> {
                                Evento obj = (Evento) adapterView.getAdapter().getItem(i);
                                Evento item = new Evento(obj.getImage().toString(), obj.getName().toString(), obj.getAddress().toString(), obj.getValor().toString(), obj.getTaxa().toString(), obj.getId().toString(), obj.getData().toString(), obj.getTipo().toString(), "", "", obj.getDescricao().toString(), obj.getClassificacao().toString(), obj.getArea().toString());
                                carrinhoList.add(item);
                                qtdItensCarrinho = carrinhoList.size();
                                textView.setText(String.valueOf(qtdItensCarrinho));

                                double total = 0.0;
                                for(int j=0; j<carrinhoList.size(); j++){
                                    total = total + Float.parseFloat(carrinhoList.get(j).getValor().substring(3, carrinhoList.get(j).getValor().length()).replace(".", "").replace(",", "."));
                                    if(!carrinhoList.get(j).getTaxa().contains("-")) {
                                        total = total + Float.parseFloat(carrinhoList.get(j).getTaxa().substring(3, carrinhoList.get(j).getTaxa().length()).replace(".", "").replace(",", "."));
                                    }
                                }
                                amountEditText.setText("R$ " + String.valueOf(String.format("%.2f", total)));
                            });
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void sair(View view){
        startActivity( new Intent( IngressosActivity.this, ValidationActivity.class));
        finish();
    }

    public void voltar(View view){
        startActivity( new Intent( IngressosActivity.this, MenuIngressosActivity.class));
        finish();
    }

    public void prosseguir(View view){
        Intent intent = new Intent(IngressosActivity.this, PosTransactionActivity.class);
        Bundle bundle = new Bundle();
        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<String> valorList = new ArrayList<String>();
        ArrayList<String> taxaList = new ArrayList<String>();
        ArrayList<String> capaList = new ArrayList<String>();
        ArrayList<String> enderecoList = new ArrayList<String>();
        ArrayList<String> descricaoList = new ArrayList<>();
        ArrayList<String> eventoList = new ArrayList<String>();
        ArrayList<String> classificacaoList = new ArrayList<>();
        ArrayList<String> tipoList = new ArrayList<String>();

        for(int i=0; i<carrinhoList.size(); i++){
            nameList.add(carrinhoList.get(i).getName());
            valorList.add(carrinhoList.get(i).getValor());
            taxaList.add(carrinhoList.get(i).getTaxa());
            capaList.add(carrinhoList.get(i).getImage());
            enderecoList.add(carrinhoList.get(i).getAddress());
            descricaoList.add(carrinhoList.get(i).getDescricao());
            eventoList.add("-");
            classificacaoList.add(carrinhoList.get(i).getClassificacao());
            tipoList.add(carrinhoList.get(i).getArea());
        }
        bundle.putStringArrayList("nameList", (ArrayList<String>)nameList);
        bundle.putStringArrayList("valorList", (ArrayList<String>)valorList);
        bundle.putStringArrayList("taxaList", (ArrayList<String>)taxaList);
        bundle.putStringArrayList("capaList", (ArrayList<String>)capaList);
        bundle.putStringArrayList("enderecoList", (ArrayList<String>)enderecoList);
        bundle.putStringArrayList("descricaoList", (ArrayList<String>)descricaoList);
        bundle.putStringArrayList("eventoList", (ArrayList<String>)eventoList);
        bundle.putStringArrayList("classificacaoList", (ArrayList<String>)descricaoList);
        bundle.putStringArrayList("tipoList", (ArrayList<String>)eventoList);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
