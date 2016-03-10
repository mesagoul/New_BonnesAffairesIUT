package com.acy.iut.fr.lesbonsplansdeliut.Objets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boinnarr on 01/03/2016.
 */
public class Objet {
    private int id;
    private int id_utilisateur;
    private int id_categorie;
    private String nom;
    private String description;
    private List<String> url_photo1 = new ArrayList<>();
    private double prix;


    public Objet(int id, double prix, String description, String nom, int id_categorie, int id_utilisateur) {
        this.id = id;
        this.prix = prix;
        this.description = description;
        this.nom = nom;
        this.id_categorie = id_categorie;
        this.id_utilisateur = id_utilisateur;
    }

    public Objet(int id_utilisateur, int id_categorie, String nom, String description, double prix) {
        this.id_utilisateur = id_utilisateur;
        this.id_categorie = id_categorie;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
    }

    public Objet(String nom, String description, double prix) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public List<String> getUrl_photo1() {
        return url_photo1;
    }

    public void setUrl_photo1(List<String> url_photo1) {
        this.url_photo1 = url_photo1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId_categorie() {
        return id_categorie;
    }

    public void setId_categorie(int id_categorie) {
        this.id_categorie = id_categorie;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }
}
