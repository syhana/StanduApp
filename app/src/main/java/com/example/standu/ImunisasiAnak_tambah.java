package com.example.standu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ImunisasiAnak_tambah extends AppCompatActivity {

    private EditText namaImunisasiTambah, tanggalImunisasiTambah;
    private Button button_simpan;
    private ImageButton button_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imunisasi_anak_tambah);

        namaImunisasiTambah = findViewById(R.id.tambah_namaImunisasi);
        tanggalImunisasiTambah = findViewById(R.id.tambah_tanggalImunisasi);
        button_simpan = findViewById(R.id.button_simpan);
        button_back = findViewById(R.id.button_back_pink);

        tanggalImunisasiTambah.setOnClickListener(view -> showDatePickerDialog());
        button_simpan.setOnClickListener(view -> tambahImunisasi());
        button_back.setOnClickListener(view -> onBackPressed());
    }

    private void tambahImunisasi() {
        String namaImunisasi = namaImunisasiTambah.getText().toString().trim();
        String tanggalImunisasi = tanggalImunisasiTambah.getText().toString().trim();

        if (TextUtils.isEmpty(namaImunisasi) || TextUtils.isEmpty(tanggalImunisasi)) {
            Toast.makeText(this, "Harap isi semua informasi imunisasi", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = SessionManager.getUserDetails(this).username;
        String namaAnak = getIntent().getStringExtra("namaAnak");

        DatabaseReference imunisasiRef = FirebaseDatabase.getInstance().getReference("imunisasi").child(username).child(namaAnak);

        String imunisasiId = imunisasiRef.push().getKey();

        ImunisasiAnak_model imunisasi = new ImunisasiAnak_model(namaAnak, imunisasiId, namaImunisasi, tanggalImunisasi);

        imunisasiRef.child(imunisasiId).setValue(imunisasi)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Imunisasi berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal menambahkan imunisasi ke Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int defaultYear = calendar.get(Calendar.YEAR);
        int defaultMonth = calendar.get(Calendar.MONTH);
        int defaultDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    String selectedDate = convertDateFormat(dayOfMonth + "/" + (month + 1) + "/" + year);
                    tanggalImunisasiTambah.setText(selectedDate);
                },
                defaultYear,
                defaultMonth,
                defaultDay);

        datePickerDialog.show();
    }

    private String convertDateFormat(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = inputFormat.parse(inputDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return inputDate;
        }
    }
}