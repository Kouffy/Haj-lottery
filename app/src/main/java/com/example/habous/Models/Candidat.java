package com.example.habous.Models;

public class Candidat {
    private long Id ;
    private String Nom ;
    private String Prenom ;
    private String Pdp ;
    private String Datenaissance ;
    private String Code ;
    private  String Email;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public String getPdp() {
        return Pdp;
    }

    public void setPdp(String pdp) {
        Pdp = pdp;
    }

    public String getDatenaissance() {
        return Datenaissance;
    }

    public void setDatenaissance(String datenaissance) {
        Datenaissance = datenaissance;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getSexe() {
        return Sexe;
    }

    public void setSexe(String sexe) {
        Sexe = sexe;
    }

    public String getCin() {
        return Cin;
    }

    public void setCin(String cin) {
        Cin = cin;
    }

    public String getDocuments() {
        return Documents;
    }

    public void setDocuments(String documents) {
        Documents = documents;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public long getIdcandidature() {
        return Idcandidature;
    }

    public void setIdcandidature(long idcandidature) {
        Idcandidature = idcandidature;
    }

    public int getNbcandidature() {
        return Nbcandidature;
    }

    public void setNbcandidature(int nbcandidature) {
        Nbcandidature = nbcandidature;
    }

    public Candidat(long id, String nom, String prenom, String pdp, String datenaissance, String code, String sexe, String cin, String documents, String tel, long idcandidature, int nbcandidature,String email) {
        Id = id;
        Nom = nom;
        Prenom = prenom;
        Pdp = pdp;
        Datenaissance = datenaissance;
        Code = code;
        Sexe = sexe;
        Cin = cin;
        Documents = documents;
        Tel = tel;
        Idcandidature = idcandidature;
        Nbcandidature = nbcandidature;
        Email = email;
    }
    private String Sexe ;
    private String Cin;
    private String Documents;
    private String Tel ;
    private long Idcandidature ;
    private int Nbcandidature ;
}
