package br.com.firstingressos.dashboard.sdkdemo.activities.menuProduto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import br.com.firstingressos.dashboard.sdkdemo.activities.novaCategoria.NovaCategoriaActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.novoProduto.NovoProdutoActivity;
import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.menu.MenuActivity;

public class MenuProdutoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_menu_produto);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void addProduto(View view){
        startActivity( new Intent( this, NovoProdutoActivity.class));
        finish();
    }

    public void addCategoria(View view){
        startActivity( new Intent( this, NovaCategoriaActivity.class));
        finish();
    }

    public void voltar(View view){
        startActivity( new Intent( this, MenuActivity.class));
        finish();
    }
}