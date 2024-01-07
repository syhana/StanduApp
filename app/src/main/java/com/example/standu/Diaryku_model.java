package com.example.standu;

public class Diaryku_model {
    private String diaryId;
    private String title;
    private String content;
    private String date;

    public Diaryku_model() {
    }

    public Diaryku_model(String diaryId, String title, String content, String date) {
        this.diaryId = diaryId;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getDiaryId() {
        return diaryId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
}

