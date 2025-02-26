package model;

public class Historique {
    private String id;
    private String isbn;
    private String dateEmprunt;
    private String dateRetour;
    private String adherentNum;

    public Historique(String id, String isbn, String dateEmprunt, String dateRetour, String adherentNum) {
        this.id = id;
        this.isbn = isbn;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
        this.adherentNum = adherentNum;
    }

    public String getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getDateEmprunt() { return dateEmprunt; }
    public String getDateRetour() { return dateRetour; }
    public String getAdherentNum() { return adherentNum; }

    public void setId(String id) { this.id = id; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setDateEmprunt(String dateEmprunt) { this.dateEmprunt = dateEmprunt; }
    public void setDateRetour(String dateRetour) { this.dateRetour = dateRetour; }
    public void setAdherentNum(String adherentNum) { this.adherentNum = adherentNum; }
}
