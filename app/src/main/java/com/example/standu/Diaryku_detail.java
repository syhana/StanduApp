package com.example.standu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Diaryku_detail extends AppCompatActivity {
    private TextView dateLihat, titleLihat, contentLihat;
    private Button button_edit, button_hapus;
    private ImageButton button_back;
    private DatabaseReference diaryRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SessionManager.isUserLoggedIn(this)){
            setContentView(R.layout.activity_diaryku_detail);
        }else{
            startActivity(new Intent(Diaryku_detail.this, MainActivity.class));
            finish();
        }

        titleLihat = findViewById(R.id.lihat_judul);
        contentLihat = findViewById(R.id.lihat_catatan);
        dateLihat = findViewById(R.id.lihat_tanggal);

        String diaryId = getIntent().getStringExtra("diaryId");
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String date = getIntent().getStringExtra("date");

        titleLihat.setText(title);
        contentLihat.setText(content);
        dateLihat.setText(date);

        button_edit = findViewById(R.id.button_edit);
        button_hapus = findViewById(R.id.button_hapus);
        button_back = findViewById(R.id.button_back_green);

        diaryRef = FirebaseDatabase.getInstance().getReference("diaries")
                .child(SessionManager.getUserDetails(this).userId)
                .child(date).child(diaryId);

        button_edit.setOnClickListener(view -> openDiaryEdit(diaryId, title, content, date));
        button_hapus.setOnClickListener(view -> showDeleteConfirmation());
        button_back.setOnClickListener(view -> startActivity(new Intent(Diaryku_detail.this, Diaryku.class)));
    }
    private void openDiaryEdit(String diaryId, String title, String content, String date) {
        Intent intent = new Intent(Diaryku_detail.this, Diaryku_edit.class);
        intent.putExtra("diaryId", diaryId);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("date", date);
        startActivity(intent);
    }

    private void showDeleteConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Penghapusan");
        builder.setMessage("Apakah Anda yakin ingin menghapus catatan ini?");
        builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                diaryRef.removeValue();
                Toast.makeText(Diaryku_detail.this, "Catatan dihapus", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Diaryku_detail.this, Diaryku.class));
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