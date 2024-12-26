package br.com.firstingressos.dashboard.sdkdemo.activities.novo_evento;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.menu.MenuActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.annotation.Nullable;

public class NovoEventoActivity  extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText editTextTipo, editTextClassificacao, editTextData, editTextDescricao, editTextHora, editTextLocal, editTextNome, editTextTaxa, editTextValor;
    private ImageView profilePic;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    String email = "";
    private Switch ingressos, pulseiras, fichas;
    boolean valueIngressos, valuePulseiras, valueFichas;
    Bitmap bmp;
    ByteArrayOutputStream baos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_evento);

        editTextClassificacao = findViewById(R.id.classificacao);
        editTextData = findViewById(R.id.data);
        editTextDescricao = findViewById(R.id.descricao);
        editTextHora = findViewById(R.id.hora);
        editTextLocal = findViewById(R.id.local);
        editTextNome = findViewById(R.id.nome);
        editTextTaxa = findViewById(R.id.taxa);
        editTextValor = findViewById(R.id.valor);
        editTextTipo = findViewById(R.id.tipo);
        ingressos = (Switch) findViewById(R.id.ingressos);
        pulseiras = (Switch) findViewById(R.id.pulseiras);
        fichas = (Switch) findViewById(R.id.fichas);
        profilePic = findViewById(R.id.imageUpload);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference ref = db.collection("promotores").document(email);

            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    try {
                        valuePulseiras = (boolean) documentSnapshot.get("pulseiras");
                        valueIngressos = (boolean) documentSnapshot.get("ingressps");
                        valueFichas = (boolean) documentSnapshot.get("fichas");

                        if(!valuePulseiras){
                            pulseiras.setVisibility(View.GONE);
                        }
                        if(!valueIngressos){
                            ingressos.setVisibility(View.GONE);
                        }
                        if(!valueFichas){
                            fichas.setVisibility(View.GONE);
                        }
                    }catch(Exception error1) {
                        Log.e(TAG, "The exception caught while executing the process. (error1)");
                        error1.printStackTrace();
                    }
                }
            });
        }

        ingressos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    valueIngressos = true;
                } else {
                    valueIngressos = false;
                }
            }
        });

        pulseiras.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    valuePulseiras = true;
                } else {
                    valuePulseiras = false;
                }
            }
        });

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
        startActivity( new Intent( NovoEventoActivity.this, MenuActivity.class));
        finish();
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
        }
    }

    public void salvar(View view) {
        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/" + randomKey);
        bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            bmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);

        byte[] fileInBytes = baos.toByteArray();

        riversRef.putBytes(fileInBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String capa = uri.toString();
                        String classificacao = editTextClassificacao.getText().toString();
                        String data = editTextData.getText().toString();
                        String descricao = editTextDescricao.getText().toString();
                        String hora = editTextHora.getText().toString();
                        String local = editTextLocal.getText().toString();
                        String nome = editTextNome.getText().toString();
                        String status = "ativo";
                        String taxa = editTextTaxa.getText().toString();
                        String valor = editTextValor.getText().toString();
                        String tipo = editTextTipo.getText().toString();

                        if (nome.isEmpty()){
                            editTextNome.setError("Nome não pode estar vazio");
                        }else{
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            db.collection("promotores/"+email+"/eventos").add(new Evento(email, capa, classificacao, data, descricao, hora, local, nome, status, taxa, valor, tipo));
                            Snackbar.make(findViewById(android.R.id.content), "Evento criado com sucesso", Snackbar.LENGTH_SHORT).show();
                            startActivity( new Intent( NovoEventoActivity.this, MenuActivity.class));
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed To Upload", Toast.LENGTH_LONG).show();
            }
        });
    }
}
