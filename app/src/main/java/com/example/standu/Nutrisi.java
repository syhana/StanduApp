package com.example.standu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class Nutrisi extends AppCompatActivity {
    RecyclerView recyclerView;
    Nutrisi_adapter nutrisiAdapter;
    ImageButton button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SessionManager.isUserLoggedIn(this)){
            setContentView(R.layout.activity_nutrisi);
        }else{
            startActivity(new Intent(Nutrisi.this, MainActivity.class));
            finish();
        }

        button_back = findViewById(R.id.button_back);
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Nutrisi_model> options =
                new FirebaseRecyclerOptions.Builder<Nutrisi_model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("nutrisi"), Nutrisi_model.class)
                        .build();

        nutrisiAdapter = new Nutrisi_adapter(options, this);
        recyclerView.setAdapter(nutrisiAdapter);

        button_back.setOnClickListener(view -> startActivity(new Intent(Nutrisi.this, Home.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        nutrisiAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        nutrisiAdapter.stopListening();
    }
}

