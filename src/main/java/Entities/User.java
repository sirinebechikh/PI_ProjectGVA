package Entities;

public class User {
    // Attributes
    private int id;
    private String nom;
    private String email;
    private String motDePasse;
    private String telephone;
    private RoleType role;
    private boolean compteValide;

    // Constructors
    public User() {}

    public User(String nom, String email, String motDePasse, String telephone, RoleType role, boolean compteValide) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
        this.role = role;
        this.compteValide = compteValide;
    }

    public User(int id, String nom, String email, String motDePasse, String telephone, RoleType role, boolean compteValide) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
        this.role = role;
        this.compteValide = compteValide;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public boolean isCompteValide() {
        return compteValide;
    }

    public void setCompteValide(boolean compteValide) {
        this.compteValide = compteValide;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", role=" + role +
                ", compteValide=" + compteValide +
                '}';
    }
}
