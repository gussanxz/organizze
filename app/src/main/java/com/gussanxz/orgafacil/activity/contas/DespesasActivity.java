package com.gussanxz.orgafacil.activity.contas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.gussanxz.orgafacil.R;
import com.gussanxz.orgafacil.activity.SelecionarCategoriaActivity;
import com.gussanxz.orgafacil.config.ConfiguracaoFirebase;
import com.gussanxz.orgafacil.helper.Base64Custom;
import com.gussanxz.orgafacil.model.Movimentacao;
import com.gussanxz.orgafacil.model.Usuario;
import com.gussanxz.orgafacil.model.DatePickerHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoDescricao;
    private EditText campoValor, campoCategoria;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double despesaTotal;
    private ActivityResultLauncher<Intent> launcherCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_despesas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.textCategoria);
        campoDescricao = findViewById(R.id.editDescricao);

        campoData.setText(DatePickerHelper.setDataAtual());

        campoData.setFocusable(false);
        campoData.setClickable(true);

        campoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerHelper.showDatePickerDialog(DespesasActivity.this, campoData);
            }
        });

        launcherCategoria = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        String categoria = result.getData().getStringExtra("categoriaSelecionada");
                        campoCategoria.setText(categoria);
                    }
                });

        campoCategoria = findViewById(R.id.textCategoria);
        campoCategoria.setOnClickListener(v -> {
            Intent intent = new Intent(DespesasActivity.this, SelecionarCategoriaActivity.class);
            launcherCategoria.launch(intent);
        });

        recuperarDespesaTotal();

    }

    public void retornarPrincipal(View view){
        startActivity(new Intent(this, PrincipalActivity.class));
    }

    public void salvarDespesa(View view) {

        if (validarCamposDespesas()) {

            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

            movimentacao.setValor( valorRecuperado );
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("d");

            Double despesaAtualizada = despesaTotal + valorRecuperado;
            atualizarDespesa( despesaAtualizada );

            movimentacao.salvar(data);
            Toast.makeText(this, "Despesa adicionada!", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    public Boolean validarCamposDespesas() {

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if ( !textoValor.isEmpty()) {
            if ( !textoData.isEmpty()) {
                if ( !textoCategoria.isEmpty()) {
                    if ( !textoDescricao.isEmpty()) {

                        return true;

                    }else {
                        Toast.makeText(DespesasActivity.this,
                                "Descrição não foi preenchida!", Toast.LENGTH_SHORT).show();
                        return false;

                    }
                }else {
                    Toast.makeText(DespesasActivity.this,
                            "Categoria não foi preenchida!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(DespesasActivity.this,
                        "Data não foi preenchida!", Toast.LENGTH_SHORT).show();
                return false;

            }

        }else {
            Toast.makeText(DespesasActivity.this,
                    "Valor não foi preenchido!", Toast.LENGTH_SHORT).show();
            return false;

        }

    }

    public void recuperarDespesaTotal() {

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child( idUsuario );

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue( Usuario.class );
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void atualizarDespesa(Double despesa) {

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesa);

    }

}