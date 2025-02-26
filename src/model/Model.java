package model;

import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.Alert;

public class Model {

    // Vérifier les identifiants de connexion
    public static Utilisateur verifierConnexion(String email, String password) {
        String query = "SELECT * FROM utilisateur WHERE email = ? AND password = ?";
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
    
            stmt.setString(1, email);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                return new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;  // Retourne `null` si les identifiants sont incorrects
    }    

    // Récupérer tous les livres
    public static List<Livre> getAllLivres() {
        List<Livre> livres = new ArrayList<>();
        
        String query = """
            SELECT l.ISBN, l.titre, 
                   CASE WHEN l.adherent_id IS NULL THEN TRUE ELSE FALSE END AS disponible,
                   a.num, a.nom, a.prenom, a.date_naissance, a.description
            FROM livre l
            LEFT JOIN auteur a ON l.auteur = a.num
        """;
    
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            System.out.println("Chargement des livres depuis la base...");
            while (rs.next()) {
                Auteur auteur = new Auteur( 
                    rs.getString("prenom"),
                    rs.getString("nom"),
                    rs.getString("date_naissance"),
                    rs.getString("description")
                );
    
                boolean estDisponible = rs.getBoolean("disponible");
    
                Livre livre = new Livre(
                    rs.getString("titre"),
                    rs.getString("ISBN"),
                    auteur,
                    estDisponible
                );
    
                livres.add(livre);
            }

            if (livres.isEmpty()) {
                System.out.println("Aucun livre trouvé dans la base de données.");
            } else {
                System.out.println("Livres chargés avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }  

    // Récupérer l'historique des emprunts
    public static ObservableList<Historique> getHistorique() {
        ObservableList<Historique> historiqueList = FXCollections.observableArrayList();
        String query = "SELECT h.id, h.isbn, h.date_emprunt, h.date_retour, h.adherent_id " +
               "FROM historique h " +
               "ORDER BY h.date_emprunt DESC";
    
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                String id = rs.getString("id");
                String isbn = rs.getString("isbn");
                String dateEmprunt = rs.getString("date_emprunt");
                String dateRetour = rs.getString("date_retour") != null ? rs.getString("date_retour") : "Non retourné";
                String adherentNum = rs.getString("adherent_id");
    
                historiqueList.add(new Historique(id, isbn, dateEmprunt, dateRetour, adherentNum));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur SQL lors de la récupération de l'historique.");
        }
        return historiqueList;
    }

    // Récupérer tous les adhérents
    public static ObservableList<Utilisateur> getAllAdherents() {
        ObservableList<Utilisateur> adherents = FXCollections.observableArrayList();
        String query = "SELECT * FROM utilisateur WHERE role = 'adherent'";
    
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                Utilisateur adherent = new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("role")
                );
                adherents.add(adherent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adherents;
    }
     
    // Vérifier si un adhérent existe avec l'email donné
    public static Utilisateur verifierAdherentParEmail(String email) {
        String query = "SELECT * FROM utilisateur WHERE email = ? AND role = 'adherent'";
    
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
    
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                return new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Récupérer un utilisateur par ID
    public static Utilisateur getUtilisateurById(int id) {
        String query = "SELECT * FROM utilisateur WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur SQL lors de la récupération de l'utilisateur par ID.");
        }
        return null;
    }
    
    
    // Méthode pour emprunter un livre
    public static boolean emprunterLivre(String isbn, int adherentId) {
        String query = "UPDATE livre SET adherent_id = ? WHERE ISBN = ? AND adherent_id IS NULL";
    
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
    
            stmt.setInt(1, adherentId);
            stmt.setString(2, isbn);
    
            System.out.println("Tentative d'emprunt - ISBN: " + isbn + ", Adhérent ID: " + adherentId);
    
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Livre emprunté avec succès !");
            } else {
                System.out.println("Aucun livre mis à jour (ISBN déjà emprunté ou inexistant)");
            }
    
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Méthode pour retourner un livre
    public static boolean retournerLivre(String isbn) {
        String query = "UPDATE livre SET adherent_id = NULL WHERE isbn = ? AND adherent_id IS NOT NULL";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, isbn);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                enregistrerRetour(isbn);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur SQL lors du retour du livre.");
        }
        return false;
    }

    // Enregistrer l'emprunt dans l'historique
    public static void enregistrerEmprunt(String isbn, int adherentId) {
        String query = "INSERT INTO historique (isbn, adherent_id, date_emprunt) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, isbn);
            stmt.setInt(2, adherentId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Insertion réussie dans l'historique pour ISBN : " + isbn + " | Adhérent ID : " + adherentId);
            } else {
                System.out.println("Échec de l'insertion dans l'historique pour ISBN : " + isbn + " | Adhérent ID : " + adherentId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur SQL lors de l'enregistrement de l'emprunt.");
        }
    }

    // Enregistrer la date de retour dans l'historique sans passer l'adherent_id
    public static void enregistrerRetour(String isbn) {
        String query = "UPDATE historique SET date_retour = CURRENT_TIMESTAMP " +
                    "WHERE isbn = ? AND date_retour IS NULL";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, isbn);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Retour enregistré dans l'historique pour ISBN : " + isbn);
            } else {
                System.out.println("Aucun emprunt actif trouvé pour ISBN : " + isbn + " (vérifie si le livre est déjà retourné)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur SQL lors de l'enregistrement du retour.");
        }
    }

    // Afficher une boîte de dialogue d'erreur
    public static void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Afficher une boîte de dialogue de confirmation
    public static void afficherConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Récupérer les emprunts d'un utilisateur
    public static List<Emprunt> getEmpruntsByUtilisateur(int utilisateurId) {
        List<Emprunt> emprunts = new ArrayList<>();
        String query = """
            SELECT l.titre, CONCAT(a.prenom, ' ', a.nom) AS auteur, 
                   h.date_emprunt, h.date_retour
            FROM historique h
            JOIN livre l ON h.isbn = l.ISBN
            JOIN auteur a ON l.auteur = a.num
            WHERE h.adherent_id = ?
            ORDER BY h.date_emprunt DESC
        """;
    
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
    
            stmt.setInt(1, utilisateurId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                String titre = rs.getString("titre");
                String auteur = rs.getString("auteur");
                String dateEmprunt = rs.getTimestamp("date_emprunt").toLocalDateTime().toLocalDate().toString();
                String dateRetour = rs.getTimestamp("date_retour") != null
                                    ? rs.getTimestamp("date_retour").toLocalDateTime().toLocalDate().toString()
                                    : null;
    
                emprunts.add(new Emprunt(titre, auteur, dateEmprunt, dateRetour));
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return emprunts;
    }

    // Mettre a jour un utilisateur
    public static boolean updateUtilisateur(int id, String nom, String prenom, String email, String password) {
        String baseQuery = "UPDATE utilisateur SET nom = ?, prenom = ?, email = ?";
        boolean hasPassword = (password != null && !password.isEmpty());
    
        if (hasPassword) {
            baseQuery += ", password = ?";
        }
        baseQuery += " WHERE id = ?";
    
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(baseQuery)) {
    
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            int index = 4;
            if (hasPassword) {
                // Ici on peut ajouter un hachage : e.g. hashPassword(password)
                stmt.setString(4, password);
                index = 5;
            }
            stmt.setInt(index, id);
    
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            Model.afficherErreur("Erreur lors de la mise à jour de l'utilisateur.");
            return false;
        }
    }

    public static String getDernierEmpruntPourAdherent(int adherentId) {
        String query = """
            SELECT date_emprunt 
            FROM historique
            JOIN utilisateur ON historique.adherent_id = utilisateur.id
            WHERE utilisateur.id = ?
            ORDER BY date_emprunt DESC
            LIMIT 1
        """;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, adherentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // ex: "2025-03-10 14:35:00"
                return rs.getString("date_emprunt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur SQL lors de la récupération du dernier emprunt.");
        }
        return null; // Pas trouvé
    }
    
    public static String getDernierRetourPourAdherent(int adherentId) {
        String query = """
            SELECT date_retour 
            FROM historique
            JOIN utilisateur ON historique.adherent_id = utilisateur.id
            WHERE utilisateur.id = ?
              AND date_retour IS NOT NULL
            ORDER BY date_retour DESC
            LIMIT 1
        """;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, adherentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("date_retour");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur SQL lors de la récupération du dernier retour.");
        }
        return null;
    }    
    
}