 package org.example.controllers;

import org.example.models.DemandeSponsoring;
import org.example.dao.DemandeSponsoringService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class AdminDemandeSponsoring {

    @FXML
    private Button acceptButton;

    @FXML
    private TableView<DemandeSponsoring> demandeTable;

    @FXML
    private TableColumn<DemandeSponsoring, Integer> eventIdColumn;

    @FXML
    private TableColumn<DemandeSponsoring, Integer> idColumn;

    @FXML
    private TableColumn<DemandeSponsoring, String> justificationColumn;

    @FXML
    private Button rejectButton;

    @FXML
    private TableColumn<DemandeSponsoring, String> sponsorIdColumn;

    @FXML
    private TableColumn<DemandeSponsoring,String > statutColumn;

    private DemandeSponsoringService demandeService = new DemandeSponsoringService();
    private ObservableList<DemandeSponsoring> demandeList = FXCollections.observableArrayList();


    @FXML
    void handleAccept(ActionEvent event) {
        DemandeSponsoring selectedDemande = demandeTable.getSelectionModel().getSelectedItem();
        if (selectedDemande != null) {
            try {
                demandeService.updateDemandeStatus(selectedDemande.getId(), "Accepted");
                loadDemandesSponsoring();
            } catch (SQLException e) {
                System.err.println("Error updating request: " + e.getMessage());
            }
        }

    }

    @FXML
    void handleReject(ActionEvent event) {
        DemandeSponsoring selectedDemande = demandeTable.getSelectionModel().getSelectedItem();
        if (selectedDemande != null) {
            try {
                demandeService.updateDemandeStatus(selectedDemande.getId(), "Rejected");
                loadDemandesSponsoring();
            } catch (SQLException e) {
                System.err.println("Error updating request: " + e.getMessage());
            }
        }
    }


    private void loadDemandesSponsoring() {
        demandeList.clear();
        demandeList.addAll(demandeService.getAllDemandes());
        demandeTable.setItems(demandeList);
    }
    public void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        sponsorIdColumn.setCellValueFactory(new PropertyValueFactory<>("sponsor"));
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        justificationColumn.setCellValueFactory(new PropertyValueFactory<>("justification"));

        loadDemandesSponsoring();
    }

}
