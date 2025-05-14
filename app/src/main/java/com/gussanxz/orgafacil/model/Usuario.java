package com.gussanxz.orgafacil.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;
import com.gussanxz.orgafacil.config.ConfiguracaoFirebase;

import java.util.HashMap;

public class Usuario {

    private String idUsuario;
    private String nome;
    private String email;
    private String senha;
    private Double proventosTotal = 0.00;
    private Double despesaTotal = 0.00;

    public Usuario() {
    }

    public void salvar() {
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuarioRef = firebase.child("usuarios").child(getIdUsuario());

        usuarioRef.setValue(this);

        usuarioRef.child("categorias").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // Cria um mapa vazio (pode começar sem categorias)
                    usuarioRef.child("categorias").setValue(new HashMap<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Erro ao verificar categorias", error.toException());
            }
        });
    }

    public Double getProventosTotal() {
        return proventosTotal;
    }

    public void setProventosTotal(Double proventosTotal) {
        this.proventosTotal = proventosTotal;
    }

    public Double getDespesaTotal() {
        return despesaTotal;
    }

    public void setDespesaTotal(Double despesaTotal) {
        this.despesaTotal = despesaTotal;
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
