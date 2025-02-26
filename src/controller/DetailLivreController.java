package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Livre;

public class DetailLivreController {
    @FXML private Label labelTitre;
    @FXML private Label labelAuteur;
    @FXML private Label labelDateNaissanceAuteur;
    @FXML private TextArea labelDescriptionAuteur;
    @FXML private Button btnEmprunter;

    private AccueilController accueilController;
    private Livre livreSelectionne;

    public void setAccueilController(AccueilController accueilController) {
        this.accueilController = accueilController;
    }    

    public void setLivreSelectionne(Livre livre) {
        this.livreSelectionne = livre;
        
        if (livreSelectionne == null) {
            System.out.println("Aucun livre sélectionné.");
            return;
        }

        // Vérification de la disponibilité du livre
        if (livreSelectionne.isDisponible()) {
            btnEmprunter.setDisable(false);
        } else {
            btnEmprunter.setDisable(true);
        }

        // Affichage des informations du livre
        labelTitre.setText("Titre : " + livreSelectionne.getTitre());
    
        // Vérification et affichage des informations de l’auteur
        if (livreSelectionne.getAuteur() != null) {
            labelAuteur.setText(livreSelectionne.getAuteur().getPrenom() + " " + livreSelectionne.getAuteur().getNom());
            labelDateNaissanceAuteur.setText(livreSelectionne.getAuteur().getDateNaissance());
            labelDescriptionAuteur.setText(livreSelectionne.getAuteur().getDescription());
        } else {
            labelAuteur.setText("Auteur : Inconnu");
            labelDateNaissanceAuteur.setText("Date de naissance : Inconnue");
            labelDescriptionAuteur.setText("Description : Non disponible");
        }
    
        System.out.println("Détails du livre mis à jour : " + livreSelectionne.getTitre());
    }
    
    @FXML
    private void handleEmprunt() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EmpruntView.fxml"));
            Parent root = loader.load();

            EmpruntController empruntController = loader.getController();
            empruntController.setIsbnLivre(livreSelectionne.getIsbn());
            empruntController.setLivre(livreSelectionne.getTitre());

            // Vérification et transmission de accueilController
            if (accueilController != null) {
                empruntController.setAccueilController(accueilController);
                System.out.println("AccueilController transmis avec succès à EmpruntController !");
            } else {
                System.out.println("Erreur : accueilController est NULL lors de la transmission à EmpruntController !");
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRetour() {
        // Fermer la fenêtre actuelle
        Stage stage = (Stage) labelTitre.getScene().getWindow();
        stage.close();
    }

}