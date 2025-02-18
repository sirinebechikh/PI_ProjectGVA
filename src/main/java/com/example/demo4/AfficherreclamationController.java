/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temreclamatione file, choose Tools | Temreclamationes
 * and open the temreclamatione in the editor.
 */
package com.example.demo4;

import com.example.demo4.entities.reclamation;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import com.example.demo4.services.reclamationService;

/**
 * FXML Controller class
 *
 * @author asus
 */
public class AfficherreclamationController implements Initializable {

    @FXML
    private GridPane gridev;

    reclamationService ab=new reclamationService();
    @FXML
    private TextField chercherevField;
    @FXML
    private Button ajouter;
    @FXML
    private Button mailButton;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        afficherreclamation();
               
    }    


    @FXML
    private void ajouterreclamation(ActionEvent ev) {
      try {
            //navigation
            Parent loader = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
            chercherevField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @FXML
    private void Stat(ActionEvent ev) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(getClass().getResource("Statistics.fxml"));
            chercherevField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void afficherreclamation(){
         try {
            List<reclamation> reclamation = ab.recupererreclamation();
            gridev.getChildren().clear();
            int row = 0;
            int column = 0;
            for (int i = 0; i < reclamation.size(); i++) {
                //chargement dynamique d'une interface
                FXMLLoader loader = new FXMLLoader(getClass().getResource("reclamation.fxml"));
                AnchorPane pane = loader.load();
               
                //passage de parametres
                reclamationController controller = loader.getController();
                controller.setreclamation(reclamation.get(i));
                controller.setIdev(reclamation.get(i).getId_reclamation());
                gridev.add(pane, column, row);
                column++;
                if (column > 1) {
                    column = 0;
                    row++;
                }

            }
        } catch (SQLException | IOException ex) {
            System.out.println(ex.getMessage());
        }   
    }

    @FXML
    private void rechercherreclamation(KeyEvent ev) {
        try {
            List<reclamation> reclamation = ab.chercherev(chercherevField.getText());
            gridev.getChildren().clear();
            int row = 0;
            int column = 0;
            for (int i = 0; i < reclamation.size(); i++) {
                //chargement dynamique d'une interface
                FXMLLoader loader = new FXMLLoader(getClass().getResource("reclamation.fxml"));
                AnchorPane pane = loader.load();         
                //passage de parametres
                reclamationController controller = loader.getController();
                controller.setreclamation(reclamation.get(i));
                controller.setIdev(reclamation.get(i).getId_reclamation());
                gridev.add(pane, column, row);
                column++;
                if (column > 1) {
                    column = 0;
                    row++;
                }

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }   
    }



    @FXML
    private void trierreclamation(ActionEvent ev) throws SQLException {
        try {
            List<reclamation> reclamation = ab.trierev();
            gridev.getChildren().clear();
            int row = 0;
            int column = 0;
            for (int i = 0; i < reclamation.size(); i++) {
                //chargement dynamique d'une interface
                FXMLLoader loader = new FXMLLoader(getClass().getResource("reclamation.fxml"));
                AnchorPane pane = loader.load();      
                //passage de parametres
                reclamationController controller = loader.getController();
                controller.setreclamation(reclamation.get(i));
                controller.setIdev(reclamation.get(i).getId_reclamation());
                gridev.add(pane, column, row);
                column++;
                if (column > 1) {
                    column = 0;
                    row++;
                }

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
 
    }
    
}
