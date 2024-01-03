package com.example.standu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Diaryku extends AppCompatActivity {

    private Button button_tambah;
    private ImageButton button_back;
    private RecyclerView recyclerView;
    private Diaryku_adapter adapter;
    private List<Diaryku_model> diaryList;
    private DatabaseReference diaryRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SessionManager.isUserLoggedIn(this)){
            setContentView(R.layout.activity_diaryku);
        }else{
            startActivity(new Intent(Diaryku.this, MainActivity.class));
            finish();
        }

        button_back = findViewById(R.id.button_back_green);
        button_tambah = findViewById(R.id.diaryku_button_tambah);

        button_back.setOnClickListener(v -> {
            Intent intent = new Intent(Diaryku.this, Home.class);
            startActivity(intent);
        });
        button_tambah.setOnClickListener(v -> {
            Intent intent = new Intent(Diaryku.this, Diaryku_tambah.class);
            startActivity(intent);
        });

        String uid = SessionManager.getUserDetails(this).uid;
        diaryRef = FirebaseDatabase.getInstance().getReference("diaries").child(uid);

        recyclerView = findViewById(R.id.recycler_view_diary);
        diaryList = new ArrayList<>();

        // Mengambil data dari Firebase Realtime Database
        diaryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                diaryList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Diaryku_model diary = snapshot.getValue(Diaryku_model.class);
                    diaryList.add(diary);
                }


                Collections.reverse(diaryList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });

        adapter = new Diaryku_adapter(diaryList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }
}