package com.example.standu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Sharing_detail extends AppCompatActivity {
    private TextView judulLihat, ceritaLihat;
    private ImageView imageLihat;
    private ImageButton button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_detail);

        judulLihat = findViewById(R.id.judulSharing);
        ceritaLihat = findViewById(R.id.ceritaSharing);
        imageLihat = findViewById(R.id.imageSharing);
        button_back = findViewById(R.id.button_back);

        String sharingId = getIntent().getStringExtra("sharingId");
        String judul = getIntent().getStringExtra("judul");
        String cerita = getIntent().getStringExtra("cerita");
        String image = getIntent().getStringExtra("image");

        button_back.setOnClickListener(view -> startActivity(new Intent(Sharing_detail.this, Sharing.class)));

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            judulLihat.setText(bundle.getString("judul"));
            ceritaLihat.setText(bundle.getString("cerita"));
            Glide.with(this).load(bundle.getString("image")).into(imageLihat);
        }

    }
}