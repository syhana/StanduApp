package com.example.standu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button_daftar, button_masuk;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SessionManager.isUserLoggedIn(this)){
            masukKeHome();
        }else{
            button_daftar = findViewById(R.id.button_daftar);
            button_masuk = findViewById(R.id.button_masuk);

            button_daftar.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, Daftar.class) ));
            button_masuk.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, Masuk.class)));
        }
    }
    private void masukKeHome(){
        startActivity(new Intent(MainActivity.this, Home.class));
        finish();
    }
}