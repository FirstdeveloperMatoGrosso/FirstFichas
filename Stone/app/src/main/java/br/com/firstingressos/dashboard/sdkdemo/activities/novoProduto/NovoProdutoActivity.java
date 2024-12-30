package br.com.firstingressos.dashboard.sdkdemo.activities.novoProduto;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.menu.MenuActivity;
import br.com.firstingressos.dashboard.sdkdemo.activities.menuProduto.MenuProdutoActivity;

public class NovoProdutoActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText editTextDescricao, editTextNome, editTextValor, editTextEvento;
    private Spinner spinner;
    private TextView txt1, txt2;
    String email = "";
    private ImageView profilePic, icon;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    String categoriaSpinner;
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
        setContentView(R.layout.activity_novo_produto);

        editTextDescricao = findViewById(R.id.descricao);
        editTextNome = findViewById(R.id.nome);
        editTextEvento = findViewById(R.id.evento);
        editTextValor = findViewById(R.id.valor);
        profilePic = findViewById(R.id.imageUpload);
        spinner = findViewById(R.id.spinner);
        txt1 = findViewById(R.id.textView12);
        txt2 = findViewById(R.id.textView6);
        icon = findViewById(R.id.imageView6);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();

            if (user != null) {
                email = user.getEmail();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("promotores/"+email+"/categorias/").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                final List<String> datas = new ArrayList<String>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    datas.add(document.get("nome").toString());
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(NovoProdutoActivity.this, com.library.R.layout.support_simple_spinner_dropdown_item, datas);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);

                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        categoriaSpinner = datas.get(position);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        categoriaSpinner = "Não selecionado";
                                    }
                                });
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    });

            }
        }
        mAuth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void voltar(View view){
        startActivity( new Intent( this, MenuProdutoActivity.class));
        finish();
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
        txt1.setVisibility(View.GONE);
        txt2.setVisibility(View.GONE);
        icon.setVisibility(View.GONE);
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
                        String descricao = editTextDescricao.getText().toString();
                        String categoria = categoriaSpinner;
                        String nome = editTextNome.getText().toString();
                        String valor = editTextValor.getText().toString();
                        String evento = editTextEvento.getText().toString();

                        if (nome.isEmpty()) {
                            editTextNome.setError("Nome não pode estar vazio");
                        } else {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            db.collection("promotores/" + email + "/produtos").add(new Produto(categoria, capa, descricao, nome, valor, evento));
                            startActivity(new Intent(NovoProdutoActivity.this, MenuActivity.class));
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
