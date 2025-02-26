package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Model;
import model.SessionUtilisateur;
import model.Utilisateur;
import util.SceneManager;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button btnLogin;

    /**
     * Méthode appelée lorsque l'utilisateur clique sur "Se connecter".
     * On vérifie la validité des champs et on tente une connexion.
     */
    @FXML
    private void handleLogin() {
        // Récupération des champs
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // 1. Vérifications basiques (champs vides)
        if (email.isEmpty() || password.isEmpty()) {
            Model.afficherErreur("Veuillez renseigner votre email et votre mot de passe.");
            return;
        }

        // 2. Vérification éventuelle de format d'email (exemple simple):
        if (!estEmailValide(email)) {
            Model.afficherErreur("Email invalide. Veuillez saisir un email correct.");
            return;
        }

        // 3. Vérification des identifiants en base
        Utilisateur utilisateur = Model.verifierConnexion(email, password);

        if (utilisateur != null) {
            // 4. On stocke l'utilisateur dans la session
            SessionUtilisateur.getInstance().setUtilisateur(utilisateur);

            // 5. On redirige selon le rôle
            redirigerUtilisateur();
        } else {
            // Identifiants incorrects
            Model.afficherErreur("Identifiant ou mot de passe incorrect !");
        }
    }

    /**
     * Rediriger l'utilisateur en fonction de son rôle (bibliothécaire, adhérent, etc.)
     */
    private void redirigerUtilisateur() {
        Stage stage = (Stage) btnLogin.getScene().getWindow();

        // On récupère l'utilisateur courant
        Utilisateur user = SessionUtilisateur.getInstance().getUtilisateur();
        if (user == null) {
            // Cas improbable si handleLogin a déjà validé la connexion, 
            // mais on gère la sécurité
            Model.afficherErreur("Erreur : aucun utilisateur connecté.");
            return;
        }

        // On vérifie le rôle
        String role = user.getRole(); // ou SessionUtilisateur.getInstance().getRole()
        if ("bibliothecaire".equalsIgnoreCase(role)) {
            SceneManager.loadScene(stage, "/view/BibliothecaireView.fxml");
        } else if ("adherent".equalsIgnoreCase(role)) {
            SceneManager.loadScene(stage, "/view/AdherentView.fxml");
        } else {
            // Rôle inconnu => gestion d’erreur ou renvoi vers une page par défaut
            Model.afficherErreur("Rôle utilisateur inconnu : " + role);
            SceneManager.loadScene(stage, "/view/AccueilView.fxml");
        }
    }

    /**
     * Méthode appelée lorsque l'utilisateur clique sur "Retour".
     * Permet de revenir à la page d'accueil générale.
     */
    @FXML
    private void handleRetourAccueil() {
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        SceneManager.loadScene(stage, "/view/AccueilView.fxml");
    }

    /**
     * Vérification sommaire du format d'email.
     */
    private boolean estEmailValide(String email) {
        String regex = "^[^@]+@[^@]+\\.[^@]+$";
        return email.matches(regex);
    }
}
