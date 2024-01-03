package com.example.standu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class Diaryku_tambah extends AppCompatActivity {

    private EditText titleTambah, contentTambah, dateTambah;
    private Button button_simpan;
    private ImageButton button_back;
    private DatabaseReference diaryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SessionManager.isUserLoggedIn(this)){
            setContentView(R.layout.activity_diaryku_tambah);
        }else{
            startActivity(new Intent(Diaryku_tambah.this, MainActivity.class));
            finish();
        }

        String uid = SessionManager.getUserDetails(this).uid;
        diaryRef = FirebaseDatabase.getInstance().getReference("diaries").child(uid);

        titleTambah = findViewById(R.id.tambah_judul);
        contentTambah = findViewById(R.id.tambah_catatan);
        dateTambah = findViewById(R.id.tambah_tanggal);
        button_simpan = findViewById(R.id.button_simpan);
        button_back = findViewById(R.id.button_back_green);

        dateTambah.setOnClickListener(view -> showDatePickerDialog());
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
                    dateTambah.setText(selectedDate);
                },
                defaultYear,
                defaultMonth,
                defaultDay);

        datePickerDialog.show();
    }

    private void saveDiary() {
        String title = titleTambah.getText().toString().trim();
        String content = contentTambah.getText().toString().trim();
        String date = dateTambah.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Harap isi judul dan catatan", Toast.LENGTH_SHORT).show();
            return;
        }

        String diaryId = diaryRef.push().getKey();
        Diaryku_model diary = new Diaryku_model(diaryId, title, content, date);
        diaryRef.child(diaryId).setValue(diary);

        Toast.makeText(this, "Catatan harian berhasil disimpan", Toast.LENGTH_SHORT).show();
        finish();
    }
}

