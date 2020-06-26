package com.example.habous.Models;

public class TirageAuSort {
    private long Id;
    private  int NombreRt;
    private  int NombreAt;
    private  String Responsables;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public int getNombreRt() {
        return NombreRt;
    }

    public void setNombreRt(int nombreRt) {
        NombreRt = nombreRt;
    }

    public int getNombreAt() {
        return NombreAt;
    }

    public void setNombreAt(int nombreAt) {
        NombreAt = nombreAt;
    }

    public String getResponsables() {
        return Responsables;
    }

    public void setResponsables(String responsables) {
        Responsables = responsables;
    }

    public String getLieu() {
        return Lieu;
    }

    public void setLieu(String lieu) {
        Lieu = lieu;
    }

    public String getDateTirage() {
        return DateTirage;
    }

    public void setDateTirage(String dateTirage) {
        DateTirage = dateTirage;
    }

    public String Lieu;
    public String DateTirage;

    public TirageAuSort(int nombreRt, int nombreAt, String responsables, String lieu, String dateTirage) {
        NombreRt = nombreRt;
        NombreAt = nombreAt;
        Responsables = responsables;
        Lieu = lieu;
        DateTirage = dateTirage;
    }

    public TirageAuSort(long id, int nombreRt, int nombreAt, String responsables, String lieu, String dateTirage) {
        Id = id;
        NombreRt = nombreRt;
        NombreAt = nombreAt;
        Responsables = responsables;
        Lieu = lieu;
        DateTirage = dateTirage;
    }
}
