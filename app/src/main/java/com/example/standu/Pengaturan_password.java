package com.example.standu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Pengaturan_password extends AppCompatActivity {

    private EditText passwordLamaEditText, passwordBaruEditText;
    private Button ubahButton;
    private ImageButton button_back;
    private DatabaseReference userRef;
    private ToggleButton show_passLama, show_passBaru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SessionManager.isUserLoggedIn(this)){
            setContentView(R.layout.activity_pengaturan_password);
        }else{
            startActivity(new Intent(Pengaturan_password.this, MainActivity.class));
            finish();
        }

        userRef = FirebaseDatabase.getInstance().getReference("users");

        passwordLamaEditText = findViewById(R.id.password_lama);
        passwordBaruEditText = findViewById(R.id.password_baru);
        ubahButton = findViewById(R.id.button_ubah_password);
        button_back = findViewById(R.id.button_back);
        show_passLama = findViewById(R.id.show_lama);
        show_passBaru = findViewById(R.id.show_baru);

        show_passLama.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            int inputType = isChecked ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
            passwordLamaEditText.setInputType(inputType);
        }));
        show_passBaru.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            int inputType = isChecked ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
            passwordBaruEditText.setInputType(inputType);
        }));
        ubahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubahPassword();
            }
        });
        button_back.setOnClickListener(view -> onBackPressed());
    }

    private void ubahPassword() {
        final String passwordLama = passwordLamaEditText.getText().toString().trim();
        final String passwordBaru = passwordBaruEditText.getText().toString().trim();

        if (passwordLama.isEmpty() || passwordBaru.isEmpty()) {
            Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        SessionManager.User user = SessionManager.getUserDetails(this);

        if (user != null) {
            String username = user.username;

            userRef.child(username).child("pass").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String passwordDatabase = dataSnapshot.getValue(String.class);

                        if (passwordDatabase != null && passwordDatabase.equals(passwordLama)) {
                            userRef.child(username).child("pass").setValue(passwordBaru)
                                    .addOnCompleteListener(databaseTask -> {
                                        if (databaseTask.isSuccessful()) {
                                            Toast.makeText(Pengaturan_password.this, "Password berhasil diubah", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(Pengaturan_password.this, "Gagal menyimpan password", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(Pengaturan_password.this, "Password lama tidak valid", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
}