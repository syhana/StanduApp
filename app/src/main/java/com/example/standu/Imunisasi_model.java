package com.example.standu;

public class Imunisasi_model {
    private String anakId;
    private String namaAnak;
    private String umurAnak;
    private String jkAnak;
    private String bbAnak;
    private String tbAnak;

    public Imunisasi_model() {
    }

    public Imunisasi_model(String anakId, String namaAnak, String umurAnak, String jkAnak, String bbAnak, String tbAnak) {
        this.anakId = anakId;
        this.namaAnak = namaAnak;
        this.umurAnak = umurAnak;
        this.jkAnak = jkAnak;
        this.bbAnak = bbAnak;
        this.tbAnak = tbAnak;
    }

    public String getAnakId() {
        return anakId;
    }

    public String getNamaAnak() {
        return namaAnak;
    }

    public String getUmurAnak() {
        return umurAnak;
    }

    public String getJkAnak() {
        return jkAnak;
    }

    public String getBbAnak() {
        return bbAnak;
    }

    public String getTbAnak() {
        return tbAnak;
    }
}
