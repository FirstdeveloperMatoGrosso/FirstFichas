package br.com.firstingressos.dashboard.sdkdemo.activities.novo_promotor;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.menu.MenuActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class NovoPromotorActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextNome, editTextSenha, editTextTelefone, editTextEstado, editTextCidade, editTextBairro, editTextRua, editTextNumero, editTextCep, editTextIpTef, editTextIpPrint;
    private Switch ingressos, pulseiras, fichas;
    boolean valueIngressos, valuePulseiras, valueFichas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_promotor);

        editTextEmail = findViewById(R.id.email);
        editTextSenha = findViewById(R.id.senha);
        editTextNome = findViewById(R.id.nome);
        editTextTelefone = findViewById(R.id.telefone);
        editTextEstado = findViewById(R.id.estado);
        editTextCidade = findViewById(R.id.cidade);
        editTextBairro = findViewById(R.id.bairro);
        editTextRua = findViewById(R.id.rua);
        editTextNumero = findViewById(R.id.numero);
        editTextCep = findViewById(R.id.cep);

        ingressos = (Switch) findViewById(R.id.ingressos);
        ingressos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    valueIngressos = true;
                } else {
                    valueIngressos = false;
                }
            }
        });

        pulseiras = (Switch) findViewById(R.id.pulseiras);
        pulseiras.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    valuePulseiras = true;
                } else {
                    valuePulseiras = false;
                }
            }
        });


        fichas = (Switch) findViewById(R.id.fichas);
        fichas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    valueFichas = true;
                } else {
                    valueFichas = false;
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
        startActivity( new Intent( NovoPromotorActivity.this, MenuActivity.class));
        finish();
    }

    public void salvar(View view){
        String email = editTextEmail.getText().toString();
        String nome = editTextNome.getText().toString();
        String senha = editTextSenha.getText().toString();
        String telefone = editTextTelefone.getText().toString();
        String estado = editTextEstado.getText().toString();
        String cidade = editTextCidade.getText().toString();
        String bairro = editTextBairro.getText().toString();
        String rua = editTextRua.getText().toString();
        String numero = editTextNumero.getText().toString();
        String cep = editTextCep.getText().toString();
        String ipTef = "0.0.0.0";
        String ipPrint = "0.0.0.0";

        if (email.isEmpty()){
            editTextEmail.setError("E-mail não pode estar vazio");
        }
        if(nome.isEmpty()){
            editTextNome.setError("Nome não pode estar vazio");
        }
        if(telefone.isEmpty()){
            editTextTelefone.setError("Telefone não pode estar vazio");
        }
        if(senha.isEmpty()){
            editTextTelefone.setError("Senha não pode estar vazia");
        }else{
            mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(NovoPromotorActivity.this, "Registro criado com sucesso", Toast.LENGTH_SHORT).show();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        db.collection("promotores").document(email)
                                .set(new Promotor(nome, telefone, estado, cidade, bairro, rua, numero, cep, ipTef, ipPrint, valueIngressos, valuePulseiras, valueFichas));
                        startActivity( new Intent( NovoPromotorActivity.this, MenuActivity.class));
                        finish();
                    } else{
                        Toast.makeText(NovoPromotorActivity.this, "Erro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
