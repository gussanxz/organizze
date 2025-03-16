package com.gussanxz.organizze;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.gussanxz.organizze.activity.LoginActivity;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;

public class MainActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}