package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Model;
import model.SessionUtilisateur;
import model.Utilisateur;
import util.SceneManager;

public class ListeAdherentsController {

    @FXML private TextField searchField;
    @FXML private TableView<Utilisateur> tableAdherents;
    @FXML private TableColumn<Utilisateur, Integer> colId;
    @FXML private TableColumn<Utilisateur, String> colNom;
    @FXML private TableColumn<Utilisateur, String> colPrenom;
    @FXML private TableColumn<Utilisateur, String> colEmail;
    @FXML private TableColumn<Utilisateur, String> colDernierEmprunt;  // Nouvelle
    @FXML private TableColumn<Utilisateur, String> colDernierRetour;   // Nouvelle
    @FXML private Button btnRetour;

    private ObservableList<Utilisateur> adherentsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Vérification d'accès
        if (!"bibliothecaire".equals(SessionUtilisateur.getInstance().getRole())) {
            SceneManager.loadScene((Stage) btnRetour.getScene().getWindow(), "/view/LoginView.fxml");
            return;
        }

        // Configuration des colonnes
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNom.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        colPrenom.setCellValueFactory(cellData -> cellData.getValue().prenomProperty());
        colEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        // CellValueFactory pour "Dernier Emprunt" et "Dernier Retour"
        colDernierEmprunt.setCellValueFactory(cellData -> {
            Utilisateur user = cellData.getValue();
            String dateEmprunt = Model.getDernierEmpruntPourAdherent(user.getId());
            // ex: "2025-03-10 14:00" ou null
            return new javafx.beans.property.SimpleStringProperty(
                    dateEmprunt != null ? dateEmprunt : "Pas encore"
            );
        });

        colDernierRetour.setCellValueFactory(cellData -> {
            Utilisateur user = cellData.getValue();
            String dateRetour = Model.getDernierRetourPourAdherent(user.getId());
            return new javafx.beans.property.SimpleStringProperty(
                    dateRetour != null ? dateRetour : "Pas encore"
            );
        });

        // Chargement des adhérents
        chargerAdherents();
    }

    // Fonction de chargement des adhérents
    private void chargerAdherents() {
        adherentsList.clear();
        adherentsList.addAll(Model.getAllAdherents());
        tableAdherents.setItems(adherentsList);
    }

    // Fonction de filtrage des adhérents pour la barre de recherche
    @FXML
    private void filtrerAdherents() {
        String searchTerm = searchField.getText().toLowerCase();

        ObservableList<Utilisateur> filteredList = adherentsList.filtered(adherent ->
            adherent.getNom().toLowerCase().contains(searchTerm) ||
            adherent.getPrenom().toLowerCase().contains(searchTerm) ||
            adherent.getEmail().toLowerCase().contains(searchTerm)
        );
        tableAdherents.setItems(filteredList);
    }

    @FXML
    private void retourBibliothecaire() {
        SceneManager.loadScene((Stage) btnRetour.getScene().getWindow(), "/view/BibliothecaireView.fxml");
    }
}