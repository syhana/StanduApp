package com.example.standu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Pengaturan extends AppCompatActivity {

    private TextView namaLengkapLihat, usernameLihat, emailLihat;
    private Button button_ubahPassword, button_keluar;
    private ImageButton button_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SessionManager.isUserLoggedIn(this)) {
            setContentView(R.layout.activity_pengaturan);
        } else {
            startActivity(new Intent(Pengaturan.this, MainActivity.class));
            finish();
        }

        namaLengkapLihat = findViewById(R.id.namaLengkap);
        usernameLihat = findViewById(R.id.username);
        emailLihat = findViewById(R.id.email);
        button_ubahPassword = findViewById(R.id.button_ubah);
        button_keluar = findViewById(R.id.button_keluar);
        button_back = findViewById(R.id.button_back_yellow);
        button_ubahPassword = findViewById(R.id.button_ubah);

        String namaLengkap = SessionManager.getUserDetails(this).nama;
        String username = SessionManager.getUserDetails(this).username;
        String email = SessionManager.getUserDetails(this).email;

        namaLengkapLihat.setText(namaLengkap);
        usernameLihat.setText(username);
        emailLihat.setText(email);

        button_back.setOnClickListener(view -> startActivity(new Intent(Pengaturan.this, Home.class)));
        button_ubahPassword.setOnClickListener(view -> startActivity(new Intent(Pengaturan.this, Pengaturan_password.class)));
        button_keluar.setOnClickListener(view -> showLogoutConfirmationDialog());
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Logout");
        builder.setMessage("Apakah Anda yakin ingin keluar?");

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SessionManager.logoutUser(Pengaturan.this);
                Intent intent = new Intent(Pengaturan.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}