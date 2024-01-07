package com.example.standu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Imunisasi_edit extends AppCompatActivity {

    private EditText editUmur, editBb, editTb;
    private TextView lihatNama, lihatJk;
    private Button button_simpan;
    private ImageButton button_back;
    private DatabaseReference anakRef;
    private String anakId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SessionManager.isUserLoggedIn(this)){
            setContentView(R.layout.activity_imunisasi_edit);
        }else{
            startActivity(new Intent(Imunisasi_edit.this, MainActivity.class));
            finish();
        }

        editUmur = findViewById(R.id.edit_umurAnak);
        editBb = findViewById(R.id.edit_bbAnak);
        editTb = findViewById(R.id.edit_tbAnak);
        lihatNama = findViewById(R.id.edit_namaAnak);
        lihatJk = findViewById(R.id.edit_jkAnak);
        button_simpan = findViewById(R.id.button_simpan);
        button_back = findViewById(R.id.button_back_pink);

        anakId = getIntent().getStringExtra("anakId");
        String namaAnak = getIntent().getStringExtra("namaAnak");
        if (anakId == null) {
            Toast.makeText(this, "Error: anakId not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        anakRef = FirebaseDatabase.getInstance().getReference("anak")
                .child(SessionManager.getUserDetails(this).username)
                .child(namaAnak);

        anakRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Imunisasi_model anak = snapshot.getValue(Imunisasi_model.class);
                    if(anak != null){
                        editUmur.setText(anak.getUmurAnak());
                        editBb.setText(anak.getBbAnak());
                        editTb.setText(anak.getTbAnak());
                        lihatNama.setText(anak.getNamaAnak());
                        lihatJk.setText(anak.getJkAnak());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Imunisasi_edit.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        button_back.setOnClickListener(view -> onBackPressed());
        button_simpan.setOnClickListener(view -> saveAnak());
    }

    private void saveAnak() {
        String namaAnak = lihatNama.getText().toString().trim();
        String umurAnak = editUmur.getText().toString().trim();
        String jkAnak = lihatJk.getText().toString().trim();
        String bbAnak = editBb.getText().toString().trim();
        String tbAnak = editTb.getText().toString().trim();

        if (umurAnak.isEmpty() || bbAnak.isEmpty() || tbAnak.isEmpty()) {
            Toast.makeText(this, "Harap isi semua", Toast.LENGTH_SHORT).show();
            return;
        }

        Imunisasi_model updatedAnak = new Imunisasi_model(anakId, namaAnak, umurAnak, jkAnak, bbAnak, tbAnak);
        anakRef.setValue(updatedAnak);

        anakRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Imunisasi_model updatedAnak = snapshot.getValue(Imunisasi_model.class);
                    if(updatedAnak != null){
                        Intent intent = new Intent(Imunisasi_edit.this, Imunisasi_detail.class);
                        intent.putExtra("anakId", updatedAnak.getAnakId());
                        intent.putExtra("namaAnak", updatedAnak.getNamaAnak());
                        intent.putExtra("umurAnak", updatedAnak.getUmurAnak());
                        intent.putExtra("jkAnak", updatedAnak.getJkAnak());
                        intent.putExtra("bbAnak", updatedAnak.getBbAnak());
                        intent.putExtra("tbAnak", updatedAnak.getTbAnak());
                        startActivity(intent);
                        Toast.makeText(Imunisasi_edit.this, "Data anak berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}