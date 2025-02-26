package model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Livre {
    private SimpleStringProperty titre;
    private SimpleStringProperty isbn;
    private SimpleObjectProperty<Auteur> auteur;
    private SimpleBooleanProperty disponible;

    public Livre(String titre, String isbn, Auteur auteur, boolean disponible) {
        this.titre = new SimpleStringProperty(titre);
        this.isbn = new SimpleStringProperty(isbn);
        this.auteur = new SimpleObjectProperty<>(auteur);
        this.disponible = new SimpleBooleanProperty(disponible);
    }

    public String getTitre() {
        return titre.get();
    }

    public void setTitre(String titre) {
        this.titre.set(titre);
    }

    public String getIsbn() {
        return isbn.get();
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public Auteur getAuteur() {
        return auteur.get();
    }

    public void setAuteur(Auteur auteur) {
        this.auteur.set(auteur);
    }

    public boolean isDisponible() {
        return disponible.get();
    }

    public void setDisponible(boolean disponible) {
        this.disponible.set(disponible);
    }

    public SimpleStringProperty titreProperty() {
        return titre;
    }

    public SimpleObjectProperty<Auteur> auteurProperty() {
        return auteur;
    }

    public SimpleBooleanProperty disponibleProperty() {
        return disponible;
    }
}