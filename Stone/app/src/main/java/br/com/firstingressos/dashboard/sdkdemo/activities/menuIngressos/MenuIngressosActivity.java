package br.com.firstingressos.dashboard.sdkdemo.activities.menuIngressos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import br.com.firstingressos.dashboard.sdkdemo.activities.menuPrintType.MenuPrintTypeActivity;
import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.ingressos.IngressosActivity;

public class MenuIngressosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_menu_ingressos);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void camarote(View view){
        startActivity( new Intent( this, IngressosActivity.class));
        finish();
    }

    public void backstage(View view){
        startActivity( new Intent( this, IngressosActivity.class));
        finish();
    }

    public void pista(View view){
        startActivity( new Intent( this, IngressosActivity.class));
        finish();
    }

    public void voltar(View view){
        startActivity( new Intent( this, MenuPrintTypeActivity.class));
        finish();
    }
}