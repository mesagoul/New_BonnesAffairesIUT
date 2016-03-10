package com.acy.iut.fr.lesbonsplansdeliut.Objets;

/**
 * Created by cadavidj on 01/03/2016.
 */
public class Utilisateur {
    private int id;
    private int id_departement;
    private String nom;
    private String prenom;
    private String mail;
    private String tel;
    private String motdepasse;
    private String login;

    public int getId() {
        return id;
    }
    /*GET/SET*/

    public void setId(int id) {
        this.id = id;
    }

    public int getId_departement() {
        return id_departement;
    }

    public void setId_departement(int id_departement) {
        this.id_departement = id_departement;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Utilisateur(int id_departement, String nom, String prenom, String mail, String tel, String motdepasse, String login) {
        this.id_departement = id_departement;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.tel = tel;
        this.motdepasse = motdepasse;
        this.login = login;
    }
    public Utilisateur(){}
}
