package com.example.standu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Sharingku_detail extends AppCompatActivity {

    private TextView judulLihat, ceritaLihat;
    private ImageView imageLihat;
    private ImageButton button_back;
    private Button button_edit, button_hapus;
    private DatabaseReference sharingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharingku_detail);

        judulLihat = findViewById(R.id.judulSharing);
        ceritaLihat = findViewById(R.id.ceritaSharing);
        imageLihat = findViewById(R.id.imageSharing);
        button_back = findViewById(R.id.button_back);
        button_hapus = findViewById(R.id.button_hapus);
        button_edit = findViewById(R.id.button_edit);

        String sharingId = getIntent().getStringExtra("sharingId");
        String judul = getIntent().getStringExtra("judul");
        String cerita = getIntent().getStringExtra("cerita");
        String image = getIntent().getStringExtra("image");

        button_back.setOnClickListener(view -> startActivity(new Intent(Sharingku_detail.this, Sharingku.class)));
        button_hapus.setOnClickListener(view -> showDeleteConfirmation());
        button_edit.setOnClickListener(view -> openSharingEdit(sharingId, judul, cerita, image));

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            judulLihat.setText(bundle.getString("judul"));
            ceritaLihat.setText(bundle.getString("cerita"));
            Glide.with(this).load(bundle.getString("image")).into(imageLihat);
        }

        String username = SessionManager.getUserDetails(this).username;
        sharingRef = FirebaseDatabase.getInstance().getReference("sharing").child(username).child(sharingId);
    }

    private void openSharingEdit(String sharingId, String judul, String cerita, String image) {
        Intent intent = new Intent(Sharingku_detail.this, Sharingku_edit.class);
        intent.putExtra("sharingId", sharingId);
        intent.putExtra("judul", judul);
        intent.putExtra("image", image);
        startActivity(intent);
    }

    private void showDeleteConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Penghapusan");
        builder.setMessage("Apakah Anda yakin ingin menghapus sharing ini?");

        builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String imageUrl = getIntent().getStringExtra("image");
                if(imageUrl != null && !imageUrl.isEmpty()){
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // File gambar berhasil dihapus dari Firebase Storage
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Gagal menghapus file gambar dari Firebase Storage
                        }
                    });
                }
                sharingRef.removeValue();
                Toast.makeText(Sharingku_detail.this, "Data sharing dihapus", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Sharingku_detail.this, Sharingku.class));
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