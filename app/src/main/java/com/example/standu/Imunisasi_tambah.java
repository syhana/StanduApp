package com.example.standu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Imunisasi_tambah extends AppCompatActivity {

    private EditText namaAnakTambah, umurAnakTambah, bbAnakTambah, tbAnakTambah;
    private DatabaseReference anakRef;
    private Button button_simpan;
    private ImageButton button_back;
    private AutoCompleteTextView autoCompleteTextViewJK;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SessionManager.isUserLoggedIn(this)){
            setContentView(R.layout.activity_imunisasi_tambah);
        }else{
            startActivity(new Intent(Imunisasi_tambah.this, MainActivity.class));
            finish();
        }

        String userId = SessionManager.getUserDetails(this).userId;
        anakRef = FirebaseDatabase.getInstance().getReference("anak").child(userId);

        namaAnakTambah = findViewById(R.id.tambah_namaAnak);
        umurAnakTambah = findViewById(R.id.tambah_umurAnak);
        bbAnakTambah = findViewById(R.id.tambah_bbAnak);
        tbAnakTambah = findViewById(R.id.tambah_tbAnak);
        autoCompleteTextViewJK = findViewById(R.id.tambah_jkAnak);

        button_simpan = findViewById(R.id.button_simpan);
        button_back = findViewById(R.id.button_back_pink);

        button_simpan.setOnClickListener(view -> saveAnak());
        button_back.setOnClickListener(view -> onBackPressed());


        String[] jenisKelaminArray = {"Laki-laki", "Perempuan"};

        ArrayAdapter<String> jenisKelaminAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, jenisKelaminArray) {
            @Override
            public Filter getFilter() {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults results = new FilterResults();
                        results.values = jenisKelaminArray;
                        results.count = jenisKelaminArray.length;
                        return results;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        notifyDataSetChanged();
                    }
                };
            }
        };

        autoCompleteTextViewJK.setAdapter(jenisKelaminAdapter);

        autoCompleteTextViewJK.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedValue = (String) adapterView.getItemAtPosition(position);
                autoCompleteTextViewJK.setText(selectedValue);
            }
        });

        autoCompleteTextViewJK.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                autoCompleteTextViewJK.showDropDown();
                return false;
            }
        });

    }

    private void saveAnak() {
        String namaAnak = namaAnakTambah.getText().toString().trim();
        String umurAnak = umurAnakTambah.getText().toString().trim();
        String jkAnak = autoCompleteTextViewJK.getText().toString().trim();
        String bbAnak = bbAnakTambah.getText().toString().trim();
        String tbAnak = tbAnakTambah.getText().toString().trim();

        if (TextUtils.isEmpty(namaAnak) || TextUtils.isEmpty(umurAnak) || TextUtils.isEmpty(jkAnak)
                || TextUtils.isEmpty(bbAnak) || TextUtils.isEmpty(tbAnak)) {
            Toast.makeText(this, "Harap isi semua informasi anak", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference namaAnakChildRef = anakRef.child(namaAnak);

        anakRef.orderByChild("namaAnak").equalTo(namaAnak).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(Imunisasi_tambah.this, "Anak dengan nama tersebut sudah terdaftar", Toast.LENGTH_SHORT).show();
                }else{
                    String anakId = namaAnakChildRef.push().getKey();
                    Imunisasi_model anak = new Imunisasi_model(anakId, namaAnak, umurAnak, jkAnak, bbAnak, tbAnak);
                    anakRef.child(namaAnak).setValue(anak);

                    Toast.makeText(Imunisasi_tambah.this, "Data anak berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}