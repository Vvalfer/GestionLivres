package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Emprunt {

    private final StringProperty titre;
    private final StringProperty auteur;
    private final StringProperty dateEmprunt;
    private final StringProperty dateRetour;

    public Emprunt(String titre, String auteur, String dateEmprunt, String dateRetour) {
        this.titre = new SimpleStringProperty(titre);
        this.auteur = new SimpleStringProperty(auteur);
        this.dateEmprunt = new SimpleStringProperty(dateEmprunt);
        this.dateRetour = new SimpleStringProperty(dateRetour != null ? dateRetour : "En cours");
    }

    public StringProperty titreProperty() { return titre; }
    public StringProperty auteurProperty() { return auteur; }
    public StringProperty dateEmpruntProperty() { return dateEmprunt; }
    public StringProperty dateRetourProperty() { return dateRetour; }

    public String getTitre() { return titre.get(); }
    public String getAuteur() { return auteur.get(); }
}