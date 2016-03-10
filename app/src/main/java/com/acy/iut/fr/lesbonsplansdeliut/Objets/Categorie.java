package com.acy.iut.fr.lesbonsplansdeliut.Objets;

/**
 * Created by boinnarr on 01/03/2016.
 */
public class Categorie {
    private int id;
    private String nom;

    public Categorie(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public int getId_categorie() {
        return id;
    }

    public void setId_categorie(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
