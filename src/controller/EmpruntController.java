package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Model;
import model.Utilisateur;

public class EmpruntController {
    @FXML private Label labelTitre;
    @FXML private TextField emailField;

    private AccueilController accueilController;
    private String isbnLivre;
    private String titreLivre;

    public void setLivre(String titreLivre) {
        this.titreLivre = titreLivre;
        System.out.println("Titre reçu dans EmpruntController : " + titreLivre);
        labelTitre.setText(titreLivre);
    }

    public void setAccueilController(AccueilController accueilController) {
        this.accueilController = accueilController;
        System.out.println("AccueilController bien transmis à EmpruntController !");
    }

    public void setIsbnLivre(String isbn) {
        this.isbnLivre = isbn;
        System.out.println("ISBN reçu dans EmpruntController : " + isbn);
    }

    private boolean isValidEmail(String email) {
        // Expression régulière simple pour vérifier le format d'un email
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }

    @FXML
    private void handleConfirmer() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            Model.afficherErreur("Veuillez entrer un email.");
            return;
        }

        // Vérification du format de l'email
        if (!isValidEmail(email)) {
            Model.afficherErreur("Format d'email invalide. Veuillez entrer un email valide.");
            return;
        }

        Utilisateur adherent = Model.verifierAdherentParEmail(email);
        if (adherent == null) {
            Model.afficherErreur("Aucun adhérent trouvé avec cet email.");
            return;
        }

        boolean success = Model.emprunterLivre(isbnLivre, adherent.getId()); 
        Model.enregistrerEmprunt(isbnLivre, adherent.getId());

        if (success) {
            Model.afficherConfirmation("Livre emprunté avec succès !");

            // Vérification de l'actualisation
            if (accueilController != null) {
                System.out.println("Appel de handleActualiser() après emprunt...");
                accueilController.handleActualiser();
            } else {
                System.out.println("Erreur : accueilController est NULL après emprunt !");
            }

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.close();
        } else {
            Model.afficherErreur("Impossible d'emprunter ce livre.");
        }
    }
        
    @FXML
    private void handleAnnuler() {
        // Fermer la fenêtre
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }
}