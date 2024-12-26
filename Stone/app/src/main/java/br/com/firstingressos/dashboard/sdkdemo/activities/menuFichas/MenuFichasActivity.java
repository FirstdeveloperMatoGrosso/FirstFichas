package br.com.firstingressos.dashboard.sdkdemo.activities.menuFichas;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

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

import br.com.firstingressos.dashboard.sdkdemo.activities.ingressos.Evento;
import br.com.firstingressos.dashboard.sdkdemo.activities.ingressos.ProdutoAdapter;
import br.com.firstingressos.dashboard.sdkdemo.activities.menuPrintType.MenuPrintTypeActivity;
import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.menuFichasProduto.MenuFichasProdutoActivity;

public class MenuFichasActivity extends AppCompatActivity {

    GridView listView;
    String email;
    private ArrayList<Evento> itemList = new ArrayList<>();
    private ArrayList<String> categorias = new ArrayList<>();
    private ArrayList<String> capas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_menu_fichas);

        listView = (GridView)findViewById(R.id.listView);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            Log.i("Nilo", email);
        }

        db.collection("promotores/"+email+"/categorias").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                categorias.add((String) document.get("nome"));
                                capas.add((String) document.get("capa"));
                            }

                            for(int i=0;i<categorias.size();i++){
                                Log.i("tag", categorias.get(i));
                                itemList.add(new Evento(capas.get(i), categorias.get(i), "", "", "", "", "", "", "", "", "", "", ""));
                            }

                            ProdutoAdapter arrayAdapter = new ProdutoAdapter(MenuFichasActivity.this, R.layout.list_row, itemList);
                            listView.setAdapter(arrayAdapter);

                            listView.setOnItemClickListener((adapterView, view, i, l) -> {
                                Evento obj = (Evento) adapterView.getAdapter().getItem(i);
                                Evento item = new Evento(obj.getImage().toString(), obj.getName().toString(), obj.getAddress().toString(), obj.getValor().toString(), obj.getTaxa().toString(), obj.getId().toString(), obj.getData().toString(), obj.getTipo().toString(), "", "", "", "", "");

                                Intent intent = new Intent(MenuFichasActivity.this, MenuFichasProdutoActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("categoria", (String) item.getName());
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            });
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void voltar(View view){
        startActivity( new Intent( this, MenuPrintTypeActivity.class));
        finish();
    }
}