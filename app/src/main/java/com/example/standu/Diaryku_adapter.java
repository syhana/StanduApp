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

public class Diaryku_adapter extends RecyclerView.Adapter<Diaryku_adapter.DiarykuViewHolder> {

    private List<Diaryku_model> diaries;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public Diaryku_adapter(List<Diaryku_model> diaries, Context context) {
        this.diaries = diaries;
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
    public DiarykuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_diaryku_item, parent, false);
        return new DiarykuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiarykuViewHolder holder, int position) {
        Diaryku_model diary = diaries.get(position);

        holder.textTitle.setText(diary.getTitle());
        holder.textDate.setText(diary.getDate());
        holder.textContent.setText(diary.getContent());

        holder.itemView.setOnClickListener(view ->
                openDiaryDetails(diary.getDiaryId())
        );
    }

    @Override
    public int getItemCount() {
        return diaries.size();
    }

    public static class DiarykuViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, textDate, textContent;

        public DiarykuViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            textDate = itemView.findViewById(R.id.text_date);
            textContent = itemView.findViewById(R.id.text_content);
        }
    }

    private void openDiaryDetails(String diaryId) {
        Intent intent = new Intent(context, Diaryku_detail.class);
        intent.putExtra("diaryId", diaryId);
        intent.putExtra("title", getTitleByDiaryId(diaryId));
        intent.putExtra("content", getContentByDiaryId(diaryId));
        intent.putExtra("date", getDateByDiaryId(diaryId));
        context.startActivity(intent);
    }

    // Helper methods to get title, content, and date by diaryId
    private String getTitleByDiaryId(String diaryId) {
        for (Diaryku_model diary : diaries) {
            if (diary.getDiaryId().equals(diaryId)) {
                return diary.getTitle();
            }
        }
        return ""; // or null, depending on your preference
    }

    private String getContentByDiaryId(String diaryId) {
        for (Diaryku_model diary : diaries) {
            if (diary.getDiaryId().equals(diaryId)) {
                return diary.getContent();
            }
        }
        return ""; // or null, depending on your preference
    }

    private String getDateByDiaryId(String diaryId) {
        for (Diaryku_model diary : diaries) {
            if (diary.getDiaryId().equals(diaryId)) {
                return diary.getDate();
            }
        }
        return ""; // or null, depending on your preference
    }

}
