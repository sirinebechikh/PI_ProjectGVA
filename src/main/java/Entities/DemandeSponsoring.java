package Entities;

public class DemandeSponsoring {
    private int id;
    private String sponsor; // Name or ID of the sponsor
    private int eventId; // Event to be sponsored
    private String statut; // "Pending", "Accepted", "Rejected"
    private String justification; // Reason for sponsoring

    // Constructor
    public DemandeSponsoring(int id, String sponsor, int eventId, String statut, String justification) {
        this.id = id;
        this.sponsor = sponsor;
        this.eventId = eventId;
        this.statut = statut;
        this.justification = justification;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSponsor() { return sponsor; }
    public void setSponsor(String sponsor) { this.sponsor = sponsor; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }

    @Override
    public String toString() {
        return "DemandeSponsoring{" +
                "id=" + id +
                ", sponsor='" + sponsor + '\'' +
                ", eventId=" + eventId +
                ", statut='" + statut + '\'' +
                ", justification='" + justification + '\'' +
                '}';
    }
}
