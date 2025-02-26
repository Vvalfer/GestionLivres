package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.SessionUtilisateur;
import util.SceneManager;

public class BibliothecaireController {
    @FXML private Button btnVoirAdherents;
    @FXML private Button btnVoirHistorique;
    @FXML private Button btnDeconnexion;

    @FXML
    private void voirAdherents() {
        SceneManager.loadScene((Stage) btnVoirAdherents.getScene().getWindow(), "/view/ListeAdherentsView.fxml");
    }

    @FXML
    private void voirHistorique() {
        SceneManager.loadScene((Stage) btnVoirHistorique.getScene().getWindow(), "/view/HistoriqueView.fxml");
    }

    @FXML
    private void deconnexion() {
        SessionUtilisateur.getInstance().deconnecter();
        SceneManager.loadScene((Stage) btnDeconnexion.getScene().getWindow(), "/view/LoginView.fxml");
    }
    
}