package com.example.standu;

public class Diaryku_model {
    private String id;
    private String title;
    private String content;
    private String date;

    // Diperlukan konstruktor kosong untuk Firebase
    public Diaryku_model() {
    }

    public Diaryku_model(String id, String title, String content, String date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getId() {
        return id;
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

