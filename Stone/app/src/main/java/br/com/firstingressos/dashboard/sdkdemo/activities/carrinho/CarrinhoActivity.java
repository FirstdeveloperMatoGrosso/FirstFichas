package br.com.firstingressos.dashboard.sdkdemo.activities.carrinho;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import br.com.firstingressos.dashboard.sdkdemo.activities.ingressos.Evento;
import br.com.firstingressos.dashboard.R;
import br.com.firstingressos.dashboard.sdkdemo.activities.PosTransactionActivity;

import java.util.ArrayList;
import java.util.List;

import br.com.firstingressos.dashboard.sdkdemo.activities.ingressos.ProdutoAdapter;
import br.com.firstingressos.dashboard.sdkdemo.activities.menuPrintType.MenuPrintTypeActivity;

public class CarrinhoActivity extends AppCompatActivity {

    private ArrayList<Evento> itemList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();
    private List<String> valorList = new ArrayList<>();
    private List<String> taxaList = new ArrayList<>();
    private List<String> capaList = new ArrayList<>();
    private List<String> enderecoList = new ArrayList<>();
    private List<String> descricaoList = new ArrayList<>();
    private List<String> eventoList = new ArrayList<>();
    private List<String> classificacaoList = new ArrayList<>();
    private List<String> tipoList = new ArrayList<>();
    TextView amountEditText;
    ListView listView;
    ProdutoAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_carrinho);

        amountEditText = findViewById(R.id.textTotal);
        listView = findViewById(R.id.dynamic_list);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nameList = bundle.getStringArrayList("nameList");
            valorList = bundle.getStringArrayList("valorList");
            taxaList = bundle.getStringArrayList("taxaList");
            capaList = bundle.getStringArrayList("capaList");
            enderecoList = bundle.getStringArrayList("enderecoList");
            eventoList = bundle.getStringArrayList("eventoList");
            descricaoList = bundle.getStringArrayList("descricaoList");
            classificacaoList = bundle.getStringArrayList("classificacaoList");
            tipoList = bundle.getStringArrayList("tipoList");
            double total = 0.0;
            for(int j=0; j<valorList.size(); j++){
                total = total + Float.parseFloat(valorList.get(j).substring(3, valorList.get(j).length()).replace(".", "").replace(",", "."));
                if(!taxaList.get(j).contains("-")) {
                    total = total + Float.parseFloat(taxaList.get(j).substring(3, taxaList.get(j).length()).replace(".", "").replace(",", "."));
                }
                itemList.add(new Evento(capaList.get(j), nameList.get(j), enderecoList.get(j), valorList.get(j), taxaList.get(j), "", "", "", "", eventoList.get(j), descricaoList.get(j), classificacaoList.get(j), tipoList.get(j)));
            }
            arrayAdapter = new ProdutoAdapter(this, R.layout.carrinho_card_view, itemList);
            listView.setAdapter(arrayAdapter);
            amountEditText.setText("R$ " + String.valueOf(String.format("%.2f", total)));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    makeText(CarrinhoActivity.this, nameList.get(i) + " removido do carrinho", LENGTH_SHORT).show();
                    nameList.remove(i);
                    valorList.remove(i);
                    taxaList.remove(i);
                    capaList.remove(i);
                    itemList.remove(i);
                    enderecoList.remove(i);
                    descricaoList.remove(i);
                    eventoList.remove(i);
                    arrayAdapter = new ProdutoAdapter(CarrinhoActivity.this, R.layout.carrinho_card_view, itemList);
                    listView.setAdapter(arrayAdapter);

                    double total = 0.0;
                    for(int j=0; j<valorList.size(); j++){
                        total = total + Float.parseFloat(valorList.get(j).substring(3, valorList.get(j).length()).replace(".", "").replace(",", "."));
                        total = total + Float.parseFloat(taxaList.get(j).substring(3, taxaList.get(j).length()).replace(".", "").replace(",", "."));
                    }
                    amountEditText.setText("R$ " + String.valueOf(String.format("%.2f", total)));
                }
            });
        }



        Button fechamentbtn = findViewById(R.id.pagar);
        fechamentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(CarrinhoActivity.this);
                View view1 = LayoutInflater.from(CarrinhoActivity.this).inflate(R.layout.bottom_sheet_layout, null);

                bottomSheetDialog.setContentView(view1);
                                    bottomSheetDialog.show();
                                    Button dismissBtn = view1.findViewById(R.id.dismiss);
                                    Button ingressos = view1.findViewById(R.id.ingressos);
                                    Button fichas = view1.findViewById(R.id.fichas);

                                    dismissBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            bottomSheetDialog.dismiss();
                                        }
                                    });

                                    bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {

                                        }
                                    });
            }
        });
    }

    public void voltar(View view){
        Intent intent = new Intent(CarrinhoActivity.this, MenuPrintTypeActivity.class);
        startActivity(intent);
        finish();
    }
}
