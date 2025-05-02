package com.gussanxz.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gussanxz.organizze.R;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private TextView textoContas, textoVendas, textoMercado, textoAtividades, textoConfigs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textoContas = findViewById(R.id.textViewContas);
        textoVendas = findViewById(R.id.textViewVendas);
        textoMercado = findViewById(R.id.textViewMercado);
        textoAtividades = findViewById(R.id.textViewAtividades);
        textoConfigs = findViewById(R.id.textViewConfigs);

    }

    public void acessarPrincipalActivity(View view) {
        startActivity(new Intent(this, PrincipalActivity.class));
        Log.i(TAG, "acessou PrincipalActivity");
    }

    public void acessarResumoVendasAcitivity(View view) {
        startActivity(new Intent(this, ResumoVendasActivity.class));
        Log.i(TAG, "acessou ResumoVendasActivity");
    }

    public void acessarResumoListaMercado(View view) {
        startActivity(new Intent(this, ResumoListaMercadoActivity.class));
        Log.i(TAG, "acessou ResumoListaMercadoActivity");
    }

    public void acessarListaAtividades(View view) {
        startActivity(new Intent(this, ListaAtividadesActivity.class));
        Log.i(TAG, "acessou ListaAtividadesActivity");
    }

    public void acessarBoletos(View view) {
        startActivity(new Intent(this, BoletosActivity.class));
        Log.i(TAG, "acessou BoletosActivity");
    }
}