package com.example.standu;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ImunisasiAnak_adapter extends RecyclerView.Adapter<ImunisasiAnak_adapter.ImunisasiAnakViewHolder>{
    private List<ImunisasiAnak_model> listImunisasi;
    private Context context;
    private OnItemClickListener onItemClickListener;
    
    public ImunisasiAnak_adapter(List<ImunisasiAnak_model> listImunisasi, Context context){
        this.listImunisasi = listImunisasi;
        this.context = context;
    }
    
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    
    public void setOnItemClickListener(ImunisasiAnak_adapter.OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ImunisasiAnakViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_imunisasi_anak_item, parent, false);
        return new ImunisasiAnakViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImunisasiAnak_adapter.ImunisasiAnakViewHolder holder, int position) {
        ImunisasiAnak_model imunisasi = listImunisasi.get(position);
        holder.textNamaImunisasi.setText(imunisasi.getNamaImunisasi());
        holder.textTanggalImunisasi.setText(imunisasi.getTanggalImunisasi());

        holder.itemView.setOnClickListener(view ->
                openDialogs(imunisasi, position)
        );
    }

    private void openDialogs(ImunisasiAnak_model imunisasi, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Konfirmasi Penghapusan");
        builder.setMessage("Apakah Anda ingin menghapus history ini?");

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listImunisasi.remove(position);
                notifyItemRemoved(position);

                deleteItem(imunisasi);

                Toast.makeText(context, "History dihapus", Toast.LENGTH_SHORT).show();
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

    private void deleteItem(ImunisasiAnak_model imunisasi) {
        DatabaseReference imunisasiRef = FirebaseDatabase.getInstance().getReference("imunisasi")
                .child(SessionManager.getUserDetails(context).username)
                .child(imunisasi.getNamaAnak())
                .child(imunisasi.getImunisasiId());

        imunisasiRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Item dihapus dari Firebase", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Gagal menghapus item dari Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public int getItemCount() {
        return listImunisasi.size();
    }

    public static class ImunisasiAnakViewHolder extends RecyclerView.ViewHolder {
        public TextView textNamaImunisasi, textTanggalImunisasi;

        public ImunisasiAnakViewHolder(@NonNull View itemView) {
            super(itemView);
            textNamaImunisasi = itemView.findViewById(R.id.text_namaImunisasi);
            textTanggalImunisasi = itemView.findViewById(R.id.text_tanggal_imunisasi);
        }
    }


}
