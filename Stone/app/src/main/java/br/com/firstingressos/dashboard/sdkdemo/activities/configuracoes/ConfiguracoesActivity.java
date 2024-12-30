package br.com.firstingressos.dashboard.sdkdemo.activities.configuracoes;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.menu.MenuActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfiguracoesActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText editTextIpTef, editTextIpPrint;
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_configuracoes);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }
        editTextIpTef = findViewById(R.id.ipTef);
        editTextIpPrint = findViewById(R.id.ipPrint);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("promotores").document(email);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.e("Error", "e.toString()");
                try {
                    editTextIpTef.setText(documentSnapshot.get("ipTef").toString());
                    editTextIpPrint.setText(documentSnapshot.get("ipPrint").toString());
                }catch(Exception e){
                    Log.e("Error", e.toString());
                    editTextIpTef.setText("");
                    editTextIpPrint.setText("");
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void voltar(View view){
        startActivity( new Intent( ConfiguracoesActivity.this, MenuActivity.class));
        finish();
    }

    public void salvar(View view){
        String ipTef = editTextIpTef.getText().toString();
        String ipPrint = editTextIpPrint.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference ref = db.collection("promotores").document(email);
        ref.update("ipTef", ipTef);
        ref.update("ipPrint", ipPrint);

        startActivity( new Intent( ConfiguracoesActivity.this, MenuActivity.class));
        finish();
    }
}