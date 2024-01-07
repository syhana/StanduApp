package com.example.standu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Imunisasi_detail extends AppCompatActivity {

    private TextView namaAnakLihat, umurAnakLihat, jkAnakLihat, bbAnakLihat, tbAnakLihat;
    private ImageButton button_back;
    private Button  button_edit, button_hapus, buttom_tambah_history;
    private DatabaseReference anakRef, imunisasiRef;
    private ImunisasiAnak_adapter adapter;
    private RecyclerView recyclerView;
    List<ImunisasiAnak_model> imunisasiList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SessionManager.isUserLoggedIn(this)){
            setContentView(R.layout.activity_imunisasi_detail);
        }else{
            startActivity(new Intent(Imunisasi_detail.this, MainActivity.class));
            finish();
        }

        namaAnakLihat = findViewById(R.id.lihat_namaAnak);
        umurAnakLihat = findViewById(R.id.lihat_umurAnak);
        jkAnakLihat = findViewById(R.id.lihat_jkAnak);
        bbAnakLihat = findViewById(R.id.lihat_bbAnak);
        tbAnakLihat = findViewById(R.id.lihat_tbAnak);
        button_back = findViewById(R.id.button_back_pink);
        button_edit = findViewById(R.id.button_edit);
        button_hapus = findViewById(R.id.button_hapus);
        buttom_tambah_history = findViewById(R.id.button_tambah_history);
        recyclerView = findViewById(R.id.recycler_view_imunisasi_detail);

        String anakId = getIntent().getStringExtra("anakId");
        String namaAnak = getIntent().getStringExtra("namaAnak");
        String umurAnak = getIntent().getStringExtra("umurAnak");
        String jkAnak = getIntent().getStringExtra("jkAnak");
        String bbAnak = getIntent().getStringExtra("bbAnak");
        String tbAnak = getIntent().getStringExtra("tbAnak");

        namaAnakLihat.setText(namaAnak);
        umurAnakLihat.setText(umurAnak + " tahun");
        jkAnakLihat.setText(jkAnak);
        bbAnakLihat.setText(bbAnak + " kg");
        tbAnakLihat.setText(tbAnak + " cm");

        String username = SessionManager.getUserDetails(this).username;
        anakRef = FirebaseDatabase.getInstance().getReference("anak")
                .child(username).child(namaAnak);
        imunisasiRef = FirebaseDatabase.getInstance().getReference("imunisasi").child(username).child(namaAnak);
        imunisasiList = new ArrayList<>();

        imunisasiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imunisasiList.clear();

                for (DataSnapshot anakSnapshot : dataSnapshot.getChildren()){
                    ImunisasiAnak_model imunisasi = anakSnapshot.getValue(ImunisasiAnak_model.class);
                    if (imunisasi != null) {
                        imunisasiList.add(imunisasi);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new ImunisasiAnak_adapter(imunisasiList,  this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        button_back.setOnClickListener(view -> startActivity(new Intent(Imunisasi_detail.this, Imunisasi.class)));
        button_edit.setOnClickListener(view -> openAnakEdit(anakId, namaAnak, umurAnak, jkAnak, bbAnak,tbAnak));
        button_hapus.setOnClickListener(view -> showDeleteConfirmation());
        buttom_tambah_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Imunisasi_detail.this, ImunisasiAnak_tambah.class);
                intent.putExtra("namaAnak", namaAnak);
                startActivity(intent);
            }
        });
    }

    private void openAnakEdit(String anakId, String namaAnak, String umurAnak, String jkAnak, String bbAnak, String tbAnak) {
        Intent intent = new Intent(Imunisasi_detail.this, Imunisasi_edit.class);
        intent.putExtra("anakId", anakId);
        intent.putExtra("namaAnak", namaAnak);
        intent.putExtra("umurAnak", umurAnak);
        intent.putExtra("jkAnak", jkAnak);
        intent.putExtra("bbAnak", bbAnak);
        intent.putExtra("tbAnak", tbAnak);
        startActivity(intent);
    }

    private void showDeleteConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Penghapusan");
        builder.setMessage("Apakah Anda yakin ingin menghapus data anak ini?");

        builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                anakRef.removeValue();
                Toast.makeText(Imunisasi_detail.this, "Data Anak dihapus", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Imunisasi_detail.this, Imunisasi.class));
                finish();
            }
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}