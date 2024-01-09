package com.example.standu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Sharingku_tambah extends AppCompatActivity {

    ImageButton button_back, uploadImage;
    Button button_simpan;
    EditText judulSharingTambah, ceritaSharingTambah;
    String imageURL;
    Uri uri;
    DatabaseReference sharingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharingku_tambah);

        String uid = SessionManager.getUserDetails(this).userId;
        sharingRef = FirebaseDatabase.getInstance().getReference("sharing").child(uid);

        button_back = findViewById(R.id.button_back_purple);
        button_simpan = findViewById(R.id.button_simpan);
        uploadImage = findViewById(R.id.tambah_gambar);
        judulSharingTambah = findViewById(R.id.tambah_judulSharingku);
        ceritaSharingTambah = findViewById(R.id.tambah_ceritaSharingku);

        button_simpan.setOnClickListener(v -> saveSharing());
        button_back.setOnClickListener(view -> startActivity(new Intent(Sharingku_tambah.this, Sharingku.class)));

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        }else{
                            Toast.makeText(Sharingku_tambah.this, "Gambar belum dipilih", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
    }

    public void saveSharing(){
        String uid = SessionManager.getUserDetails(this).userId;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("sharing")
                .child(uid).child(uri.getLastPathSegment());

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploadData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    private void uploadData() {
        String judul = judulSharingTambah.getText().toString().trim();
        String cerita = ceritaSharingTambah.getText().toString().trim();

        sharingRef.orderByChild("judul").equalTo(judul).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(Sharingku_tambah.this, "Judul sudah ada", Toast.LENGTH_SHORT).show();
                }else{
                    String sharingId = sharingRef.push().getKey();
                    Sharingku_model sharing = new Sharingku_model(sharingId, judul, cerita, imageURL);
                    sharingRef.child(sharingId).setValue(sharing);

                    Toast.makeText(Sharingku_tambah.this, "Sharing berhasil disimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}