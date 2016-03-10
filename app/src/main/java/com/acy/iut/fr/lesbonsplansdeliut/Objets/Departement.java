package com.acy.iut.fr.lesbonsplansdeliut.Objets;

/**
 * Created by mesagoul on 01/03/2016.
 */
public class Departement {
    private String nom;
    private int id;

    public Departement(int id,String nom){
        this.id = id;
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
