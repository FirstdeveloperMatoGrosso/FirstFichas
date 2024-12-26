package br.com.firstingressos.dashboard.sdkdemo.activities.pagamentoRealizado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import br.com.firstingressos.dashboard.sdkdemo.activities.menuPrintType.MenuPrintTypeActivity;
import br.com.firstingressos.dashboard.R;

public class PagamentoRealizadoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_pagamento_realizado);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void inicio(View view){
        startActivity( new Intent( PagamentoRealizadoActivity.this, MenuPrintTypeActivity.class));
        finish();
    }
}
