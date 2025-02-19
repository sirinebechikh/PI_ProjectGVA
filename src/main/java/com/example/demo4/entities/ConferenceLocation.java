package org.example.models;

public class ConferenceLocation {
    private int locationId;
    private String name;
    private String address;
    private int capacity;
    private double pricePerDay;

    public ConferenceLocation() {}

    public ConferenceLocation(int locationId, String name, String address, int capacity, double pricePerDay) {
        this.locationId = locationId;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.pricePerDay = pricePerDay;
    }

    // Getters and Setters
    public int getLocationId() { return locationId; }
    public void setLocationId(int locationId) { this.locationId = locationId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public double getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }
}