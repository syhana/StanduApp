package com.example.standu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Nutrisi_detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SessionManager.isUserLoggedIn(this)){
            setContentView(R.layout.activity_nutrisi_detail);
        }else{
            startActivity(new Intent(Nutrisi_detail.this, MainActivity.class));
            finish();
        }

        // Terima data dari intent
        Nutrisi_model nutrisiModel = (Nutrisi_model) getIntent().getSerializableExtra("nutrisi_model");

        // Inisialisasi tampilan
        ImageView imageView = findViewById(R.id.detailImage);
        TextView titleTextView = findViewById(R.id.detailTitle);
        TextView detailInfoTextView = findViewById(R.id.detailInfo);
        TextView kandunganVitaminTextView = findViewById(R.id.kandunganvit);
        TextView resepMpasiTextView = findViewById(R.id.resepmpasi);
        TextView bahanTextView = findViewById(R.id.bahanmpasi);
        TextView cara_membuatTextView = findViewById(R.id.cara_membuat);

        // Gunakan Glide untuk memuat gambar dari URL
        Glide.with(this)
                .load(nutrisiModel.getTurl())
                .into(imageView);

        // Set teks pada TextView
        titleTextView.setText(nutrisiModel.getName());
        detailInfoTextView.setText(nutrisiModel.getDetail());
        kandunganVitaminTextView.setText( nutrisiModel.getKandungan_vitamin());
        resepMpasiTextView.setText(nutrisiModel.getResep_mpasi());
        bahanTextView.setText(nutrisiModel.getBahan());
        cara_membuatTextView.setText(nutrisiModel.getCara_membuat());

        // Ambil nilai resep_mpasi, bahan_mpasi, dan cara_membuat dari model
        String resepMpasi = nutrisiModel.getResep_mpasi();
        String bahan = nutrisiModel.getBahan();
        String cara_membuat = nutrisiModel.getCara_membuat();

        // Ganti setiap '\n' dengan newline (enter)
        String resepMpasiFormatted = resepMpasi.replace("\\n", "\n");
        String bahanFormatted = bahan.replace("\\n", "\n");
        String caraMembuatFormatted = cara_membuat.replace("\\n", "\n");

        // Gunakan Html.fromHtml() untuk memproses teks HTML
        String resepMpasiHtml = resepMpasiFormatted;
        resepMpasiTextView.setText(HtmlCompat.fromHtml(resepMpasiHtml, HtmlCompat.FROM_HTML_MODE_LEGACY));

        // Set teks pada TextView bahanTextView dan cara_membuatTextView
        bahanTextView.setText( bahanFormatted);
        cara_membuatTextView.setText(caraMembuatFormatted);

        // Tambahkan event klik pada tombol kembali
        // Tambahkan event klik pada tombol kembali
        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent untuk kembali ke MainActivity
                Intent intent = new Intent(Nutrisi_detail.this, Nutrisi.class);
                startActivity(intent);
                finish(); // Tutup aktivitas saat ini agar tidak bisa dikembalikan dengan tombol back
            }
        });
    }
}