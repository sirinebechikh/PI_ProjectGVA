/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temreclamatione file, choose Tools | Temreclamationes
 * and open the temreclamatione in the editor.
 */
package com.example.demo4;

import com.example.demo4.entities.reclamation;
import com.example.demo4.entities.reponse;
import com.example.demo4.services.reclamationService;
import com.example.demo4.services.reponseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * FXML Controller class
 *
 * @author asus
 */
public class AfficherreponseFrontController implements Initializable {

    @FXML
    private TableView<reponse> tablereponse;
     reclamationService ab=new reclamationService();
    @FXML
    private TableColumn<reponse, Integer> iduserTv;
    @FXML
    private TableColumn<reponse, String> idevTv;
    @FXML
    private TableColumn<reponse, Date> datePartTv;
    @FXML
    private TableColumn<reponse, String> descriptionevTv;
    @FXML
    private TableColumn<reponse, String> fullnameevTv;
    @FXML
    private Button modifierPartBtn;
    @FXML

    private Button supprimerPartBtn;
    @FXML
    private TextField descriptionevField;
    @FXML
    private TextField fullnameevField;
     @FXML
    private Button ajouter;
    @FXML
    private TextField idread;
    @FXML
    private TextField iduserField;
    @FXML
    private TextField idevField;
    @FXML
    private DatePicker datepartField;
    @FXML
    private TextField chercherevField;
    @FXML
    private Button repondreevButton;
    reponseService Ps=new reponseService();
    @FXML
    private TextField datepartField1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        getComment();
    }    
     @FXML
    private void ajouterreclamation(ActionEvent ev) {
      try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));
            chercherevField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @FXML
    private void rechercherreclamation(KeyEvent ev) {
        try {
            List<reclamation> reclamation = ab.chercherev(chercherevField.getText());
            
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
                

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }   
    }
    @FXML
    private void modifierreponse(ActionEvent ev) throws SQLException {
        
         reponse pa = new reponse();
        pa.setId_reponse(Integer.valueOf(idread.getText()));
        pa.setName(String.valueOf(idevField.getText()));


        pa.setCommentaire(descriptionevField.getText());

        pa.setFullname(fullnameevField.getText());
        //pa.setCreated(datepartField.getText());
       
        Ps.modifierreponse(pa);
        resetPart();
        getComment();
           
        
    }
    @FXML
    private void repondreev(MouseEvent ev) throws SQLException {











        getComment();







    }

    @FXML
    private void supprimerreponse(ActionEvent ev) {
         reponse p = tablereponse.getItems().get(tablereponse.getSelectionModel().getSelectedIndex());
      
        try {
            Ps.Deletereponse(p);
        } catch (SQLException ex) {
            Logger.getLogger(AjouterreclamationController.class.getName()).log(Level.SEVERE, null, ex);
        }   
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information ");
        alert.setHeaderText("reponse delete");
        alert.setContentText("reponse deleted successfully!");
        alert.showAndWait();
        getComment();
     
    }
    @FXML
    private void afficherreclamation(ActionEvent ev) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("index.fxml")));
            modifierPartBtn.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void choisirreponse(MouseEvent ev)  throws IOException {

        reponse part = tablereponse.getItems().get(tablereponse.getSelectionModel().getSelectedIndex());
        
        idread.setText(String.valueOf(part.getId_reponse()));
        idevField.setText(String.valueOf(part.getName()));


        descriptionevField.setText(String.valueOf(part.getCommentaire()));
        fullnameevField.setText(String.valueOf(part.getFullname()));
        //datepartField.setValue((part.getCreated()));
        
    }


    public void getReponse(){
        try {


            // TODO
            List<reponse> part = Ps.recupererReponse();
            ObservableList<reponse> olp = FXCollections.observableArrayList(part);
            tablereponse.setItems(olp);

            idevTv.setCellValueFactory(new PropertyValueFactory<>("name"));

            descriptionevTv.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
            fullnameevTv.setCellValueFactory(new PropertyValueFactory<>("fullname"));
            // this.delete();
        } catch (SQLException ex) {
            System.out.println("error" + ex.getMessage());
        }
    }
    public void getComment(){
        try {


            // TODO
            List<reponse> part = Ps.recupererComment();
            ObservableList<reponse> olp = FXCollections.observableArrayList(part);
            tablereponse.setItems(olp);

            idevTv.setCellValueFactory(new PropertyValueFactory<>("name"));

            descriptionevTv.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
            fullnameevTv.setCellValueFactory(new PropertyValueFactory<>("fullname"));
            // this.delete();
        } catch (SQLException ex) {
            System.out.println("error" + ex.getMessage());
        }
    }
    
    public void resetPart() {
        idread.setText("");
        idevField.setText("");
        iduserField.setText("");

        
    }
   
    
}


 