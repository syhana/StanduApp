package com.example.standu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
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

        button_back.setOnClickListener(view -> startActivity(new Intent(Diaryku.this, Home.class)));
        button_tambah.setOnClickListener(view -> {
            startActivity(new Intent(Diaryku.this, Diaryku_tambah.class));
        });

        String userId = SessionManager.getUserDetails(this).userId;
        diaryRef = FirebaseDatabase.getInstance().getReference("diaries").child(userId);

        recyclerView = findViewById(R.id.recycler_view_diary);
        diaryList = new ArrayList<>();

        diaryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                diaryList.clear();

                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot diarySnapshot : dateSnapshot.getChildren()) {
                        Diaryku_model diary = diarySnapshot.getValue(Diaryku_model.class);
                        diaryList.add(diary);
                    }
                }

                Collections.reverse(diaryList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        adapter = new Diaryku_adapter(diaryList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Diaryku_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                openDiaryDetail(diaryList.get(position).getDiaryId());
            }
        });


    }

    private void openDiaryDetail(String diaryId) {
        DatabaseReference diaryDetailRef = diaryRef.child(diaryId);
        diaryDetailRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Diaryku_model diary = snapshot.getValue(Diaryku_model.class);
                    if (diary != null) {
                        Intent intent = new Intent(Diaryku.this, Diaryku_detail.class);
                        intent.putExtra("diaryId", diary.getDiaryId());
                        intent.putExtra("title", diary.getTitle());
                        intent.putExtra("content", diary.getContent());
                        intent.putExtra("date", diary.getDate());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}