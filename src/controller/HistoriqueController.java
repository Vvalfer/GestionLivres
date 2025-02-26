package controller;

import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Model;
import model.Historique;
import model.Utilisateur;
import util.SceneManager;

public class HistoriqueController {
    @FXML private TableView<Historique> tableHistorique;
    @FXML private TableColumn<Historique, String> colIsbn;
    @FXML private TableColumn<Historique, String> colAdherentNum;
    @FXML private TableColumn<Historique, String> colDateEmprunt;
    @FXML private TableColumn<Historique, String> colDateRetour;
    @FXML private Button btnRetour;

    @FXML
    public void initialize() {
        // Charger la liste d'historique depuis Model
        ObservableList<Historique> historiqueList = FXCollections.observableArrayList(Model.getHistorique());

        // ISBN
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        // AdherentNum
        colAdherentNum.setCellValueFactory(cellData -> {
            String adherentNum = cellData.getValue().getAdherentNum();
            int adherentID = Integer.parseInt(adherentNum);
            
            // => Trouver l'utilisateur dans la BD
            Utilisateur user = Model.getUtilisateurById(adherentID); 
            
            if (user != null) {
                String fullName = user.getNom() + " " + user.getPrenom();
                return new javafx.beans.property.SimpleStringProperty(fullName);
            } else {
                // Si on ne trouve pas, on renvoie l'adherentNum brut
                return new javafx.beans.property.SimpleStringProperty("Inconnu (" + adherentNum + ")");
            }
        });

        // Date Emprunt
        colDateEmprunt.setCellValueFactory(new PropertyValueFactory<>("dateEmprunt"));
        // Date Retour
        colDateRetour.setCellValueFactory(new PropertyValueFactory<>("dateRetour"));

        tableHistorique.setItems(historiqueList);
    }

    @FXML
    private void retourBibliothecaire() {
        Stage stage = (Stage) btnRetour.getScene().getWindow();
        SceneManager.loadScene(stage, "/view/BibliothecaireView.fxml");
    }
}