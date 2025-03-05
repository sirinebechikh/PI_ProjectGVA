/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temreclamatione file, choose Tools | Temreclamationes
 * and open the temreclamatione in the editor.
 */
package com.example.demo4.Services;

//import com.sun.javafx.iio.ImageStorage.ImageType;

import com.example.demo4.Entities.reclamation;
import com.example.demo4.db.MyDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        String requete = "INSERT INTO `reclamation` (`name`, `image`, `commentaire`, `updated`, `statut`, `email`, `id_user`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?);";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setString(1, e.getName());
            pst.setString(2, e.getImage());
            pst.setString(3, e.getCommentaire());
            pst.setDate(4, e.getUpdated());
            pst.setString(5, e.getStatut());
            pst.setString(6, e.getEmail());
            pst.setInt(7, e.getId_user()); // Ajout de l'id_user
            pst.executeUpdate();
            System.out.println("Réclamation ajoutée avec succès");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void modifierreclamation(reclamation e) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temreclamationes.
        String req = "UPDATE reclamation SET name = ?,image=?,commentaire = ?,updated=?,statut = ?,email = ? where id_reclamation = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, e.getName());

        ps.setString(2, e.getImage());
        ps.setString(3, e.getCommentaire());
        ps.setDate(4, e.getUpdated());
        ps.setString(5, e.getStatut());
        ps.setString(6, e.getEmail());


        ps.setInt(7, e.getId_reclamation());
        ps.executeUpdate();
    }
    public void updateReclamationStatus(int idReclamation, String newStatus) throws SQLException {
        String query = "UPDATE reclamation SET statut = ? WHERE id_reclamation = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setString(1, newStatus); // Définir le nouveau statut
            pstmt.setInt(2, idReclamation); // Définir l'ID de la réclamation
            pstmt.executeUpdate(); // Exécuter la mise à jour
            System.out.println("Statut de la réclamation mis à jour avec succès.");
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la mise à jour du statut de la réclamation : " + ex.getMessage());
            throw ex; // Propager l'exception pour la gestion des erreurs
        }
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
    public List<reclamation> recupererreclamation(int id_user) throws SQLException {
        List<reclamation> reclamations = new ArrayList<>();
        String s = "SELECT * FROM reclamation WHERE id_user = ?";
        try {
            pst = cnx.prepareStatement(s);
            pst.setInt(1, id_user);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                reclamation e = new reclamation();
                e.setId_reclamation(rs.getInt("id_reclamation"));
                e.setName(rs.getString("name"));
                e.setImage(rs.getString("image"));
                e.setCommentaire(rs.getString("commentaire"));
                e.setUpdated(rs.getDate("updated"));
                e.setStatut(rs.getString("statut"));
                e.setEmail(rs.getString("email"));
                e.setId_user(rs.getInt("id_user"));
                reclamations.add(e);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return reclamations;
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
            e.setEmail(rs.getString("email"));
            e.setUpdated(rs.getDate("updated"));
            e.setStatut(rs.getString("statut"));



            e.setId_reclamation(rs.getInt("id_reclamation"));

            reclamation.add(e);

        }
        return reclamation;
    }



    public reclamation FetchOneevv(String name) {
        reclamation ev = null; // Initialiser à null pour gérer les cas où aucune réclamation n'est trouvée
        String requete = "SELECT * FROM reclamation WHERE name = ?"; // Utiliser un paramètre

        try {
            pst = cnx.prepareStatement(requete);
            pst.setString(1, name); // Définir la valeur du paramètre
            ResultSet rs = pst.executeQuery();

            if (rs.next()) { // Vérifier si un résultat a été trouvé
                ev = new reclamation(
                        rs.getInt("id_reclamation"),
                        rs.getString("name"),
                        rs.getString("image"),
                        rs.getString("commentaire"),
                        rs.getDate("updated"),
                        rs.getString("statut"),
                        rs.getString("email")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(reclamationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ev; // Retourner null si aucune réclamation n'est trouvée
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
                e.setEmail(rs.getString("email"));
                e.setUpdated(rs.getDate("updated"));
                e.setStatut(rs.getString("statut"));



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
            e.setEmail(rs.getString("email"));

            e.setStatut(rs.getString("statut"));


            e.setId_reclamation(rs.getInt("id_reclamation"));
            reclamation.add(e);
        }
        return reclamation;
    }
    public String GenerateQrev(reclamation ev) throws FileNotFoundException, IOException {
        // Construire la chaîne d'informations avec tous les champs de la réclamation
        String evInfo = "ID de la réclamation: " + ev.getId_reclamation() + "\n" +
                "Nom d'utilisateur: " + ev.getName() + "\n" +
                "Email: " + ev.getEmail() + "\n" +
                "Date: " + ev.getUpdated() + "\n" +
                "Commentaire de la réclamation: " + ev.getCommentaire() + "\n" +
                "Statut: " + ev.getStatut() + "\n" +
                "Lien vers l'image: " + ev.getImage() + "\n";

        // Générer le QR code
        ByteArrayOutputStream out = QRCode.from(evInfo).to(ImageType.JPG).stream();
        String filename = ev.getName() + "_QrCode.jpg";
        File f = new File("C:\\xamp\\htdocs\\xchangex\\" + filename);
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(out.toByteArray());
        fos.flush();

        System.out.println("QR code généré avec succès");
        return filename;
    }


}
