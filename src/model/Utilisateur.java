package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Utilisateur {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty email;
    private final SimpleStringProperty nom;
    private final SimpleStringProperty prenom;
    private final SimpleStringProperty role;

    public Utilisateur(int id, String email, String nom, String prenom, String role) {
        this.id = new SimpleIntegerProperty(id);
        this.email = new SimpleStringProperty(email);
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.role = new SimpleStringProperty(role);
    }

    public int getId() { return id.get(); }
    public String getEmail() { return email.get(); }
    public String getNom() { return nom.get(); }
    public String getPrenom() { return prenom.get(); }
    public String getRole() { return role.get(); }

    public SimpleIntegerProperty idProperty() { return id; }
    public SimpleStringProperty emailProperty() { return email; }
    public SimpleStringProperty nomProperty() { return nom; }
    public SimpleStringProperty prenomProperty() { return prenom; }
    public SimpleStringProperty roleProperty() { return role; }
}