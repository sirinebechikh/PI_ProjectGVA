/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temreclamatione file, choose Tools | Temreclamationes
 * and open the temreclamatione in the editor.
 */
package com.example.demo4.entities;

import java.sql.Date;

/**
 *
 * @author asus
 */
public class reclamation {

   
        private int id_reclamation;



    private String name,image,commentaire;
    private Date updated;


    public reclamation() {
    }

    public reclamation(int id_reclamation, String name, String image, String commentaire, Date updated) {
        this.id_reclamation = id_reclamation;



        this.name = name;

        this.image = image;
        this.commentaire = commentaire;
        this.updated = updated;

    }
    public reclamation(String name, String image, String commentaire, Date updated ) {



        this.name = name;

        this.image = image;
        this.commentaire = commentaire;
        this.updated = updated;

    }
    
    
     public reclamation(int id_reclamation, String name, String image, String commentaire) {
        this.id_reclamation = id_reclamation;



        this.name = name;

        this.image = image;
        this.commentaire = commentaire;
        
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






    @Override
    public String toString() {
        return "reclamation{" + "id_reclamation=" + id_reclamation+  ", name=" + name + ", image=" + image + ", commentaire=" + commentaire + ", updated=" + updated + '}';
    }
    
    
    
    
    
    
}
