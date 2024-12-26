package br.com.firstingressos.dashboard.sdkdemo.activities;

import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
//import static br.com.stonesdk.sdkdemo.activities.ValidationActivityPermissionsDispatcher.initiateAppWithPermissionCheck;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.firstingressos.dashboard.sdkdemo.activities.aberturaCaixa.AberturaCaixaActivity;
import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.registro.RegistroActivity;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import stone.application.StoneStart;
import stone.application.interfaces.StoneCallbackInterface;
import stone.providers.ActiveApplicationProvider;
import stone.user.UserModel;
import stone.utils.Stone;
import stone.utils.keys.StoneKeyType;

@RuntimePermissions
public class ValidationActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextSenha;
    private static final String TAG = "ValidationActivity";
    private static final int REQUEST_PERMISSION_SETTINGS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.email);
        editTextSenha = findViewById(R.id.senha);

        mAuth = FirebaseAuth.getInstance();

//        initiateAppWithPermissionCheck(ValidationActivity.this);

        Map<StoneKeyType, String> keys = new HashMap<>();

//      PRODUÇÃO
//        keys.put(StoneKeyType.QRCODE_PROVIDERID, "66aa3a26-7595-444c-b762-b16a2ccf53a8");
//        keys.put(StoneKeyType.QRCODE_AUTHORIZATION, "e1159dae-c026-489d-8552-e7706a8f6ae3");

//      SANDBBOX
        keys.put(StoneKeyType.QRCODE_PROVIDERID, "fcb9931a-62f5-439c-99d2-19f7b77f908a");
        keys.put(StoneKeyType.QRCODE_AUTHORIZATION, "4d2d0093-b351-4286-9d80-78cd46c8721d");

        List<UserModel> user = StoneStart.init(this, keys);

//        Toast.makeText(ValidationActivity.this, Stone.getEnvironment().toString(), Toast.LENGTH_SHORT).show();

        Stone.setAppName("First Ingressos");

        List<String> stoneCodeList = new ArrayList<>();
        stoneCodeList.add("206192723");

        ActiveApplicationProvider provider;

        try {
            provider = new ActiveApplicationProvider(this);
            provider.setConnectionCallback(new StoneCallbackInterface() {
                public void onSuccess() {
//                    Toast.makeText(ValidationActivity.this, "Ativou o aplicativo", Toast.LENGTH_SHORT).show();
                }
                public void onError() {
                    Toast.makeText(ValidationActivity.this, "Erro na ativacao do aplicativo, verifique a lista de erros do provider", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onError: " + provider.getListOfErrors().toString());

                }
            });
            provider.activate(stoneCodeList);
        } catch(Exception e){
            Toast.makeText(ValidationActivity.this, "Erro na ativacao do aplicativo", Toast.LENGTH_SHORT).show();
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(ValidationActivity.this, AberturaCaixaActivity.class ));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void entrar(View view){
        String email = editTextEmail.getText().toString();
        String pass = editTextSenha.getText().toString();
        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!pass.isEmpty()) {
                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
//                                Toast.makeText(MainActivity.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ValidationActivity.this, AberturaCaixaActivity.class ));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("err", e.toString());
                                Toast.makeText(ValidationActivity.this, "Erro ao efetuar login", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                editTextSenha.setError("Senha não pode estar vazia");
            }

        } else if (email.isEmpty()) {
            editTextEmail.setError("O e-mail não pode estar vazio");
        } else {
            editTextEmail.setError("Digite um e-mail válido");
        }
    }

    public void registrar(View view){
        startActivity( new Intent( ValidationActivity.this, RegistroActivity.class));
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    public void initiateApp() {
    }

    @Override
    public void onClick(View v) {
        List<String> stoneCodeList = new ArrayList<>();
        // Adicione seu Stonecode abaixo, como string.
        stoneCodeList.add("206192723");

        final ActiveApplicationProvider provider = new ActiveApplicationProvider(this);
        provider.setDialogMessage("Ativando o aplicativo...");
        provider.setDialogTitle("Aguarde");
        provider.setConnectionCallback(new StoneCallbackInterface() {
            /* Metodo chamado se for executado sem erros */
            public void onSuccess() {
//                Toast.makeText(ValidationActivity.this, "Ativado com sucesso, iniciando o aplicativo", Toast.LENGTH_SHORT).show();
                continueApplication();
            }

            /* metodo chamado caso ocorra alguma excecao */
            public void onError() {
                Toast.makeText(ValidationActivity.this, "Erro na ativacao do aplicativo, verifique a lista de erros do provider", Toast.LENGTH_SHORT).show();

                /* Chame o metodo abaixo para verificar a lista de erros. Para mais detalhes, leia a documentacao: */
                Log.e(TAG, "onError: " + provider.getListOfErrors().toString());

            }
        });
        provider.activate(stoneCodeList);
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE})
    public void showDenied() {
        buildPermissionDialog(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                initiateAppWithPermissionCheck(ValidationActivity.this);
            }
        });
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE})
    public void showNeverAskAgain() {
        buildPermissionDialog(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_PERMISSION_SETTINGS);
            }
        });
    }

    private void continueApplication() {
        Intent mainIntent = new Intent(ValidationActivity.this, ValidationActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE})
    public void showRationale(final PermissionRequest request) {
        buildPermissionDialog(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                request.proceed();
            }
        });
    }

    private void buildPermissionDialog(DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Android 6.0")
                .setCancelable(false)
                .setMessage("Com a versão do android igual ou superior ao Android 6.0," +
                        " é necessário que você aceite as permissões para o funcionamento do app.\n\n")
                .setPositiveButton("OK", listener)
                .create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_SETTINGS) {
//            initiateAppWithPermissionCheck(this);
        }
//        ValidationActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}