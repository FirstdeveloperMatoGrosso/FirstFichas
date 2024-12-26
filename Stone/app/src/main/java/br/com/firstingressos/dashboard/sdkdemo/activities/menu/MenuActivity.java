package br.com.firstingressos.dashboard.sdkdemo.activities.menu;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import br.com.firstingressos.dashboard.sdkdemo.activities.ManageStoneCodeActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.ValidationActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.configuracoes.ConfiguracoesActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.estacionamento.EstacionamentoActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.fluxoCaixa.FluxoCaixaActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.impressao.ImpressaoActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.menuPrintType.MenuPrintTypeActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.menuProduto.MenuProdutoActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.novaCategoria.NovaCategoriaActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.novo_evento.NovoEventoActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.novo_promotor.NovoPromotorActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.perfil.PerfilActivity;
import br.com.firstingressos.dashboard.R;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MenuActivity extends AppCompatActivity {
    String uid = "";
    boolean admin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_menu);
        findViewById(R.id.addPromotor).setVisibility(View.GONE);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getEmail();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference ref = db.collection("promotores").document(uid);
            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    try {
                        admin = (boolean) documentSnapshot.get("admin");
                        if(admin){
                            findViewById(R.id.addPromotor).setVisibility(View.VISIBLE);
                        }
                    }catch(Exception error1) {
                        Log.e(TAG, "The exception caught while executing the process. (error1)");
                        error1.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void configurar(View view){
        startActivity( new Intent( MenuActivity.this, ConfiguracoesActivity.class));
        finish();
    }

    public void criarNovoProduto(View view){
        startActivity( new Intent( MenuActivity.this, MenuProdutoActivity.class));
        finish();
    }

    public void perfil(View view){
        startActivity( new Intent( MenuActivity.this, PerfilActivity.class));
        finish();
    }

    public void estacionamento(View view){
        startActivity( new Intent( MenuActivity.this, EstacionamentoActivity.class));
        finish();
    }

    public void sair(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity( new Intent( MenuActivity.this, ValidationActivity.class));
        finish();
    }

    public void criarNovoPromotor(View view){
        startActivity( new Intent( MenuActivity.this, NovoPromotorActivity.class));
        finish();
    }

    public void criarNovaCategoria(View view){
        startActivity( new Intent( MenuActivity.this, NovaCategoriaActivity.class));
        finish();
    }

    public void criarNovoEvento(View view){
        startActivity( new Intent( MenuActivity.this, NovoEventoActivity.class));
        finish();
    }

    public void totem(View view){
        startActivity( new Intent( MenuActivity.this, MenuPrintTypeActivity.class));
        finish();
    }

    public void fluxoCaixa(View view){
        startActivity( new Intent( MenuActivity.this, FluxoCaixaActivity.class));
        finish();
    }

    public void stoneCode(View view){
        startActivity( new Intent( MenuActivity.this, ManageStoneCodeActivity.class));
        finish();
    }

    public void impressao(View view){
        startActivity( new Intent( MenuActivity.this, ImpressaoActivity.class));
        finish();
    }
}