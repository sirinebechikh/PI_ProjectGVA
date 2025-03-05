/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temreclamatione file, choose Tools | Temreclamationes
 * and open the temreclamatione in the editor.
 */
package com.example.demo4.Entities;

import java.sql.Date;

/**
 *
 * @author asus
 */
public class reclamation {


    private int id_reclamation , id_user;



    private String name,image,commentaire,statut,email;
    private Date updated;


    public reclamation() {
    }

    public reclamation(int id_reclamation, String name, String image, String commentaire, Date updated, String statut, String email,int id_user) {
        this.id_reclamation = id_reclamation;



        this.name = name;

        this.image = image;
        this.commentaire = commentaire;
        this.email = email;
        this.updated = updated;
        this.statut = statut;
        this.id_user = id_user;

    }
    public reclamation(int id_reclamation, String name, String image, String commentaire, Date updated, String statut, String email) {
        this.id_reclamation = id_reclamation;



        this.name = name;

        this.image = image;
        this.commentaire = commentaire;
        this.email = email;
        this.updated = updated;
        this.statut = statut;

    }
    public reclamation(String name, String image, String commentaire, Date updated, String statut, String email, int id_user ) {



        this.name = name;
        this.email = email;
        this.image = image;
        this.commentaire = commentaire;
        this.updated = updated;
        this.statut = statut;
        this.id_user = id_user;

    }
    public reclamation(String name, String image, String commentaire, Date updated, String statut, String email) {



        this.name = name;
        this.email = email;
        this.image = image;
        this.commentaire = commentaire;
        this.updated = updated;
        this.statut = statut;

    }


    public reclamation(int id_reclamation, String name, String image, String commentaire, String statut, String email) {
        this.id_reclamation = id_reclamation;



        this.name = name;
        this.email = email;
        this.image = image;
        this.commentaire = commentaire;
        this.statut = statut;

    }
    // Getters et Setters
    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }


    //****************** getters ****************

    public int getId_reclamation() {
        return id_reclamation;
    }

    public String getName() {
        return name;
    }



    public String getImage() {
        return image;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Date getUpdated() {
        return updated;
    }



    //****************** setters ****************

    public void setId_reclamation(int id_reclamation) {
        this.id_reclamation = id_reclamation;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setImage(String image) {
        this.image = image;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    public String getStatut() {
        return statut;
    }
    public void setStatut(String statut) {
        this.statut = statut;
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "reclamation{" + "id_reclamation=" + id_reclamation+  ", name=" + name + ", image=" + image + ", commentaire=" + commentaire + ", updated=" + updated + ", statut=" + statut +  ", email=" + email + '}';
    }






}
