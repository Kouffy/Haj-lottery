package com.example.habous.Models;

import android.text.Editable;

public class Utilisateur
{
    private long Id ;
    private String Nom ;
    private String Prenom ;
    private String Pdp ;
    private String Datenaissance ;
    private String Sexe ;
    private String Cin;
    private String Role ;
    private String Login ;
    private String SessionToken ;

    public Utilisateur(String nom, String prenom, String pdp, String datenaissance, String sexe, String cin, String role, String login, String password) {
        Nom = nom;
        Prenom = prenom;
        Pdp = pdp;
        Datenaissance = datenaissance;
        Sexe = sexe;
        Cin = cin;
        Role = role;
        Login = login;
        Password = password;
    }

    private String Password ;


    public Utilisateur(long id, String nom, String prenom, String pdp, String datenaissance, String sexe, String cin, String role, String login, String password,String sessionToken) {
        Id = id;
        Nom = nom;
        Prenom = prenom;
        Pdp = pdp;
        Datenaissance = datenaissance;
        Sexe = sexe;
        Cin = cin;
        Role = role;
        Login = login;
        Password = password;
        SessionToken = sessionToken;
    }



    public String getSessionToken() {
        return SessionToken;
    }

    public void setSessionToken(String sessionToken) {
        SessionToken = sessionToken;
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

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }




}
