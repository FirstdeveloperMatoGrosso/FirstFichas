package br.com.firstingressos.dashboard.sdkdemo.activities.menuPrintType;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.menu.MenuActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.menuFichas.MenuFichasActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.menuIngressos.MenuIngressosActivity;

public class MenuPrintTypeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_menu_print_type);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void ingressos(View view){
        startActivity( new Intent( this, MenuIngressosActivity.class));
        finish();
    }

    public void fichas(View view){
        startActivity( new Intent( this, MenuFichasActivity.class));
        finish();
    }

    public void pulseiras(View view){
        startActivity( new Intent( this, MenuIngressosActivity.class));
        finish();
    }

    public void voltar(View view){
        startActivity( new Intent( this, MenuActivity.class));
        finish();
    }
}
