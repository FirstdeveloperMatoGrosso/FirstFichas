package br.com.firstingressos.dashboard.sdkdemo.activities.menuFichasProduto;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.com.firstingressos.dashboard.sdkdemo.activities.PosTransactionActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.ingressos.Evento;
import br.com.firstingressos.dashboard.sdkdemo.activities.ingressos.ProdutoAdapter;
import br.com.firstingressos.dashboard.sdkdemo.activities.menuFichas.MenuFichasActivity;
import br.com.firstingressos.dashboard.R;

public class MenuFichasProdutoActivity extends AppCompatActivity {
    int qtdItensCarrinho;
    TextView textView;
    GridView listView;
    String categoria, email;
    private ArrayList<Evento> itemList = new ArrayList<>();
    private List<Evento> carrinhoList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();
    private List<String> valorList = new ArrayList<>();
    private List<String> taxaList = new ArrayList<>();
    private List<String> capaList = new ArrayList<>();
    private List<String> enderecoList = new ArrayList<>();
    private List<String> eventoList = new ArrayList<>();
    TextView amountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_menu_fichas_produto);

        qtdItensCarrinho = 0;
        itemList = new ArrayList<>();
        carrinhoList = new ArrayList<>();
        nameList = new ArrayList<>();
        valorList = new ArrayList<>();
        taxaList = new ArrayList<>();
        capaList = new ArrayList<>();
        enderecoList = new ArrayList<>();
        eventoList = new ArrayList<>();

        textView = (TextView) findViewById(R.id.viewQtdItensCarrinho);
        textView.setText(String.valueOf(qtdItensCarrinho));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            categoria = bundle.getString("categoria");

            listView = findViewById(R.id.dynamic_list);

            textView.setText(String.valueOf(qtdItensCarrinho));
            amountEditText = findViewById(R.id.textTotal);

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                email = user.getEmail();
            }
            db.collection("promotores/"+email+"/produtos/").whereEqualTo("categoria", categoria).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    itemList.add(new Evento(document.get("capa").toString(), document.get("nome").toString(), document.get("descricao").toString(), document.get("valor").toString(), "", email, "", "", "", document.get("evento").toString(), "", "", ""));
                                }
                                ProdutoAdapter arrayAdapter = new ProdutoAdapter(MenuFichasProdutoActivity.this, R.layout.list_row, itemList);
                                listView.setAdapter(arrayAdapter);

                                listView.setOnItemClickListener((adapterView, view, i, l) -> {
                                    Evento obj = (Evento) adapterView.getAdapter().getItem(i);

                                    if(itemList.get(i).getQtd().toString() != "")
                                        itemList.get(i).setQtd(String.valueOf(Integer.parseInt(itemList.get(i).getQtd().toString())+1));
                                    else{
                                        itemList.get(i).setQtd("1");
                                    }
                                    arrayAdapter.notifyDataSetChanged();

                                    Evento item = new Evento(obj.getImage().toString(), obj.getName().toString(), obj.getAddress().toString(), obj.getValor().toString(), obj.getTaxa().toString(), obj.getId().toString(), obj.getData().toString(), obj.getTipo().toString(), "", obj.getEvento().toString(), "", "", "");
                                    carrinhoList.add(item);
                                    qtdItensCarrinho = carrinhoList.size();
                                    textView.setText(String.valueOf(qtdItensCarrinho));

                                    double total = 0.0;
                                    for(int j=0; j<carrinhoList.size(); j++){
                                        total = total + Float.parseFloat(carrinhoList.get(j).getValor().substring(3, carrinhoList.get(j).getValor().length()).replace(".", "").replace(",", "."));
                                    }
                                    amountEditText.setText("R$ " + String.valueOf(String.format("%.2f", total)));
                                });
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    });

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void prosseguir(View view){
        Intent intent = new Intent(this, PosTransactionActivity.class);
        Bundle bundle = new Bundle();
        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<String> valorList = new ArrayList<String>();
        ArrayList<String> taxaList = new ArrayList<String>();
        ArrayList<String> capaList = new ArrayList<String>();
        ArrayList<String> enderecoList = new ArrayList<String>();
        ArrayList<String> descricaoList = new ArrayList<String>();
        ArrayList<String> eventoList = new ArrayList<String>();
        ArrayList<String> classificacaoList = new ArrayList<String>();
        ArrayList<String> tipoList = new ArrayList<String>();
        for(int i=0; i<carrinhoList.size(); i++){
            nameList.add(carrinhoList.get(i).getName());
            valorList.add(carrinhoList.get(i).getValor());
            taxaList.add("-");
            capaList.add(carrinhoList.get(i).getImage());
            enderecoList.add(carrinhoList.get(i).getAddress());
            eventoList.add(carrinhoList.get(i).getEvento());
            descricaoList.add(carrinhoList.get(i).getDescricao());
            classificacaoList.add("-");
            tipoList.add("-");
        }
        bundle.putStringArrayList("nameList", (ArrayList<String>)nameList);
        bundle.putStringArrayList("valorList", (ArrayList<String>)valorList);
        bundle.putStringArrayList("taxaList", (ArrayList<String>)taxaList);
        bundle.putStringArrayList("capaList", (ArrayList<String>)capaList);
        bundle.putStringArrayList("enderecoList", (ArrayList<String>)enderecoList);
        bundle.putStringArrayList("descricaoList", (ArrayList<String>)descricaoList);
        bundle.putStringArrayList("eventoList", (ArrayList<String>)eventoList);
        bundle.putStringArrayList("classificacaoList", (ArrayList<String>)classificacaoList);
        bundle.putStringArrayList("tipoList", (ArrayList<String>)tipoList);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public void voltar(View view){
        startActivity( new Intent( this, MenuFichasActivity.class));
        finish();
    }
}
