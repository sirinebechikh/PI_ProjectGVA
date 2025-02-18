package Entities;

import java.util.Date;

public class Evenement {
    // Attributes
    private int id;
    private String nom;
    private String type;
    private int nombreInvite;
    private Date dateDebut;
    private Date dateFin;
    private String description;
    private String lieuEvenement;
    private double budgetPrevu;
    private String activities;
    private String imagePath;
    private boolean validate; // ✅ New attribute for validation status

    // Constructors
    public Evenement() {}

    public Evenement(String nom, String type, int nombreInvite, Date dateDebut, Date dateFin,
                     String description, String lieuEvenement, double budgetPrevu, String activities, String imagePath, boolean validate) {
        this.nom = nom;
        this.type = type;
        this.nombreInvite = nombreInvite;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.lieuEvenement = lieuEvenement;
        this.budgetPrevu = budgetPrevu;
        this.activities = activities;
        this.imagePath = imagePath;
        this.validate = validate;
    }
    public Evenement(int id, String nom, String type, int nombreInvite, Date dateDebut, Date dateFin,
                     String description, String lieuEvenement, double budgetPrevu, String activities, String imagePath) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.nombreInvite = nombreInvite;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.lieuEvenement = lieuEvenement;
        this.budgetPrevu = budgetPrevu;
        this.activities = activities;
        this.imagePath = imagePath;

    }

    public Evenement(int id, String nom, String type, int nombreInvite, Date dateDebut, Date dateFin,
                     String description, String lieuEvenement, double budgetPrevu, String activities, String imagePath, boolean validate) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.nombreInvite = nombreInvite;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.lieuEvenement = lieuEvenement;
        this.budgetPrevu = budgetPrevu;
        this.activities = activities;
        this.imagePath = imagePath;
        this.validate = validate;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNombreInvite() {
        return nombreInvite;
    }

    public void setNombreInvite(int nombreInvite) {
        this.nombreInvite = nombreInvite;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLieuEvenement() {
        return lieuEvenement;
    }

    public void setLieuEvenement(String lieuEvenement) {
        this.lieuEvenement = lieuEvenement;
    }

    public double getBudgetPrevu() {
        return budgetPrevu;
    }

    public void setBudgetPrevu(double budgetPrevu) {
        this.budgetPrevu = budgetPrevu;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Getters and Setters
    public boolean isValidated() {
        return validate;
    }

    public void setValidated(boolean validate) {
        this.validate = validate;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", nombreInvite=" + nombreInvite +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", description='" + description + '\'' +
                ", lieuEvenement='" + lieuEvenement + '\'' +
                ", budgetPrevu=" + budgetPrevu +
                ", activities='" + activities + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", validate=" + validate +
                '}';
    }
}
