package model;

public class SessionUtilisateur {
    private static SessionUtilisateur instance;
    private Utilisateur utilisateur;

    private SessionUtilisateur() {}

    public static SessionUtilisateur getInstance() {
        if (instance == null) {
            instance = new SessionUtilisateur();
        }
        return instance;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public String getRole() {
        return (utilisateur != null) ? utilisateur.getRole() : null;
    }

    public void deconnecter() {
        utilisateur = null;
    }
}