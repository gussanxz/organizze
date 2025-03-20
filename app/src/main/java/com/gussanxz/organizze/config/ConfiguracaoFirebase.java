package com.gussanxz.organizze.config;

import com.google.firebase.auth.FirebaseAuth;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;

    //retornar a instancia do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao() {
        if( autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

}
