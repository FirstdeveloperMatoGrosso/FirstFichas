package br.com.firstingressos.dashboard.sdkdemo.activities.impressao;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.stone.posandroid.providers.PosPrintProvider;
import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.menu.MenuActivity;
import stone.application.interfaces.StoneCallbackInterface;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ImpressaoActivity extends AppCompatActivity {

    private EditText ip, port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_impressao);

        ip = findViewById(R.id.ip);
        port = findViewById(R.id.port);
    }

    public void imprimir(View view) throws IOException {
        if(ip.getText().toString().isEmpty()){
            Toast.makeText(ImpressaoActivity.this, "Endereço IP inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if(port.getText().toString().isEmpty()){
            Toast.makeText(ImpressaoActivity.this, "Porta inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Socket client = new Socket(ip.getText().toString(), Integer.parseInt(port.getText().toString()));

            PrintStream oStream = new PrintStream(client.getOutputStream(), true, "UTF-8");

            oStream.println("-------------------------\r\n");
            oStream.println(" NOME: FIRST INGRESSOS\r\n");
            oStream.println(" CODE: 00000234242\r\n");
            oStream.println(" ADDRESS: Street 52\r\n");
            oStream.println(" Phone: 2310-892345\r\n");
            oStream.println("------------------------\r\n");

            oStream.flush();

            oStream.close();
            client.close();
        }catch (Exception e){
            Toast.makeText(ImpressaoActivity.this, "Impressora não encontrada", Toast.LENGTH_SHORT).show();
        }
    }

    public void voltar(View view){
        startActivity( new Intent( this, MenuActivity.class));
        finish();
    }
}

