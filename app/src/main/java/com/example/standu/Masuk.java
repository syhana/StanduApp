package com.example.standu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Masuk extends AppCompatActivity {


    private EditText emailMasuk, passwordMasuk;
    private Button button_masuk;
    private TextView redirect_daftar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk);

        if(SessionManager.isUserLoggedIn(this)){
            masukKeHome();
        }else{
            emailMasuk = findViewById(R.id.email_masuk);
            passwordMasuk = findViewById(R.id.password_masuk);
            button_masuk = findViewById(R.id.button_masuk);
            redirect_daftar = findViewById(R.id.link_daftar);

            button_masuk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!validateEmail()| !validatePassword()){

                    }else {
                        checkUser();
                    }
                }
            });

            redirect_daftar.setOnClickListener(view ->
                    startActivity(new Intent(Masuk.this, Daftar.class)));
        }
    }

    private void masukKeHome() {
        startActivity(new Intent(Masuk.this, Home.class));
    }

    public Boolean validateEmail(){
        String val = emailMasuk.getText().toString();
        if(val.isEmpty()){
            emailMasuk.setError("Email tidak boleh kosong");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(val).matches()){
            emailMasuk.setError("Email tidak valid");
            return false;
        }else{
            emailMasuk.setError(null);
            return true;
        }
    }
    public Boolean validatePassword(){
         String val = passwordMasuk.getText().toString();
         if(val.isEmpty()){
             passwordMasuk.setError("Password tidak boleh kosong");
             return false;
         }else{
             passwordMasuk.setError(null);
             return true;
         }
    }

    public void checkUser(){
        String userMail = emailMasuk.getText().toString().trim();
        String userPass = passwordMasuk.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("mail").equalTo(userMail);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String uid = userSnapshot.getKey();

                        emailMasuk.setError(null);
                        String passwordFromDB = userSnapshot.child("pass").getValue(String.class);

                        if(passwordFromDB.equals(userPass)){
                            emailMasuk.setError(null);

                            String namaFromDB = userSnapshot.child("nama").getValue(String.class);
                            String emailFromDB = userSnapshot.child("mail").getValue(String.class);
                            String usernameFromDB = userSnapshot.child("user").getValue(String.class);

                            Intent intent = new Intent(Masuk.this, Home.class);

                            intent.putExtra("uid", uid);
                            intent.putExtra("nama", namaFromDB);
                            intent.putExtra("email", emailFromDB);
                            intent.putExtra("username", usernameFromDB);
                            intent.putExtra("password", passwordFromDB);

                            SessionManager.loginUser(Masuk.this, uid, namaFromDB, emailFromDB, usernameFromDB,passwordFromDB);
                            Toast.makeText(Masuk.this, "Selamat Datang", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        } else {
                            passwordMasuk.setError("Password Anda salah");
                            passwordMasuk.requestFocus();
                        }
                    }
                } else {
                    emailMasuk.setError("Email tidak terdaftar");
                    emailMasuk.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}