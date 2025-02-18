/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temreclamatione file, choose Tools | Temreclamationes
 * and open the temreclamatione in the editor.
 */
package com.example.demo4;

import com.example.demo4.entities.User;
import com.example.demo4.entities.reclamation;
import com.example.demo4.entities.reponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import com.example.demo4.services.reclamationService;
import com.example.demo4.services.reponseService;
/**
 * FXML Controller class
 *
 * @author asus
 */
public class reclamationController implements Initializable {
    @FXML
    private Button modifierPartBtn;
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
    @FXML



    int idev;
    @FXML
    private Label nomevLabel;
    @FXML
    private Label typeevLabel;
    @FXML
    private Label descriptionevLabel;
    @FXML
    private Label updatedevLabel;
    @FXML
    private Label createdevLabel;

    @FXML
    private Label nb_reponsesLabel;
    
    User u=new User();

    @FXML
    private TextField idevF;
    @FXML
    private TextField iduserF;
    
    reclamationService Ev=new reclamationService();
    @FXML
    private ImageView imageview;
    @FXML
    private Label reponseComplet;
    @FXML
    private TextField idPartField;
    @FXML
    private Button annulerButton;
    @FXML
    private Button likeButton;
     @FXML
    private Button deslikeButton;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        idevF.setVisible(false);





                

    }    
    private reclamation eve=new reclamation();
    
    public void setreclamation(reclamation e) {
        this.eve=e;
        nomevLabel.setText(e.getName());

        descriptionevLabel.setText(e.getCommentaire());
        updatedevLabel.setText(String.valueOf(e.getUpdated()));


        idevF.setText(String.valueOf(e.getName()));
        iduserF.setText(String.valueOf(1));
         String path = e.getImage();
         File file=new File(path);
         Image img = new Image(file.toURI().toString());
         imageview.setImage(img);

    }
    public void setIdev(int idev){
        this.idev=idev;
    }


    @FXML
    private void repondreev(MouseEvent ev) throws SQLException {


        reponse p=new reponse(idevF.getText(),descriptionevField.getText(),fullnameevField.getText());
        
        Ps.ajouterreponse(p);

        idPartField.setText(String.valueOf(27));
        annulerButton.setVisible(true);
       
        
        repondreevButton.setVisible(false);
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("index2.fxml")));
            idevF.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }



        }
    
    public void arreterev()
    {
        repondreevButton.setVisible(false);
        reponseComplet.setVisible(true);
    }

    @FXML
    private void annulerreponse(ActionEvent ev) throws SQLException {
        reponse p=new reponse();
        p.setId_reponse(Integer.parseInt(idPartField.getText()));
        Ps.Deletereponse(p);
        repondreevButton.setVisible(true);
        annulerButton.setVisible(false);
        
    }
    
    
}
