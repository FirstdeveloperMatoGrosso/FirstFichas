package br.com.firstingressos.dashboard.sdkdemo.activities.estacionamento;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.menu.MenuActivity;
import br.com.stone.posandroid.providers.PosPrintProvider;
import stone.application.interfaces.StoneCallbackInterface;

public class EstacionamentoActivity extends AppCompatActivity {
    private EditText editTextValor, editTextTaxa, editTextNome, editTextTelefone, editTextCPF, editTextEndereco, editTextPlaca, editTextRenavam, editTextModelo, editTextDefeitos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_estacionamento);

        editTextNome = findViewById(R.id.nome);
        editTextTelefone = findViewById(R.id.telefone);
        editTextCPF = findViewById(R.id.cpf);
        editTextEndereco = findViewById(R.id.endereco);
        editTextPlaca = findViewById(R.id.placa);
        editTextRenavam = findViewById(R.id.renavam);
        editTextModelo = findViewById(R.id.modelo);
        editTextDefeitos = findViewById(R.id.defeitos);
        editTextValor = findViewById(R.id.valor);
        editTextTaxa = findViewById(R.id.taxa);
    }

    public void imprimir(View view) throws IOException {

        PosPrintProvider customPosPrintProvider = new PosPrintProvider(EstacionamentoActivity.this);

        customPosPrintProvider.addLine("Nome: " + editTextNome.getText().toString());
        customPosPrintProvider.addLine("CPF: " + editTextCPF.getText().toString());
        customPosPrintProvider.addLine("Telefone: " + editTextTelefone.getText().toString());
        customPosPrintProvider.addLine("Endereço: " + editTextEndereco.getText().toString());
        customPosPrintProvider.addLine("Placa: " + editTextPlaca.getText().toString());
        customPosPrintProvider.addLine("Renavam: " + editTextRenavam.getText().toString());
        customPosPrintProvider.addLine("Modelo: " + editTextModelo.getText().toString());
        customPosPrintProvider.addLine("Descrição de defeitos: " + editTextDefeitos.getText().toString());

        customPosPrintProvider.addLine("\n---------------------\n\n");

        String dataFormatada = null;
        String horaFormatada = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate dataAtual = LocalDate.now();
            LocalTime horaAtual = LocalTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dataFormatada = dataAtual.format(formato);
            formato = DateTimeFormatter.ofPattern("HH:mm:ss");
            horaFormatada = horaAtual.format(formato);
        }

        customPosPrintProvider.addLine("Data: " + dataFormatada);
        customPosPrintProvider.addLine("Horário: " + horaFormatada);
        customPosPrintProvider.addLine("\n\n\n----------------------------------\nAssinatura");

        customPosPrintProvider.addLine("\n---------------------\n\n");

        customPosPrintProvider.addLine("Valor: " + editTextValor.getText().toString());
        customPosPrintProvider.addLine("Taxa: " + editTextTaxa.getText().toString());

        customPosPrintProvider.setConnectionCallback(new StoneCallbackInterface() {
            @Override
            public void onSuccess() {
                Toast.makeText(EstacionamentoActivity.this, "Ficha de estacionamento impressa", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(EstacionamentoActivity.this, "Erro ao imprimir: " + customPosPrintProvider.getListOfErrors(), Toast.LENGTH_SHORT).show();
            }
        });
        customPosPrintProvider.execute();
    }

    public void voltar(View view){
        startActivity( new Intent( this, MenuActivity.class));
        finish();
    }
}

