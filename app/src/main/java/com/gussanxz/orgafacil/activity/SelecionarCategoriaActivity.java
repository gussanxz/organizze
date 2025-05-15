package com.gussanxz.orgafacil.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gussanxz.orgafacil.R;
import com.gussanxz.orgafacil.helper.Base64Custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelecionarCategoriaActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> categorias = new ArrayList<>();
    private ArrayAdapter<String> adapter;

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

        inicializarCategoriasPadrao();
        carregarCategoriasDoFirebase();

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            String categoriaSelecionada = categorias.get(position);
            Intent resultado = new Intent();
            resultado.putExtra("categoriaSelecionada", categoriaSelecionada);
            setResult(Activity.RESULT_OK, resultado);
            finish();
        });

        findViewById(R.id.btnNovaCategoria).setOnClickListener(v -> mostrarDialogNovaCategoria());
    }

    private void inicializarCategoriasPadrao() {
        categorias.addAll(Arrays.asList(
                "Alimentação", "Aluguel", "Pets", "Contas", "Doações e caridades",
                "Educação", "Investimento", "Lazer", "Mercado", "Moradia"
        ));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categorias);
        listView.setAdapter(adapter);
    }

    private void mostrarDialogNovaCategoria() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nova Categoria");

        final EditText input = new EditText(this);
        input.setHint("Digite o nome da categoria");
        builder.setView(input);

        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String novaCategoria = input.getText().toString().trim();
            if (!TextUtils.isEmpty(novaCategoria)) {
                salvarCategoriaNoFirebase(novaCategoria);
            } else {
                Toast.makeText(this, "Digite uma categoria válida", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void salvarCategoriaNoFirebase(String nomeCategoria) {
        String emailUsuario = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference categoriasRef = ref.child("usuarios").child(idUsuario).child("categorias");

        String novaChave = categoriasRef.push().getKey();
        categoriasRef.child(novaChave).setValue(nomeCategoria).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Categoria adicionada!", Toast.LENGTH_SHORT).show();
                atualizarListaCategorias(nomeCategoria);
            } else {
                Toast.makeText(this, "Erro ao salvar categoria", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void atualizarListaCategorias(String novaCategoria) {
        categorias.add(novaCategoria);
        adapter.notifyDataSetChanged();
    }

    private void carregarCategoriasDoFirebase() {
        String emailUsuario = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference categoriasRef = ref.child("usuarios").child(idUsuario).child("categorias");

        categoriasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@Nullable DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot catSnap : snapshot.getChildren()) {
                        String categoria = catSnap.getValue(String.class);
                        if (categoria != null && !categorias.contains(categoria)) {
                            categorias.add(categoria);
                        }
                    }
                    categorias.sort(String::compareToIgnoreCase);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@Nullable DatabaseError error) {}
        });
    }
}
