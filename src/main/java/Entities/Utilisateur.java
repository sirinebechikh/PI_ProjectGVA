package Entities;

public class Utilisateur {
    private int id;
    private String nom;
    private String email;
    private String motDePasse;
    private String telephone;

    // Constructor for creating new users
    public Utilisateur(String nom, String email, String motDePasse, String telephone) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
    }

    // Constructor with ID for existing users
    public Utilisateur(int id, String nom, String email, String motDePasse, String telephone) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
