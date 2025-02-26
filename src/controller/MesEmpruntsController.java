package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Emprunt;
import model.Model;
import model.SessionUtilisateur;
import model.Utilisateur;
import util.SceneManager;

import java.util.List;

public class MesEmpruntsController {

    @FXML private TableView<Emprunt> tableEmprunts;
    @FXML private TableColumn<Emprunt, String> colTitre;
    @FXML private TableColumn<Emprunt, String> colAuteur;
    @FXML private TableColumn<Emprunt, String> colDateEmprunt;
    @FXML private TableColumn<Emprunt, String> colDateRetour;
    @FXML private TextField searchField;
    @FXML private Button btnRetour;

    private final ObservableList<Emprunt> empruntsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupColumns();
        loadEmprunts();
        searchField.setOnKeyReleased(this::handleSearch);
    }

    // méthode pour configurer les colonnes du tableau
    private void setupColumns() {
        colTitre.setCellValueFactory(data -> data.getValue().titreProperty());
        colAuteur.setCellValueFactory(data -> data.getValue().auteurProperty());
        colDateEmprunt.setCellValueFactory(data -> data.getValue().dateEmpruntProperty());
        colDateRetour.setCellValueFactory(data -> data.getValue().dateRetourProperty());
    }

    // méthode pour charger les emprunts de l'utilisateur
    private void loadEmprunts() {
        Utilisateur Currentuser = SessionUtilisateur.getInstance().getUtilisateur();
        int utilisateurId = Currentuser.getId();
        List<Emprunt> emprunts = Model.getEmpruntsByUtilisateur(utilisateurId);
        empruntsList.setAll(emprunts);
        tableEmprunts.setItems(empruntsList);
        }

        @FXML
        // méthode pour la recherche dans la liste des emprunts
        private void handleSearch(KeyEvent event) {
        String searchText = searchField.getText().toLowerCase();

        ObservableList<Emprunt> filteredList = FXCollections.observableArrayList(
            empruntsList.filtered(emprunt -> emprunt.getTitre().toLowerCase().contains(searchText) ||
                             emprunt.getAuteur().toLowerCase().contains(searchText))
        );

        tableEmprunts.setItems(filteredList);
        }

        @FXML
        private void handleRetour() {
         SceneManager.loadScene((Stage) btnRetour.getScene().getWindow(), "/view/AdherentView.fxml");
    }
}