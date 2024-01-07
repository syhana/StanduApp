package com.example.standu;

public class ImunisasiAnak_model {
    private String imunisasiId;
    private String namaImunisasi;
    private String tanggalImunisasi;
    private String namaAnak;
    public ImunisasiAnak_model() {
    }

    public ImunisasiAnak_model(String namaAnak, String imunisasiId, String namaImunisasi, String tanggalImunisasi) {
        this.namaAnak = namaAnak;
        this.imunisasiId = imunisasiId;
        this.namaImunisasi = namaImunisasi;
        this.tanggalImunisasi = tanggalImunisasi;
    }

    public String getNamaAnak() {
        return namaAnak;
    }

    public void setNamaAnak(String namaAnak) {
        this.namaAnak = namaAnak;
    }

    public String getImunisasiId() {
        return imunisasiId;
    }

    public void setImunisasiId(String imunisasiId) {
        this.imunisasiId = imunisasiId;
    }

    public String getNamaImunisasi() {
        return namaImunisasi;
    }

    public void setNamaImunisasi(String namaImunisasi) {
        this.namaImunisasi = namaImunisasi;
    }

    public String getTanggalImunisasi() {
        return tanggalImunisasi;
    }

    public void setTanggalImunisasi(String tanggalImunisasi) {
        this.tanggalImunisasi = tanggalImunisasi;
    }
}
