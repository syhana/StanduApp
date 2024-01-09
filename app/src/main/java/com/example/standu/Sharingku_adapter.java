package com.example.standu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Sharingku_adapter extends RecyclerView.Adapter<Sharingku_adapter.SharingkuViewHolder> {
    private List<Sharingku_model> listSharing;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public Sharingku_adapter(List<Sharingku_model> listSharing, Context context) {
        this.listSharing = listSharing;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @NonNull
    @Override
    public SharingkuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_sharingku_item, parent, false);
        return new SharingkuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SharingkuViewHolder holder, int position) {
        Sharingku_model sharing = listSharing.get(position);
        Glide.with(context).load(sharing.getImg()).into(holder.imageSharing);
        holder.judulSharing.setText(sharing.getJudul());
        holder.ceritaSharing.setText(sharing.getCerita());

        holder.itemView.setOnClickListener(view ->
                openSharingDetails(sharing.getSharingId())
        );

    }

    @Override
    public int getItemCount() {
        return listSharing.size();
    }

    public static class SharingkuViewHolder extends RecyclerView.ViewHolder {
        public TextView judulSharing, ceritaSharing;
        public CircleImageView imageSharing;

        public SharingkuViewHolder(@NonNull View itemView) {
            super(itemView);
            judulSharing = itemView.findViewById(R.id.sharingku_judul);
            ceritaSharing = itemView.findViewById(R.id.description);
            imageSharing = itemView.findViewById(R.id.imageSharing);
        }
    }

    private void openSharingDetails(String sharingId) {
        Intent intent = new Intent(context, Sharingku_detail.class);
        intent.putExtra("sharingId", sharingId);
        intent.putExtra("judul", getTitleBySharingId(sharingId));
        intent.putExtra("cerita", getContentBySharingId(sharingId));
        intent.putExtra("image", getDateBySharingId(sharingId));
        context.startActivity(intent);
    }

    private String getTitleBySharingId(String sharingId) {
        for (Sharingku_model sharing : listSharing) {
            if (sharing.getSharingId().equals(sharingId)) {
                return sharing.getJudul();
            }
        }
        return "";
    }

    private String getContentBySharingId(String sharingId) {
        for (Sharingku_model sharing : listSharing) {
            if (sharing.getSharingId().equals(sharingId)) {
                return sharing.getCerita();
            }
        }
        return "";
    }

    private String getDateBySharingId(String sharingId) {
        for (Sharingku_model sharing : listSharing) {
            if (sharing.getSharingId().equals(sharingId)) {
                return sharing.getImg();
            }
        }
        return "";
    }
}