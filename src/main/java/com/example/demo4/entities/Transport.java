package org.example.models;

public class Transport {
    private int transportId;
    private String type;
    private double price;
    private String description;


    public Transport() {}

    public Transport(int transportId, String type, double price, String description) {
        this.transportId = transportId;
        this.type = type;
        this.price = price;
        this.description = description;

    }

    // Getters and Setters
    public int getTransportId() { return transportId; }
    public void setTransportId(int transportId) { this.transportId = transportId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

}