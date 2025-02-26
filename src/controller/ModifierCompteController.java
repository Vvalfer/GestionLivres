package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.stage.Stage;
import java.net.URL;
import model.Model;
import model.SessionUtilisateur;
import model.Utilisateur;
import util.SceneManager;

import java.util.Optional;

public class ModifierCompteController {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;        // Nouveau
    @FXML private PasswordField txtPasswordConfirm; // Confirmation

    private Utilisateur utilisateurActuel;

    @FXML
    public void initialize() {
        utilisateurActuel = SessionUtilisateur.getInstance().getUtilisateur();
        rechargerDonnees();
    }

    /**
     * Remplit les champs avec les infos de 'utilisateurActuel'.
     */
    private void rechargerDonnees() {
        if (utilisateurActuel != null) {
            txtNom.setText(utilisateurActuel.getNom());
            txtPrenom.setText(utilisateurActuel.getPrenom());
            txtEmail.setText(utilisateurActuel.getEmail());
            txtPassword.clear();
            txtPasswordConfirm.clear();
        }
    }

    @FXML
    private void handleValider() {
        if (utilisateurActuel == null) {
            Model.afficherErreur("Aucun utilisateur chargé.");
            return;
        }

        // Champs obligatoires
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String email = txtEmail.getText().trim();
        String newPassword = txtPassword.getText().trim();
        String confirmPassword = txtPasswordConfirm.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            Model.afficherErreur("Veuillez remplir Nom, Prénom et Email.");
            return;
        }

        // Vérification si l'utilisateur veut changer de mot de passe
        if (!newPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                Model.afficherErreur("Le nouveau mot de passe et la confirmation ne correspondent pas.");
                return;
            }
            if (newPassword.length() < 6) {
                Model.afficherErreur("Le nouveau mot de passe doit faire au moins 6 caractères.");
                return;
            }
        }

        // On demande le mot de passe actuel via pop-up
        String oldPassword = demanderMotDePasseActuel();
        if (oldPassword == null) {
            // L’utilisateur a annulé le pop-up
            return;
        }

        // Vérifier que le mot de passe actuel est correct
        Utilisateur checkUser = Model.verifierConnexion(utilisateurActuel.getEmail(), oldPassword);
        if (checkUser == null) {
            Model.afficherErreur("Mot de passe actuel incorrect.");
            return;
        }

        // Mise à jour en BD
        boolean success = Model.updateUtilisateur(
            utilisateurActuel.getId(),
            nom,
            prenom,
            email,
            newPassword  // si vide, on ne modifie pas le password
        );

        if (success) {
            Model.afficherConfirmation("Compte mis à jour avec succès !");
            // Recharger l’utilisateur actualisé depuis la BD
            Utilisateur refresh = Model.getUtilisateurById(utilisateurActuel.getId());
            if (refresh != null) {
                utilisateurActuel = refresh;
            }
            rechargerDonnees();
        } else {
            Model.afficherErreur("Une erreur est survenue lors de la mise à jour.");
        }
    }

    /**
     * Ouvre un popup demandant le mot de passe actuel, ou renvoie null si annulé.
     */
    private String demanderMotDePasseActuel() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Validation du mot de passe");
        dialog.setHeaderText("Veuillez saisir votre mot de passe actuel :");

        PasswordField pf = new PasswordField();
        pf.setPromptText("Mot de passe actuel");

        VBox vbox = new VBox();
        vbox.getChildren().add(pf);
        dialog.getDialogPane().setContent(vbox);

        ButtonType okBtn = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == okBtn) {
                return pf.getText();
            }
            return null;
        });

        // Charger la feuille de style
        URL cssURL = getClass().getResource("/view/style.css");
        if (cssURL == null) {
            System.out.println("Echec : /view/style.css introuvable !");
        } else {
            dialog.getDialogPane().getStylesheets().add(cssURL.toExternalForm());
            dialog.getDialogPane().getStyleClass().add("dialog-pane");
        }

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    @FXML
    private void handleAnnuler() {
        Stage stage = (Stage) txtNom.getScene().getWindow();
        SceneManager.loadScene(stage, "/view/AdherentView.fxml");
    }
}