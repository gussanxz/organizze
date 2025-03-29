package com.gussanxz.organizze.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.gussanxz.organizze.R;
import com.gussanxz.organizze.helper.DateCustom;
import com.gussanxz.organizze.model.Movimentacao;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_despesas);

        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editValor);

        //Prenche o campo data com a data atual
        campoData.setText( DateCustom.dataAtual() );
    }

    public void salvarDespesa(View view) {

    String data = campoData.getText().toString();
    movimentacao = new Movimentacao();
    movimentacao.setValor( Double.parseDouble(campoValor.getText().toString()) );
    movimentacao.setCategoria( campoCategoria.getText().toString() );
    movimentacao.setDescricao( campoDescricao.getText().toString() );
    movimentacao.setData( data );
    movimentacao.setTipo( "d" );

    movimentacao.salvar( data );

    }
}