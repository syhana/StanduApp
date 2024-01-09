package com.example.standu;

import java.io.Serializable;

public class Nutrisi_model implements Serializable {

    private String name;
    private String description;
    private String turl;

    private String detail;
    private String resep_mpasi;
    private String kandungan_vitamin;

    private String bahan;

    private String cara_membuat;

    // Konstruktor kosong dibutuhkan untuk Firebase
    public Nutrisi_model() {
        // Kosong karena konstruktor kosong tidak memerlukan inisialisasi nilai.
    }

    public Nutrisi_model(String name, String description, String turl) {
        this.name = name;
        this.description = description;
        this.turl = turl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTurl() {
        return turl;
    }

    public void setTurl(String turl) {
        this.turl = turl;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getResep_mpasi() {
        return resep_mpasi;
    }

    public void setResep_mpasi(String resep_mpasi) {
        this.resep_mpasi = resep_mpasi;
    }

    public String getKandungan_vitamin() {
        return kandungan_vitamin;
    }

    public void setKandungan_vitamin(String kandungan_vitamin) {
        this.kandungan_vitamin = kandungan_vitamin;
    }

    public String getBahan() {
        return bahan;
    }

    public void setBahan(String bahan) {
        this.bahan = bahan;
    }

    public String getCara_membuat() {
        return cara_membuat;
    }

    public void setCara_membuat(String cara_membuat) {
        this.cara_membuat = cara_membuat;
    }

    @Override
    public String toString() {
        return "NutrisiModel{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", turl='" + turl + '\'' +
                ", detail='" + detail + '\'' +
                ", resep_mpasi='" + resep_mpasi + '\'' +
                ", kandungan_vitamin='" + kandungan_vitamin + '\'' +
                '}';
    }
}

