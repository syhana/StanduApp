package com.example.standu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class Sharing extends AppCompatActivity {

    private Button button_buka;
    private ImageButton button_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SessionManager.isUserLoggedIn(this)){
            setContentView(R.layout.activity_sharing);
        }else{
            startActivity(new Intent(Sharing.this, MainActivity.class));
            finish();
        }

        button_buka = findViewById(R.id.sharing_button_buka);
        button_back = findViewById(R.id.button_back_purple);

        button_buka.setOnClickListener(view -> {
            startActivity(new Intent(Sharing.this, Sharingku.class));
        });
        button_back.setOnClickListener(view -> startActivity(new Intent(Sharing.this, Home.class)));
    }
}