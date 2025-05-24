package com.gussanxz.orgafacil.activity.contas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gussanxz.orgafacil.R;
import com.gussanxz.orgafacil.config.ConfiguracaoFirebase;
import com.gussanxz.orgafacil.helper.Base64Custom;
import com.gussanxz.orgafacil.model.DatePickerHelper;
import com.gussanxz.orgafacil.model.Movimentacao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.gussanxz.orgafacil.activity.contas.PrincipalActivity;

import java.util.Calendar;

public class EditarMovimentacaoActivity extends AppCompatActivity {

    private EditText editData, editHora, editDescricao, editValor, editCategoria;
    private Movimentacao movimentacao;
    private boolean isModoEdicao = false;
    private String keyFirebase;
    private final DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private final FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private double valorAnterior;
    private String tipoAnterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isModoEdicao = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        editData = findViewById(R.id.editData);
        editHora = findViewById(R.id.editHora);
        editDescricao = findViewById(R.id.editDescricao);
        editValor = findViewById(R.id.editValor);
        editCategoria = findViewById(R.id.editCategoria);

        movimentacao = (Movimentacao) getIntent().getSerializableExtra("movimentacaoSelecionada");
        keyFirebase = getIntent().getStringExtra("keyFirebase");

        if (movimentacao != null) {
            valorAnterior = movimentacao.getValor();
            tipoAnterior = movimentacao.getTipo();

            // Preenche os campos
            editData.setText(movimentacao.getData());
            editHora.setText(movimentacao.getHora());
            editDescricao.setText(movimentacao.getDescricao());
            editValor.setText(String.valueOf(valorAnterior));
            editCategoria.setText(movimentacao.getCategoria());

            editData.setOnClickListener(v -> abrirDataPicker());
            editHora.setOnClickListener(v -> abrirTimePicker());

        }
    }

    private void abrirDataPicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String dataSelecionada = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    editData.setText(dataSelecionada);
                },
                year, month, day
        );
        datePicker.show();
    }

    private void abrirTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String horaSelecionada = String.format("%02d:%02d", hourOfDay, minute1);
            editHora.setText(horaSelecionada);
        }, hora, minuto, true).show();
    }

    public void salvarDespesa(View view) {
        if (isModoEdicao) {
            atualizarMovimentacaoExistente();
        } else {
            salvarNovaMovimentacao(); // opcional, se quiser aproveitar a mesma activity
        }
    }

    private void atualizarMovimentacaoExistente() {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        String mesAno = DatePickerHelper.mesAnoDataEscolhida(movimentacao.getData());
        ; // dd/MM/yyyy

        DatabaseReference ref = firebaseRef
                .child("movimentacao")
                .child(idUsuario)
                .child(mesAno)
                .child(keyFirebase);

        movimentacao.setData(editData.getText().toString());
        movimentacao.setHora(editHora.getText().toString());
        movimentacao.setDescricao(editDescricao.getText().toString());
        movimentacao.setCategoria(editCategoria.getText().toString());
        movimentacao.setValor(Double.parseDouble(editValor.getText().toString()));

        ref.setValue(movimentacao).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ajustarTotais(); // ✅ Corrige proventos/despesas
                setResult(RESULT_OK);
                finish();
            }
        });


        Toast.makeText(this, "Movimentação atualizada", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void salvarNovaMovimentacao() {
        // opcional: se quiser permitir inserções nesta mesma tela
        Toast.makeText(this, "Função de nova movimentação não implementada aqui.", Toast.LENGTH_SHORT).show();
    }

    private void ajustarTotais() {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                double proventos = snapshot.child("proventosTotal").getValue(Double.class);
                double despesas = snapshot.child("despesaTotal").getValue(Double.class);

                // Subtrai o valor anterior
                if (tipoAnterior.equals("r")) {
                    proventos -= valorAnterior;
                } else if (tipoAnterior.equals("d")) {
                    despesas -= valorAnterior;
                }

                // Adiciona o novo valor
                if (movimentacao.getTipo().equals("r")) {
                    proventos += movimentacao.getValor();
                } else if (movimentacao.getTipo().equals("d")) {
                    despesas += movimentacao.getValor();
                }

                // Atualiza no Firebase
                usuarioRef.child("proventosTotal").setValue(proventos);
                usuarioRef.child("despesaTotal").setValue(despesas);
            }
        });
    }

}

