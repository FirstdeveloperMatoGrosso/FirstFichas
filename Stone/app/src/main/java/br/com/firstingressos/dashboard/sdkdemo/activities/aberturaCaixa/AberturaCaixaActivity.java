package br.com.firstingressos.dashboard.sdkdemo.activities.aberturaCaixa;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import br.com.stone.posandroid.providers.PosPrintProvider;
import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.menu.MenuActivity;
import stone.application.interfaces.StoneCallbackInterface;

public class AberturaCaixaActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText valorText, operadorText;
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference ref = db.collection("promotores").document(email);
            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String operador = documentSnapshot.get("operador").toString();

                    if(operador.length() != 0){
                        startActivity( new Intent( AberturaCaixaActivity.this, MenuActivity.class));
                        finish();
                    }
                    else {
                        setContentView(R.layout.activity_abertura_caixa);

                        operadorText = findViewById(R.id.operador);
                        valorText = findViewById(R.id.valor);

                        mAuth = FirebaseAuth.getInstance();
                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void voltar(View view){
        startActivity( new Intent( AberturaCaixaActivity.this, MenuActivity.class));
        finish();
    }

    public String randomGenerator() {
        Random random = new Random();
        long randomNumber = Math.abs(random.nextLong());
        String randomString = Long.toString(randomNumber);
        String tenDigitNumber = randomString.substring(0, 10);
        return tenDigitNumber;
    }

    public void salvar(View view){
        String operador = operadorText.getText().toString();
        String valor = valorText.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference ref = db.collection("promotores").document(email);
        String uuid = randomGenerator();
        ref.update("operador", operador);
        ref.update("valor", valor);
        ref.update("uuid", uuid);

        PosPrintProvider customPosPrintProvider = new PosPrintProvider(AberturaCaixaActivity.this);
        customPosPrintProvider.addLine("\n----------------------------");
        customPosPrintProvider.addLine("ABERTURA DE CAIXA\n\n");
        customPosPrintProvider.addLine("OPERADOR: " + operador);
        customPosPrintProvider.addLine("VALOR: " + valor);
        customPosPrintProvider.addLine("DATA/HORA: " + new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(Calendar.getInstance().getTime()));
        customPosPrintProvider.addLine("ID: "+uuid);

        customPosPrintProvider.setConnectionCallback(new StoneCallbackInterface() {
            @Override
            public void onSuccess() {
                Toast.makeText(AberturaCaixaActivity.this, "Comprovante impresso", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(AberturaCaixaActivity.this, "Erro ao imprimir: " + customPosPrintProvider.getListOfErrors(), Toast.LENGTH_SHORT).show();
            }
        });
        customPosPrintProvider.execute();

        startActivity( new Intent( AberturaCaixaActivity.this, MenuActivity.class));
        finish();
    }
}