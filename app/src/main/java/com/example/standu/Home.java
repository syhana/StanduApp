package com.example.standu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class Home extends AppCompatActivity {

    private ImageButton menu1_diary, menu2_informasi, menu3_nutrisi, menu4_imunisasi, menu5_pengaturan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SessionManager.isUserLoggedIn(this)){
            setContentView(R.layout.activity_home);
        }else{
            startActivity(new Intent(Home.this, MainActivity.class));
            finish();
        }


        menu1_diary = findViewById(R.id.menu1);
        menu4_imunisasi = findViewById(R.id.menu4);
        menu5_pengaturan = findViewById(R.id.menu5);

        menu1_diary.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Diaryku.class);
            startActivity(intent);
        });

        menu4_imunisasi.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Imunisasi.class);
            startActivity(intent);
        });

        menu5_pengaturan.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Pengaturan.class);
            startActivity(intent);
        });
    }
}