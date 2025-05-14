package com.gussanxz.orgafacil.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gussanxz.orgafacil.R;

import java.util.Arrays;
import java.util.List;

public class SelecionarCategoriaActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> categorias = Arrays.asList(
            "Alimentação", "Aluguel", "Pets", "Contas", "Doações e caridades",
            "Educação", "Investimento", "Lazer", "Mercado", "Moradia"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_selecionar_categoria);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.listViewCategorias);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categorias);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            String categoriaSelecionada = categorias.get(position);
            Intent resultado = new Intent();
            resultado.putExtra("categoriaSelecionada", categoriaSelecionada);
            setResult(RESULT_OK, resultado);
            finish();
        });

        findViewById(R.id.btnNovaCategoria).setOnClickListener(v -> {
            // Pode abrir um AlertDialog para adicionar nova categoria e salvar no Firebase
        });

    }
}