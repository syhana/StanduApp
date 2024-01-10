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

public class Sharing extends AppCompatActivity {

    private Button button_buka;
    private ImageButton button_back;
    private DatabaseReference sharingRef;
    private RecyclerView recyclerView;
    private Sharingku_adapter adapter;
    private List<Sharingku_model> sharingList;
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

        String userId = SessionManager.getUserDetails(this).userId;
        sharingRef = FirebaseDatabase.getInstance().getReference("sharing");

        recyclerView = findViewById(R.id.recyclerview_sharing);
        sharingList = new ArrayList<>();

        sharingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sharingList.clear();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot shareSnapshot : userSnapshot.getChildren()) {
                        Sharingku_model share = shareSnapshot.getValue(Sharingku_model.class);
                        if (share != null) {
                            sharingList.add(share);
                        }
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