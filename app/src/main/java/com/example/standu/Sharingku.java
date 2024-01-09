package com.example.standu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Sharingku extends AppCompatActivity {

    private ImageButton button_back;
    private Button button_tambah;
    private RecyclerView recyclerView;
    private List<Sharingku_model> sharingList;
    private Sharingku_adapter adapter;
    DatabaseReference sharingRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SessionManager.isUserLoggedIn(this)){
            setContentView(R.layout.activity_sharingku);
        }else{
            startActivity(new Intent(Sharingku.this, MainActivity.class));
            finish();
        }

        button_back = findViewById(R.id.button_back_purple);
        button_tambah = findViewById(R.id.sharingku_button_tambah);

        button_back.setOnClickListener( view -> startActivity(new Intent(Sharingku.this, Sharing.class)));
        button_tambah.setOnClickListener(view -> {
            startActivity(new Intent(Sharingku.this, Sharingku_tambah.class));
        });

        String userId = SessionManager.getUserDetails(this).userId;
        sharingRef = FirebaseDatabase.getInstance().getReference("sharing").child(userId);

        recyclerView = findViewById(R.id.recyclerview);
        sharingList = new ArrayList<>();

        sharingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sharingList.clear();

                for (DataSnapshot shareSnapshot : snapshot.getChildren()) {
                    Sharingku_model share = shareSnapshot.getValue(Sharingku_model.class);
                    if(share != null){
                        sharingList.add(share);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new Sharingku_adapter(sharingList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}