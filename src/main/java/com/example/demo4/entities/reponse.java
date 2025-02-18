/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temreclamatione file, choose Tools | Temreclamationes
 * and open the temreclamatione in the editor.
 */
package com.example.demo4.entities;

/**
 *
 * @author asus
 */
public class reponse extends reclamation {
    private int id_reponse;        

private int id_user;
private String commentaire,fullname;
public String name;
public reclamation reclamation;

    public reponse() {
    }

    public reponse(int id_reponse, String name, String commentaire, String fullname) {
        this.id_reponse = id_reponse;


        this.name = name;
        this.commentaire = commentaire;
        this.fullname = fullname;
    }
    public reponse( String name) {


        this.name = name;


    }


    public reponse(int id_reponse, String name, reclamation reclamation, String commentaire, String fullname) {
        this.id_reponse = id_reponse;


        this.name = name;
        this.reclamation = reclamation;
        this.commentaire = commentaire;
        this.fullname = fullname;
    }
    public reponse(  String name, String commentaire, String fullname) {



        this.name = name;

        this.commentaire = commentaire;
        this.fullname = fullname;
    }

    public int getId_reponse() {
        return id_reponse;
    }





    public String getName() {
        return name;
    }

    public reclamation getreclamation() {
        return reclamation;
    }

    public void setId_reponse(int id_reponse) {
        this.id_reponse = id_reponse;
    }





    public void setName(String name) {
        this.name = name;
    }
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getCommentaire() {
        return commentaire;
    }
    public void setReclamation(reclamation reclamation) {
        this.reclamation = reclamation;
    }

    @Override
    public String toString() {
        return "reponse{" + "id_reponse=" + id_reponse +  ", name=" + name +  ", commentaire=" + commentaire +  ", fullname=" + fullname + '}';
    }
    
    
    




}
