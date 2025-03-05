/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temreclamatione file, choose Tools | Temreclamationes
 * and open the temreclamatione in the editor.
 */
package com.example.demo4.Services;

import com.example.demo4.Entities.User;
import com.example.demo4.Entities.reclamation;
import com.example.demo4.Entities.reponse;
import com.example.demo4.db.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//**************//


/**
 *
 * @author asus
 */
public class reponseService {

    Connection cnx;
    public Statement ste;
    public PreparedStatement pst;

    public reponseService() {

        cnx = MyDB.getInstance().getCnx();
    }

    public void ajouterreponse(reponse p) {
        User U = new User();
        reclamationService es = new reclamationService();
        String requete = "INSERT INTO `reponse` (`name` ,`commentaire`,`fullname` , `rating`) VALUES(?,?,?,?) ;";

        try {
            reclamation tempev = es.FetchOneevv(p.getName());
            System.out.println("before" + tempev);

            es.modifierreclamation(tempev);
            String new_id = tempev.getName();
            p.setReclamation(tempev);
            System.out.println("after" + tempev);

            pst = (PreparedStatement) cnx.prepareStatement(requete);

            pst.setString(1, p.getName());
            pst.setString(2, p.getCommentaire());
            pst.setString(3, p.getFullname());
            pst.setDouble(4, p.getRating());

            pst.executeUpdate();

            System.out.println("reponse with id ev = " + p.getName() + " is added successfully");

        } catch (SQLException ex) {
            System.out.println("error in adding reponse");
            Logger.getLogger(reponseService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<reponse> recupererReponse() throws SQLException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temreclamationes.
        reponse dernierCommentaire = null;

        List<reponse> particip = new ArrayList<>();
        String s = "SELECT * FROM reponse WHERE id_reponse = (SELECT MAX(id_reponse) FROM reponse)";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            reponse pa = new reponse();
            pa.setId_reponse(rs.getInt("id_reponse"));

            pa.setName(rs.getString("name"));

            pa.setCommentaire(rs.getString("commentaire"));
            pa.setFullname(rs.getString("fullname"));

            particip.add(pa);

        }
        return particip;
    }
    public List<reponse> recupererComment() throws SQLException {
        List<reponse> particip = new ArrayList<>();
        String s = "select * from reponse";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            reponse pa = new reponse();
            pa.setId_reponse(rs.getInt("id_reponse"));
            pa.setName(rs.getString("name"));
            pa.setCommentaire(rs.getString("commentaire"));
            pa.setFullname(rs.getString("fullname"));
            pa.setRating(rs.getDouble("rating")); // Assurez-vous de récupérer le rating
            particip.add(pa);
        }
        return particip;
    }



    public reponse FetchOneRes(int id) throws SQLException {
        reponse r = new reponse();
        String requete = "SELECT * FROM `reponse` where id_reponse=" + id;

        try {
            ste = (Statement) cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {

                r = new reponse(rs.getInt("id_reponse"), rs.getString("name"), rs.getString("commentaire"), rs.getString("fullname"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(reclamationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    public void Deletereponse(reponse p) throws SQLException {
        reclamationService es = new reclamationService();
        reponseService rs = new reponseService();

        reponse r = rs.FetchOneRes(p.getId_reponse());

        String requete = "delete from reponse where id_reponse=" + p.getId_reponse();
        try {
            reclamation tempev = es.FetchOneevv(r.getName());
            System.out.println("before" + tempev);

            es.modifierreclamation(tempev);
            System.out.println("after" + tempev);
            pst = (PreparedStatement) cnx.prepareStatement(requete);
            //pst.setInt(1, id);

            pst.executeUpdate();
            System.out.println("reponse with id=" + p.getId_reponse() + " is deleted successfully");
        } catch (SQLException ex) {
            System.out.println("error in delete reponse " + ex.getMessage());
        }
    }

    public void modifierreponse(reponse p) throws SQLException {
        String req = "UPDATE reponse SET name = ?, commentaire = ?, fullname = ? where id_reponse = ?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, p.getName());
        ps.setString(2, p.getCommentaire());
        ps.setString(3, p.getFullname());

        ps.setInt(4, p.getId_reponse());

        ps.executeUpdate();
    }
    public void modifierreponseFront(reponse p) throws SQLException {
        String req = "UPDATE reponse SET rating = ? where id_reponse = ?";
        PreparedStatement ps = cnx.prepareStatement(req);


        ps.setDouble(1, p.getRating());
        ps.setInt(2, p.getId_reponse());

        ps.executeUpdate();
    }


}
