package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.SessionUtilisateur;
import util.SceneManager;
import javafx.scene.control.Label;

public class AdherentController {
    @FXML private Button btnVoirMesEmprunts;
    @FXML private Button btnModifierCompte;
    @FXML private Button btnDeconnexion;
    @FXML private Label lblNom;
    @FXML private Label lblPrenom;

    @FXML
    private void allerMesEmprunts() {
        SceneManager.loadScene((Stage) btnVoirMesEmprunts.getScene().getWindow(), "/view/MesEmpruntsView.fxml");
    }

    @FXML
    private void modifierMonCompte() {
        SceneManager.loadScene((Stage) btnModifierCompte.getScene().getWindow(), "/view/ModifierCompteView.fxml");
    }

    @FXML
    private void deconnexion() {
        SessionUtilisateur.getInstance().deconnecter();
        SceneManager.loadScene((Stage) btnDeconnexion.getScene().getWindow(), "/view/LoginView.fxml");
    }

}