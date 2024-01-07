package com.example.standu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Imunisasi extends AppCompatActivity {

    Button button_tambah, button_lihat;
    ImageButton button_back;
    private RecyclerView recyclerView;
    private Imunisasi_adapter adapter;
    private List<Imunisasi_model> anakList;
    private DatabaseReference anakRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SessionManager.isUserLoggedIn(this)){
            setContentView(R.layout.activity_imunisasi);
        }else{
            startActivity(new Intent(Imunisasi.this, MainActivity.class));
            finish();
        }

        button_back = findViewById(R.id.button_back_pink);
        button_tambah = findViewById(R.id.imunisasi_button_tambah);
        button_lihat = findViewById(R.id.button_lihat);

        button_back.setOnClickListener(view -> startActivity(new Intent(Imunisasi.this, Home.class)));
        button_tambah.setOnClickListener(view -> {
            startActivity(new Intent(Imunisasi.this, Imunisasi_tambah.class));
        });
        button_lihat.setOnClickListener(view -> {
            if (isOnline()) {
                startActivity(new Intent(Imunisasi.this, Imunisasi_maps.class));
            } else {
                Toast.makeText(Imunisasi.this, "Anda sedang offline. Harap periksa koneksi internet Anda.", Toast.LENGTH_SHORT).show();
            }
        });

        String username = SessionManager.getUserDetails(this).username;
        anakRef = FirebaseDatabase.getInstance().getReference("anak").child(username);

        recyclerView = findViewById(R.id.recycler_view_imunisasi);
        anakList = new ArrayList<>();

        anakRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                anakList.clear();

                for (DataSnapshot anakSnapshot : dataSnapshot.getChildren()){
                    Imunisasi_model anak = anakSnapshot.getValue(Imunisasi_model.class);
                    if (anak != null) {
                        anakList.add(anak);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new Imunisasi_adapter(anakList,  this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Imunisasi_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                openAnakDetail(anakList.get(position).getAnakId());
            }
        });
    }
    private void openAnakDetail(String anakId){
        DatabaseReference anakDetailRef = anakRef.child(anakId);
        anakDetailRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Imunisasi_model anak = snapshot.getValue(Imunisasi_model.class);
                    if(anak != null){
                        Intent intent = new Intent(Imunisasi.this, Imunisasi_detail.class);
                        intent.putExtra("anakId", anak.getAnakId());
                        intent.putExtra("namaAnak", anak.getNamaAnak());
                        intent.putExtra("umurAnak", anak.getUmurAnak());
                        intent.putExtra("jkAnak", anak.getJkAnak());
                        intent.putExtra("bbAnak", anak.getBbAnak());
                        intent.putExtra("tbAnak", anak.getTbAnak());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}