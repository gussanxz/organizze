package com.gussanxz.organizze.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;

import com.gussanxz.organizze.R;

public class ResumoVendasActivity extends AppCompatActivity {
    private final String TAG = "ResumoVendasActivity";
    private TextView textValorPendente, textVendasPendentes, textVendasPagas,
            textDataVenda, textValorVenda, textValorPendenteRestante, textDataPagamento, textValorPagamento;

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
        textDataPagamento = findViewById(R.id.textDataPagamento);
        textValorPagamento = findViewById(R.id.textValorPagamento);

        // Simula dados de teste para visualização
        textValorPendente.setText("50,00");
        textVendasPendentes.setText("1");
        textVendasPagas.setText("0");
        textDataVenda.setText("sábado, 08 fevereiro 2025");
        textValorVenda.setText("R$ 100,00");
        textValorPendenteRestante.setText("R$ 50,00");
        textDataPagamento.setText("08/02/2025");
        textValorPagamento.setText("R$ 50,00");
    }
}