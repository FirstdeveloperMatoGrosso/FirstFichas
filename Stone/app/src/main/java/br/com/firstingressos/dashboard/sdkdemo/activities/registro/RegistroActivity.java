package br.com.firstingressos.dashboard.sdkdemo.activities.registro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.firstingressos.dashboard.sdkdemo.activities.ValidationActivity;
import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.ingressos.IngressosActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextSenha, editTextSenha_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_registro);

        editTextEmail = findViewById(R.id.email);
        editTextSenha = findViewById(R.id.senha);
        editTextSenha_2 = findViewById(R.id.senha_2);

        mAuth = FirebaseAuth.getInstance();
    }

    public void entrar(View view){
        startActivity( new Intent( RegistroActivity.this, ValidationActivity.class));
        finish();
    }

    public void registrar(View view){
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();
        String senha_2 = editTextSenha_2.getText().toString();

        if (email.isEmpty()){
            editTextEmail.setError("E-mail não pode estar vazio");
        }
        if(senha.isEmpty()){
            editTextSenha.setError("Senha não pode estar vazia");
        }
        if(!senha.equals(senha_2)){
            editTextSenha.setError("Senhas não correspondem");
        }else{
            mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegistroActivity.this, "Cadastro criado com sucesso", Toast.LENGTH_SHORT).show();
                        startActivity( new Intent( RegistroActivity.this, IngressosActivity.class));
                        finish();
                    } else{
                        Toast.makeText(RegistroActivity.this, "Erro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
