package com.example.standu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Diaryku_edit extends AppCompatActivity {

    private EditText editTitle, editContent;
    private TextView editDate;
    private Button button_simpan;
    private ImageButton button_back;
    private DatabaseReference diaryRef;
    private String diaryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SessionManager.isUserLoggedIn(this)){
            setContentView(R.layout.activity_diaryku_edit);
        }else{
            startActivity(new Intent(Diaryku_edit.this, MainActivity.class));
            finish();
        }

        editTitle = findViewById(R.id.edit_judul);
        editContent = findViewById(R.id.edit_catatan);
        editDate = findViewById(R.id.edit_tanggal);
        button_simpan = findViewById(R.id.button_simpan);
        button_back = findViewById(R.id.button_back_green);

        diaryId = getIntent().getStringExtra("diaryId");
        String date = getIntent().getStringExtra("date");

        if (diaryId == null) {
            Toast.makeText(this, "Error: DiaryId not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        diaryRef = FirebaseDatabase.getInstance().getReference("diaries")
                .child(SessionManager.getUserDetails(this).userId)
                .child(date).child(diaryId);

        // Mengambil data dari Firebase Realtime Database berdasarkan diaryId
        diaryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Diaryku_model diary = snapshot.getValue(Diaryku_model.class);
                    if (diary != null) {
                        // Menampilkan data yang ada di Firebase pada EditText
                        editTitle.setText(diary.getTitle());
                        editContent.setText(diary.getContent());
                        editDate.setText(diary.getDate());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(Diaryku_edit.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        button_simpan.setOnClickListener(view -> saveDiary());
        button_back.setOnClickListener(view -> onBackPressed());
    }


    private void saveDiary() {
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();
        String date = editDate.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Harap isi semua", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update data diary ke Firebase Realtime Database
        Diaryku_model updatedDiary = new Diaryku_model(diaryId, title, content, date);
        diaryRef.setValue(updatedDiary);

        // Setelah menyimpan, baca data yang baru saja disimpan
        diaryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Diaryku_model updatedDiary = snapshot.getValue(Diaryku_model.class);

                    if (updatedDiary != null) {
                        // Navigasi ke Diaryku_detail dengan data yang baru
                        Intent intent = new Intent(Diaryku_edit.this, Diaryku_detail.class);
                        intent.putExtra("diaryId", updatedDiary.getDiaryId());
                        intent.putExtra("title", updatedDiary.getTitle());
                        intent.putExtra("content", updatedDiary.getContent());
                        intent.putExtra("date", updatedDiary.getDate());
                        startActivity(intent);
                        Toast.makeText(Diaryku_edit.this, "Catatan harian berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}