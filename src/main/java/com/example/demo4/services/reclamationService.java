/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temreclamatione file, choose Tools | Temreclamationes
 * and open the temreclamatione in the editor.
 */
package com.example.demo4.services;

//import com.sun.javafx.iio.ImageStorage.ImageType;
import com.example.demo4.entities.reclamation;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.example.demo4.utils.MyDB;
import javafx.collections.ObservableList;

//**************//
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;


/**
 *
 * @author asus
 */
public class reclamationService implements IreclamationService<reclamation> {

    Connection cnx;
    public Statement ste;
    public PreparedStatement pst;

    public reclamationService() {
        cnx = MyDB.getInstance().getCnx();

    }

    @Override
    public void ajouterreclamation(reclamation e) throws SQLException {

        String requete = "INSERT INTO `reclamation` (`name`,`image`,`commentaire`,`updated`) "
                + "VALUES (?,?,?,?);";
        try {
            pst = (PreparedStatement) cnx.prepareStatement(requete);
            pst.setString(1, e.getName());

            pst.setString(2, e.getImage());
            pst.setString(3, e.getCommentaire());
            pst.setDate(4, e.getUpdated());


            pst.executeUpdate();
            System.out.println("ev " + e.getName() + " added successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void modifierreclamation(reclamation e) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temreclamationes.
        String req = "UPDATE reclamation SET name = ?,image=?,commentaire = ?,updated=? where id_reclamation = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, e.getName());

        ps.setString(2, e.getImage());
        ps.setString(3, e.getCommentaire());
        ps.setDate(4, e.getUpdated());


        ps.setInt(5, e.getId_reclamation());
        ps.executeUpdate();
    }

    @Override
    public void supprimerreclamation(reclamation e) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temreclamationes.
        String req = "delete from reclamation where id_reclamation = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, e.getId_reclamation());
        ps.executeUpdate();
        System.out.println("ev with id= " + e.getId_reclamation() + "  is deleted successfully");
    }





    @Override
    public List<reclamation> recupererreclamation() throws SQLException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temreclamationes.

        List<reclamation> reclamation = new ArrayList<>();
        String s = "select * from reclamation";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            reclamation e = new reclamation();
            e.setName(rs.getString("name"));

            e.setImage(rs.getString("Image"));
            e.setCommentaire(rs.getString("commentaire"));
            e.setUpdated(rs.getDate("updated"));



            e.setId_reclamation(rs.getInt("id_reclamation"));

            reclamation.add(e);

        }
        return reclamation;
    }

    public reclamation FetchOneev(int id) {
        reclamation ev = new reclamation();
        String requete = "SELECT * FROM `reclamation` where id_reclamation = " + id;

        try {
            ste = (Statement) cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {

                ev = new reclamation(rs.getInt("id_reclamation"), rs.getString("name"), rs.getString("image"), rs.getString("commentaire"), rs.getDate("updated"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(reclamationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ev;
    }
    public reclamation FetchOneevv(String name) {
        reclamation ev = new reclamation();
        String requete = "SELECT * FROM `reclamation` where name = " + name;

        try {
            ste = (Statement) cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {

                ev = new reclamation(rs.getInt("id_reclamation"), rs.getString("name"), rs.getString("image"), rs.getString("commentaire"), rs.getDate("updated"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(reclamationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ev;
    }

    public ObservableList<reclamation> Fetchevs() {
        ObservableList<reclamation> evs = FXCollections.observableArrayList();
        String requete = "SELECT * FROM `reclamation`";
        try {
            ste = (Statement) cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {
                evs.add(new reclamation(rs.getInt("id_reclamation"), rs.getString("name"), rs.getString("image"), rs.getString("commentaire"), rs.getDate("updated")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(reclamationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return evs;
    }


    

    public ObservableList<reclamation> chercherev(String chaine) {
        String sql = "SELECT * FROM reclamation WHERE (name LIKE ?   ) order by name ";
        //Connection cnx= Maconnexion.getInstance().getCnx();
        String ch = "%" + chaine + "%";
        ObservableList<reclamation> myList = FXCollections.observableArrayList();
        try {

            Statement ste = cnx.createStatement();
            // PreparedStatement pst = myCNX.getCnx().prepareStatement(requete6);
            PreparedStatement stee = cnx.prepareStatement(sql);
            stee.setString(1, ch);


            ResultSet rs = stee.executeQuery();
            while (rs.next()) {
                reclamation e = new reclamation();

                e.setName(rs.getString("name"));

                e.setImage(rs.getString("Image"));
                e.setCommentaire(rs.getString("commentaire"));
                e.setUpdated(rs.getDate("updated"));



                e.setId_reclamation(rs.getInt("id_reclamation"));

                myList.add(e);
                System.out.println("ev trouvé! ");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return myList;
    }

    public List<reclamation> trierev()throws SQLException {
        List<reclamation> reclamation = new ArrayList<>();
        String s = "select * from reclamation order by name ";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            reclamation e = new reclamation();
            e.setName(rs.getString("name"));

            e.setImage(rs.getString("Image"));
            e.setCommentaire(rs.getString("commentaire"));
            e.setUpdated(rs.getDate("updated"));



            e.setId_reclamation(rs.getInt("id_reclamation"));
            reclamation.add(e);
        }
        return reclamation;
    }



}
