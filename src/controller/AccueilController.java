package controller;

import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Livre;
import model.Model;
import util.SceneManager;


public class AccueilController {
    @FXML private TableView<Livre> tableLivres;
    @FXML private TableColumn<Livre, String> colTitre;
    @FXML private TableColumn<Livre, String> colAuteur;
    @FXML private TextField searchField;
    @FXML private TableColumn<Livre, Boolean> colDisponible;
    @FXML private TableColumn<Livre, String> colIsbn;
    @FXML private Button btnSeConnecter;


    private ObservableList<Livre> livres;

    @FXML
    public void initialize() {
        // Charger les données depuis le modèle
        livres = FXCollections.observableArrayList(Model.getAllLivres());

        // Configurer les colonnes
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colAuteur.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getAuteur().getNom())
        );

        // Configuration de la colonne "Disponible" avec un indicateur graphique
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        colDisponible.setCellFactory(column -> new TableCell<Livre, Boolean>() {
            @Override
            protected void updateItem(Boolean disponible, boolean empty) {
                super.updateItem(disponible, empty);
                if (empty || disponible == null) {
                    setGraphic(null);
                } else {
                    // Créer un cercle indicateur (rayon 7 pixels)
                    Circle indicator = new Circle(7);
                    if (disponible) {
                        indicator.setFill(Color.web("#00FF00")); // Vert pour disponible
                    } else {
                        indicator.setFill(Color.web("#FF0000")); // Rouge pour indisponible
                    }
                    setGraphic(indicator);
                }
            }
        });

        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        // Ajouter les livres à la TableView
        tableLivres.setItems(livres);

        // Ajouter un gestionnaire pour le double-clic
        tableLivres.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleLivreSelection();
            }
        });
    }

    // méthode pour gérer l'événement du clic sur le bouton "Rechercher"
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<Livre> filteredLivres = livres.filtered(livre ->
            livre.getTitre().toLowerCase().contains(searchText) || 
            livre.getAuteur().getNom().toLowerCase().contains(searchText)
        );
        tableLivres.setItems(filteredLivres);
    }

    @FXML
    private void handleFilterByDisponibility() {
        ObservableList<Livre> filteredLivres = livres.filtered(Livre::isDisponible);
        tableLivres.setItems(filteredLivres);
    }

    @FXML
    private void handleFilterAll() {
        tableLivres.setItems(livres);
    }

    // méthode pour gérer l'événement du clic sur le bouton "Filtrer par titre"
    @FXML
    private void handleFilterByTitle() {
        ObservableList<Livre> sortedByTitle = FXCollections.observableArrayList(livres.sorted((l1, l2) -> l1.getTitre().compareToIgnoreCase(l2.getTitre())));
        tableLivres.setItems(sortedByTitle);
    }

    // méthode pour gérer l'événement du clic sur le bouton "Filtrer par auteur"
    @FXML
    private void handleFilterByAuthor() {
        ObservableList<Livre> sortedByAuthor = FXCollections.observableArrayList(livres.sorted((l1, l2) -> l1.getAuteur().getNom().compareToIgnoreCase(l2.getAuteur().getNom())));
        tableLivres.setItems(sortedByAuthor);
    }

    // méthode pour gérer l'événement du double-clic sur un livre
    @FXML
    private void handleLivreSelection() {
        Livre livre = tableLivres.getSelectionModel().getSelectedItem();

        if (livre == null) {
            Model.afficherErreur("Veuillez sélectionner un livre.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DetailLivre.fxml"));
            Parent root = loader.load();

            DetailLivreController detailController = loader.getController();
            detailController.setLivreSelectionne(livre);
            detailController.setAccueilController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // méthode pour gérer le retour d'un livre
    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RetourView.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Retour d'un Livre");
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);
    
            // Passer le contrôleur de l'accueil
            RetourController controller = loader.getController();
            controller.setAccueilController(this);
    
            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }  

    // méthode pour actualiser la liste des livres
    @FXML
    public void handleActualiser() {
        System.out.println("Actualisation de la liste des livres...");
        
        ObservableList<Livre> livresActuels = FXCollections.observableArrayList(Model.getAllLivres());
        tableLivres.setItems(livresActuels);
        tableLivres.refresh();

        System.out.println("Mise à jour terminée. Nombre de livres après actualisation : " + livresActuels.size());
    }
        
    // méthode pour aller à la page de connexion
    @FXML
    private void allerPageConnexion() {
        Stage stage = (Stage) btnSeConnecter.getScene().getWindow();
        SceneManager.loadScene(stage, "/view/LoginView.fxml");
    }
}