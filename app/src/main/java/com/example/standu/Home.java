package com.example.standu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class Home extends AppCompatActivity {

    private ImageButton menu1_diary;

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

        menu1_diary.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Diaryku.class);
            startActivity(intent);
        });
    }
}