package com.example.project.model;

import java.sql.Blob;
import java.util.Date;

public class Utilisateur {
    private String username=null;
    private String email=null;
    private String password=null;
    private Date date=null;
    private Blob avatar=null;
    private  int Id;

    public Utilisateur(int id,String username, String email,String mdp) {
        this.username = username;
        this.password=mdp;
        this.date = new Date();
        this.Id=id;
    }

    public Utilisateur(int id ,String username, String email,Blob pdp) {
        this.username = username;
        this.email = email;
        this.avatar = pdp;
        this.date = new Date();
        this.Id=id;
    }
    public Utilisateur(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.date = new Date();
    }
    public Utilisateur(String username, String email, String password, Date date, Blob pdp) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.date = new Date();
        this.avatar = pdp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPdp(Blob pdp) {
        this.avatar = pdp;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public java.sql.Date getDate() {
        return (java.sql.Date) date;
    }

    public com.mysql.cj.jdbc.Blob getPdp() {
        return (com.mysql.cj.jdbc.Blob) avatar;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
