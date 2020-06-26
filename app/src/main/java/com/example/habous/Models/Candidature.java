package com.example.habous.Models;

public class Candidature {

    private long Id ;
    private int Année ;
    private String Type ;

    public Candidature(int année, String type) {
        Année = année;
        Type = type;
    }

    public Candidature(long id, int année, String type) {
        Id = id;
        Année = année;
        Type = type;
    }
    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public int getAnnée() {
        return Année;
    }

    public void setAnnée(int année) {
        Année = année;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }


}
