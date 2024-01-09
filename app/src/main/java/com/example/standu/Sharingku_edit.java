package com.example.standu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sharingku_edit extends AppCompatActivity {
    private EditText editJudul, editCerita;
    private ImageView editImage;
    private Button button_simpan;
    private ImageButton button_back;
    private String sharingId, imageUrl;
    private DatabaseReference sharingRef;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharingku_edit);

        editJudul = findViewById(R.id.edit_judul);
        editCerita = findViewById(R.id.edit_cerita);
        editImage = findViewById(R.id.edit_gambar);
        button_back = findViewById(R.id.button_back_purple);
        button_simpan = findViewById(R.id.button_simpan);

        sharingId = getIntent().getStringExtra("sharingId");
        imageUrl = getIntent().getStringExtra("image");

        sharingRef = FirebaseDatabase.getInstance().getReference("sharing")
                .child(SessionManager.getUserDetails(this).userId)
                .child(sharingId);

        sharingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Sharingku_model sharing = snapshot.getValue(Sharingku_model.class);
                    if(sharing != null){
                        editJudul.setText(sharing.getJudul());
                        editCerita.setText(sharing.getCerita());
                        Glide.with(Sharingku_edit.this).load(sharing.getImg()).into(editImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button_back.setOnClickListener(view -> onBackPressed());
        button_simpan.setOnClickListener(view -> saveSharing());
    }

    private void saveSharing() {
        String judul = editJudul.getText().toString().trim();
        String cerita = editCerita.getText().toString().trim();

        Sharingku_model updatedSharing = new Sharingku_model(sharingId, judul, cerita, imageUrl);
        sharingRef.setValue(updatedSharing);

        sharingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Sharingku_model updatedSharing = snapshot.getValue(Sharingku_model.class);
                    if(updatedSharing != null){
                        Intent intent = new Intent(Sharingku_edit.this, Sharingku_detail.class);
                        intent.putExtra("sharingId", updatedSharing.getSharingId());
                        intent.putExtra("judul", updatedSharing.getJudul());
                        intent.putExtra("cerita", updatedSharing.getCerita());
                        intent.putExtra("image", updatedSharing.getImg());
                        startActivity(intent);
                        Toast.makeText(Sharingku_edit.this, "Sharing berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
