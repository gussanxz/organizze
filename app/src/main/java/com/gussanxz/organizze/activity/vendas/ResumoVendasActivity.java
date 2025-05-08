package com.gussanxz.organizze.activity.vendas;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gussanxz.organizze.R;

public class ResumoVendasActivity extends AppCompatActivity {
    private final String TAG = "ResumoVendasActivity";
    private TextView textValorPendente, textVendasPendentes, textVendasPagas,
            textDataVenda, textValorVenda, textValorPendenteRestante, textDataPagamento, textValorPagamento,
            textDescricaoVendaPaga, textValorTotalVendaPaga;
    private Button btnVerVendas, btnVerPagamentos;
    private FloatingActionMenu floatingActionMenu, menuCadastroCliente;
    private FloatingActionButton menuNovaVenda;

    private double valorPendenteTotalExemplo = 0.0;
    private int qtdVendasPendentesExemplo = 0;
    private int qtdVendasPagasExemplo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resumo_vendas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textValorPendente = findViewById(R.id.textValorPendente);
        textVendasPendentes = findViewById(R.id.textVendasPendentes);
        textVendasPagas = findViewById(R.id.textVendasPagas);
        textDataVenda = findViewById(R.id.textDataVenda);
        textValorVenda = findViewById(R.id.textValorVenda);
        textValorPendenteRestante = findViewById(R.id.textValorPendenteRestante);
        btnVerVendas = findViewById(R.id.btnVerVendas);
        textDataPagamento = findViewById(R.id.textDataPagamento);
        textDescricaoVendaPaga = findViewById(R.id.textDescricaoVendaPaga);
        textValorTotalVendaPaga = findViewById(R.id.textValorTotalVendaPaga);
        textValorPagamento = findViewById(R.id.textValorPagamento);
        btnVerPagamentos = findViewById(R.id.btnVerPagamentos);

        floatingActionMenu = findViewById(R.id.floatingActionMenu);

        // Simula dados de teste para visualização
        textValorPendente.setText("50,00");
        textVendasPendentes.setText("1");
        textVendasPagas.setText("0");
        textDataVenda.setText("sábado, 08 fevereiro 2025");
        textValorVenda.setText("R$ 100,00");
        textValorPendenteRestante.setText("R$ 50,00");
        textDataPagamento.setText("08/02/2025");
        textValorPagamento.setText("R$ 50,00");

        configurarListeners();
    }

    private void configurarListeners() {
        if (btnVerVendas != null) {
            btnVerVendas.setOnClickListener(v -> {
                 Intent intent = new Intent(ResumoVendasActivity.this, ListaVendasActivity.class);
                // intent.putExtra("filtro_status", "PENDENTE");
                 startActivity(intent);
                Log.i(TAG, "Botão Ver Vendas clicado");
            });
        }

        if (btnVerPagamentos != null) {
            btnVerPagamentos.setOnClickListener(v -> {
                 Intent intent = new Intent(ResumoVendasActivity.this, ListaPagamentosActivity.class);
                 startActivity(intent);
                Log.i(TAG,"Botão Ver Pagamentos clicado");
            });
        }
    }

    public void cadastroCliente(View view){
        floatingActionMenu.close(true);
        startActivity(new Intent(this, CadastroClienteActivity.class));
    }

    public void novaVenda(View view){
        floatingActionMenu.close(true);
        startActivity(new Intent(this, VendasActivity.class));
    }

}