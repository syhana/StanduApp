package com.example.standu;

public class User_data {
    String userId, nama, user, mail, pass;

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public User_data(String userId, String nama, String user, String mail, String pass) {
        this.userId = userId;
        this.nama = nama;
        this.user = user;
        this.mail = mail;
        this.pass = pass;
    }

    public User_data() {
    }
}
