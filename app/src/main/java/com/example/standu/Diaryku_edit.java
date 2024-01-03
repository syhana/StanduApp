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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Diaryku_edit extends AppCompatActivity {

    private EditText editTitle, editContent, editDate;
    private Button button_simpan;
    private ImageButton button_back;
    private DatabaseReference diaryRef;
    private String diaryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaryku_edit);

        editTitle = findViewById(R.id.edit_judul);
        editContent = findViewById(R.id.edit_catatan);
        editDate = findViewById(R.id.edit_tanggal);
        button_simpan = findViewById(R.id.button_simpan);
        button_back = findViewById(R.id.button_back_green);

        diaryId = getIntent().getStringExtra("diaryId");

        if (diaryId == null) {
            Toast.makeText(this, "Error: DiaryId not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        String uid = SessionManager.getUserDetails(this).uid;
        diaryRef = FirebaseDatabase.getInstance().getReference("diaries").child(uid).child(diaryId);

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

        editDate.setOnClickListener(view -> showDatePickerDialog());
        button_simpan.setOnClickListener(view -> saveDiary());
        button_back.setOnClickListener(view -> onBackPressed());
    }

    private void showDatePickerDialog() {
        // Mendapatkan tanggal hari ini
        Calendar calendar = Calendar.getInstance();
        int defaultYear = calendar.get(Calendar.YEAR);
        int defaultMonth = calendar.get(Calendar.MONTH);
        int defaultDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    editDate.setText(selectedDate);
                },
                defaultYear,
                defaultMonth,
                defaultDay);

        datePickerDialog.show();
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

        Toast.makeText(this, "Catatan harian berhasil diperbarui", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Diaryku_edit.this, Diaryku.class));
        finish();
    }
}