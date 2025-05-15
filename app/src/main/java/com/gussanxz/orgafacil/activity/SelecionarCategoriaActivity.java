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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gussanxz.orgafacil.R;
import com.gussanxz.orgafacil.adapter.AdapterCategoria;
import com.gussanxz.orgafacil.helper.Base64Custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class SelecionarCategoriaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterCategoria adapter;
    private List<String> categorias = new ArrayList<>();

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

        recyclerView = findViewById(R.id.recyclerCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdapterCategoria(categorias, categoria -> {
            Intent resultado = new Intent();
            resultado.putExtra("categoriaSelecionada", categoria);
            setResult(Activity.RESULT_OK, resultado);
            finish();
        });
        recyclerView.setAdapter(adapter);


        inicializarRecyclerView();
        carregarCategoriasDoFirebase();
        configurarSwipeToDelete();


        findViewById(R.id.btnNovaCategoria).setOnClickListener(v -> mostrarDialogNovaCategoria());
    }

    private void inicializarCategoriasPadrao() {
        categorias.addAll(Arrays.asList(
                "Alimentação", "Aluguel", "Pets", "Contas", "Doações e caridades",
                "Educação", "Investimento", "Lazer", "Mercado", "Moradia"
        ));
        categorias.sort(String::compareToIgnoreCase);
        adapter.notifyDataSetChanged();
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
                categorias.add(nomeCategoria);
                categorias.sort(String::compareToIgnoreCase);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void carregarCategoriasDoFirebase() {
        String emailUsuario = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference categoriasRef = ref.child("usuarios").child(idUsuario).child("categorias");

        categoriasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@Nullable DataSnapshot snapshot) {
                categorias.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot catSnap : snapshot.getChildren()) {
                        String categoria = catSnap.getValue(String.class);
                        if (categoria != null && !categorias.contains(categoria)) {
                            categorias.add(categoria);
                        }
                    }
                } else {
                    categorias.addAll(Arrays.asList(
                            "Alimentação", "Aluguel", "Pets", "Contas", "Doações e caridades",
                            "Educação", "Investimento", "Lazer", "Mercado", "Moradia"
                    ));
                    for (String padrao : categorias) {
                        categoriasRef.push().setValue(padrao);
                    }
                }
                categorias.sort(String::compareToIgnoreCase);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@Nullable DatabaseError error) {}
        });
    }

    private void configurarSwipeToDelete() {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                String categoria = adapter.getItem(position);

                verificarSeCategoriaPodeSerRemovida(categoria, podeRemover -> {
                    if (podeRemover) {
                        removerCategoriaDoFirebase(categoria);
                        adapter.removeItem(position);
                        Toast.makeText(SelecionarCategoriaActivity.this, "Categoria excluída", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SelecionarCategoriaActivity.this, "Categoria em uso e não pode ser removida", Toast.LENGTH_LONG).show();
                        adapter.notifyItemChanged(position);
                    }
                });
            }
        };

        new ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView);
    }

    private void verificarSeCategoriaPodeSerRemovida(String categoria, Consumer<Boolean> callback) {
        String idUsuario = Base64Custom.codificarBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("movimentacao").child(idUsuario);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mes : snapshot.getChildren()) {
                    for (DataSnapshot mov : mes.getChildren()) {
                        String cat = mov.child("categoria").getValue(String.class);
                        if (categoria.equalsIgnoreCase(cat)) {
                            callback.accept(false);
                            return;
                        }
                    }
                }
                callback.accept(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.accept(false);
            }
        });
    }

    private void removerCategoriaDoFirebase(String categoria) {
        String idUsuario = Base64Custom.codificarBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        DatabaseReference categoriasRef = FirebaseDatabase.getInstance().getReference()
                .child("usuarios").child(idUsuario).child("categorias");

        categoriasRef.orderByValue().equalTo(categoria).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot catSnap : snapshot.getChildren()) {
                    catSnap.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void inicializarRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterCategoria(categorias, categoria -> {
            Intent resultado = new Intent();
            resultado.putExtra("categoriaSelecionada", categoria);
            setResult(Activity.RESULT_OK, resultado);
            finish();
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarCategoriasDoFirebase();
    }

}
