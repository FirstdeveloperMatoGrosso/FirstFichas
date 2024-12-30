package br.com.firstingressos.dashboard.sdkdemo.activities.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.menu.MenuActivity;

public class PerfilActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText editTextNome, editTextTelefone, editTextEstado, editTextCidade, editTextBairro, editTextRua, editTextNumero, editTextCep;
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_perfil);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }

        editTextNome = findViewById(R.id.nome);
        editTextTelefone = findViewById(R.id.telefone);
        editTextEstado = findViewById(R.id.estado);
        editTextCidade = findViewById(R.id.cidade);
        editTextBairro = findViewById(R.id.bairro);
        editTextRua = findViewById(R.id.rua);
        editTextNumero = findViewById(R.id.numero);
        editTextCep = findViewById(R.id.cep);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.e("Error", email);
        DocumentReference ref = db.collection("promotores").document(email);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.e("Error", "e.toString()");
                try {
                    editTextNome.setText(documentSnapshot.get("nome").toString());
                    editTextTelefone.setText(documentSnapshot.get("telefone").toString());
                    editTextEstado.setText(documentSnapshot.get("estado").toString());
                    editTextCidade.setText(documentSnapshot.get("cidade").toString());
                    editTextBairro.setText(documentSnapshot.get("bairro").toString());
                    editTextRua.setText(documentSnapshot.get("rua").toString());
                    editTextNumero.setText(documentSnapshot.get("numero").toString());
                    editTextCep.setText(documentSnapshot.get("cep").toString());
                }catch(Exception e){
                    Log.e("Error", e.toString());
                    editTextNome.setText("");
                    editTextTelefone.setText("");
                    editTextEstado.setText("");
                    editTextCidade.setText("");
                    editTextBairro.setText("");
                    editTextRua.setText("");
                    editTextNumero.setText("");
                    editTextCep.setText("");
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
        startActivity( new Intent( PerfilActivity.this, MenuActivity.class));
        finish();
    }

    public void salvar(View view){
        String nome = editTextNome.getText().toString();
        String telefone = editTextTelefone.getText().toString();
        String estado = editTextEstado.getText().toString();
        String cidade = editTextCidade.getText().toString();
        String bairro = editTextBairro.getText().toString();
        String rua = editTextRua.getText().toString();
        String numero = editTextNumero.getText().toString();
        String cep = editTextCep.getText().toString();

        if(nome.isEmpty()){
            editTextNome.setError("Nome não pode estar vazio");
        }
        if(telefone.isEmpty()){
            editTextTelefone.setError("Telefone não pode estar vazio");
        }else{
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference ref = db.collection("promotores").document(email);
            ref.update("nome", nome);
            ref.update("telefone", telefone);
            ref.update("estado", estado);
            ref.update("cidade", cidade);
            ref.update("bairro", bairro);
            ref.update("rua", rua);
            ref.update("numero", numero);
            ref.update("cep", cep);

            startActivity( new Intent( PerfilActivity.this, MenuActivity.class));
            finish();
        }
    }
}