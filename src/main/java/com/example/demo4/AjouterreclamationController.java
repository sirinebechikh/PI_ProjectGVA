/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temreclamatione file, choose Tools | Temreclamationes
 * and open the temreclamatione in the editor.
 */
package com.example.demo4;

import com.example.demo4.entities.User;
import com.example.demo4.entities.reclamation;
import com.example.demo4.entities.reponse;
import com.example.demo4.services.reclamationService;
import com.example.demo4.services.reponseService;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;















/**
 * FXML Controller class
 *
 * @author asus
 */
public class AjouterreclamationController implements Initializable {

    @FXML
    private TextField descriptionevField;
    @FXML
    private DatePicker updatedevField;


    @FXML
    private TextField imageevField;
    @FXML
    private TextField nameField;


  
    @FXML
    private TableView<reclamation> reclamationTv;
    @FXML
    private TableColumn<reclamation, String> nomevTv;

    @FXML
    private TableColumn<reclamation, String> imageevTv;
    @FXML
    private TableColumn<reclamation, String> updatedevTv;

    @FXML
    private TableColumn<reclamation, String> descriptionevTv;




    

 
    ObservableList<reclamation> evs;
    reclamationService Ev=new reclamationService();
    reponseService Pservice =new reponseService();

    
    @FXML
    private TextField idmodifierField;

    @FXML
    private ImageView imageview;
    @FXML
    private TextField rechercher;




    /**
     * Initializes the controller class.
     */
@Override
public void initialize(URL url, ResourceBundle rb) {



    //idLabel.setText("");
    getevs(); 
}





    @FXML
    private void navigue(ActionEvent ev) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("index2Front.fxml")));
            rechercher.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
     private boolean NoDate() {
         LocalDate currentDate = LocalDate.now();     
         LocalDate myDate = updatedevField.getValue();
         int comparisonResult = myDate.compareTo(currentDate);      
         boolean test = true;
        if (comparisonResult < 0) {
        // myDate est antérieure à currentDate
        test = true;
        } else if (comparisonResult > 0) {
         // myDate est postérieure à currentDate
         test = false;
        }
        return test;
    }
    @FXML
    private void ajouterreclamation(ActionEvent ev) {

        int part = 0;
        if ((nameField.getText().length() == 0) || (imageevField.getText().length() == 0) || (descriptionevField.getText().length() == 0)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("Fields cannot be empty");
            alert.showAndWait();
        } else if (NoDate() == true) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("la date de updated  doit être aprés la date d'aujourd'hui");
            alert.showAndWait();
        } else {
            reclamation e = new reclamation();

            e.setName(nameField.getText());

            // Vérifier si le commentaire contient des mots interdits
            String commentaire = descriptionevField.getText();
            String[] badWords = {"fuck", "shit", "تبا"}; // Ajoutez vos mots interdits ici
            for (String badWord : badWords) {
                if (commentaire.toLowerCase().contains(badWord)) {
                    commentaire = commentaire.replaceAll(badWord, "*".repeat(badWord.length()));
                    // Afficher un message d'avertissement ou gérer le cas de mot interdit
                }
            }
            e.setCommentaire(commentaire);

            java.util.Date date_debut = java.util.Date.from(updatedevField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            Date sqlDate = new Date(date_debut.getTime());
            e.setUpdated(sqlDate);

            // Pour l'image
            e.setImage(imageevField.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information ");
            alert.setHeaderText("reclamation add");
            alert.setContentText("reclamation added successfully!");
            alert.showAndWait();
            try {
                Ev.ajouterreclamation(e);
                reset();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            getevs();

        }
    }


    //fin d ajout d'un reclamation
    private void reset() {
        nameField.setText("");

        descriptionevField.setText("");
        imageevField.setText("");


        updatedevField.setValue(null);

    }
    
   public void getevs() {  
         try {
            // TODO
            List<reclamation> reclamation = Ev.recupererreclamation();
            ObservableList<reclamation> olp = FXCollections.observableArrayList(reclamation);
            reclamationTv.setItems(olp);
            nomevTv.setCellValueFactory(new PropertyValueFactory<>("name"));

            imageevTv.setCellValueFactory(new PropertyValueFactory<>("image"));
             updatedevTv.setCellValueFactory(new PropertyValueFactory<>("updated"));

            descriptionevTv.setCellValueFactory(new PropertyValueFactory<>("commentaire"));


           // this.delete();
        } catch (SQLException ex) {
            System.out.println("error" + ex.getMessage());
        }
    }//get evs

     
     @FXML
   private void modifierreclamation(ActionEvent ev) throws SQLException {
        reclamation e = new reclamation();
        e.setId_reclamation(Integer.parseInt(idmodifierField.getText()));
        e.setName(nameField.getText());

        e.setCommentaire(descriptionevField.getText()); 
        Date d=Date.valueOf(updatedevField.getValue());

        e.setUpdated(d);

        e.setImage(imageevField.getText());


         Ev.modifierreclamation(e);
        reset();
        getevs();         
    }

    @FXML
    private void supprimerreclamation(ActionEvent ev) {
           reclamation e = reclamationTv.getItems().get(reclamationTv.getSelectionModel().getSelectedIndex());
        try {
            Ev.supprimerreclamation(e);
        } catch (SQLException ex) {
            Logger.getLogger(AjouterreclamationController.class.getName()).log(Level.SEVERE, null, ex);
        }   
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information ");
        alert.setHeaderText("reclamation delete");
        alert.setContentText("reclamation deleted successfully!");
        alert.showAndWait();        
        getevs();    
    }

    @FXML
    private void afficherreclamation(ActionEvent ev) {
         try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("index.fxml")));
             nameField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

  
    @FXML
    //ta3 tablee bch nenzel 3ala wehed ya5tarou w yet3abew textfield
    private void choisirev(MouseEvent ev) throws IOException {
        reclamation e = reclamationTv.getItems().get(reclamationTv.getSelectionModel().getSelectedIndex());
        //idLabel.setText(String.valueOf(e.getid_reclamation()));
        idmodifierField.setText(String.valueOf(e.getId_reclamation()));
        nameField.setText(e.getName());

        imageevField.setText(e.getImage());
        descriptionevField.setText(e.getCommentaire());



        //lel image
        String path = e.getImage();
               File file=new File(path);
              Image img = new Image(file.toURI().toString());
                imageview.setImage(img);
                

            
    }

    private void repondre(ActionEvent ev) {

        User u=new User();

        reponse p=new reponse();

        //p.setreclamation();
        p.setId_reclamation(Integer.parseInt(idmodifierField.getText()));

        Pservice.ajouterreponse(p);
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("index2.fxml")));
            nameField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void afficherreponses(ActionEvent ev) {
         try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("index2.fxml")));
             nameField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void uploadImage(ActionEvent ev)throws FileNotFoundException, IOException  {

        Random rand = new Random();
        int x = rand.nextInt(1000);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File Path");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file = fileChooser.showOpenDialog(null);
        String DBPath = "C:\\\\xampp\\\\htdocs\\\\imageP\\\\"  + x + ".jpg";
        if (file != null) {
            FileInputStream Fsource = new FileInputStream(file.getAbsolutePath());
            FileOutputStream Fdestination = new FileOutputStream(DBPath);
            BufferedInputStream bin = new BufferedInputStream(Fsource);
            BufferedOutputStream bou = new BufferedOutputStream(Fdestination);
            System.out.println(file.getAbsoluteFile());
            String path=file.getAbsolutePath();
            Image img = new Image(file.toURI().toString());
            imageview.setImage(img);    
            imageevField.setText(DBPath);
            int b = 0;
            while (b != -1) {
                b = bin.read();
                bou.write(b);
            }
            bin.close();
            bou.close();          
        } else {
            System.out.println("error");
        }
    }


    

    @FXML
    private void rechercherev(KeyEvent ev) {
        
        reclamationService bs=new reclamationService(); 
        reclamation b= new reclamation();
        ObservableList<reclamation>filter= bs.chercherev(rechercher.getText());
        populateTable(filter);
    }
     private void populateTable(ObservableList<reclamation> branlist){
       reclamationTv.setItems(branlist);
   
       }




    }


    





    

