package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Model;

public class RetourController {
    @FXML private TextField isbnField;

    private AccueilController accueilController;

    public void setAccueilController(AccueilController accueilController) {
        this.accueilController = accueilController;
    }

    @FXML
    private void handleConfirmer() {
        String isbn = isbnField.getText().trim();
    
        // Vérification que l'ISBN contient uniquement 12 ou 13 chiffres
        if (!isbn.matches("\\d{12,13}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("L'ISBN doit contenir 12 ou 13 chiffres !");
            alert.showAndWait();
            return;
        }
    
        if (Model.retournerLivre(isbn)) {
            System.out.println("Livre retourné avec succès !");
            Model.enregistrerRetour(isbn);
            
            // Afficher une notification
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Le livre a été retourné avec succès !");
            alert.showAndWait();
    
            // Rafraîchir la liste sur la page d'accueil
            if (accueilController != null) {
                accueilController.handleActualiser();
            }
    
            // Fermer la fenêtre
            Stage stage = (Stage) isbnField.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("ISBN invalide ou livre non emprunté. Veuillez réessayer.");
            alert.showAndWait();
        }
    }    
    
    @FXML
    private void handleAnnuler() {
        // Fermer la fenêtre
        Stage stage = (Stage) isbnField.getScene().getWindow();
        stage.close();
    }
}