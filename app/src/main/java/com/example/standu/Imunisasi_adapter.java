package com.example.standu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Imunisasi_adapter extends RecyclerView.Adapter<Imunisasi_adapter.ImunisasiViewHolder> {

    private List<Imunisasi_model> listAnak;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public Imunisasi_adapter(List<Imunisasi_model> listAnak, Context context) {
        this.listAnak = listAnak;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(Imunisasi_adapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ImunisasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_imunisasi_item, parent, false);
        return new ImunisasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImunisasiViewHolder holder, int position) {
        Imunisasi_model anak = listAnak.get(position);
        holder.textNamaAnak.setText(anak.getNamaAnak());

        holder.itemView.setOnClickListener(view ->
                openAnakDetails(anak.getAnakId())
        );
    }

    @Override
    public int getItemCount() {
        return listAnak.size();
    }

    public static class ImunisasiViewHolder extends RecyclerView.ViewHolder {
        public TextView textNamaAnak;

        public ImunisasiViewHolder(@NonNull View itemView) {
            super(itemView);
            textNamaAnak = itemView.findViewById(R.id.text_namaAnak);
        }
    }

    private void openAnakDetails(String anakId) {
        Intent intent = new Intent(context, Imunisasi_detail.class);
        intent.putExtra("anakId", anakId);
        intent.putExtra("namaAnak", getNamaAnakByAnakId(anakId));
        intent.putExtra("umurAnak", getUmurAnakByAnakId(anakId));
        intent.putExtra("jkAnak", getJkAnakByAnakId(anakId));
        intent.putExtra("bbAnak", getBbAnakByAnakId(anakId));
        intent.putExtra("tbAnak", getTbAnakByAnakId(anakId));
        context.startActivity(intent);
    }

    private String getNamaAnakByAnakId(String anakId) {
        for (Imunisasi_model anak : listAnak) {
            if (anak.getAnakId().equals(anakId)) {
                return anak.getNamaAnak();
            }
        }
        return "";
    }

    private String getUmurAnakByAnakId(String anakId) {
        for (Imunisasi_model anak : listAnak) {
            if (anak.getAnakId().equals(anakId)) {
                return anak.getUmurAnak();
            }
        }
        return "";
    }

    private String getJkAnakByAnakId(String anakId) {
        for (Imunisasi_model anak : listAnak) {
            if (anak.getAnakId().equals(anakId)) {
                return anak.getJkAnak();
            }
        }
        return "";
    }

    private String getBbAnakByAnakId(String anakId) {
        for (Imunisasi_model anak : listAnak) {
            if (anak.getAnakId().equals(anakId)) {
                return anak.getBbAnak();
            }
        }
        return "";
    }

    private String getTbAnakByAnakId(String anakId) {
        for (Imunisasi_model anak : listAnak) {
            if (anak.getAnakId().equals(anakId)) {
                return anak.getTbAnak();
            }
        }
        return "";
    }
}
