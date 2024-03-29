package com.example.standu;

public class Sharingku_model {
    private String sharingId;
    private String judul;
    private String cerita;
    private String img;
    private String userId;

    public Sharingku_model(String sharingId, String judulSharing, String cerita, String img, String userId) {
        this.sharingId = sharingId;
        this.judul = judulSharing;
        this.cerita = cerita;
        this.img = img;
        this.userId = userId;
    }

    public Sharingku_model() {
    }

    public String getSharingId() {
        return sharingId;
    }

    public void setSharingId(String sharingId) {
        this.sharingId = sharingId;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getCerita() {
        return cerita;
    }

    public void setCerita(String cerita) {
        this.cerita = cerita;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
