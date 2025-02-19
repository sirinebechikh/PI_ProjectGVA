package org.example.models;

public class Hotel {
    private int hotelId;
    private String location;
    private String name;
    private double pricePerNight;
    private int rating;

    public Hotel() {}

    public Hotel(int hotelId, String location, String name, double pricePerNight, int rating) {
        this.hotelId = hotelId;
        this.location = location;
        this.name = name;
        this.pricePerNight = pricePerNight;
        this.rating = rating;
    }

    // Getters and Setters
    public int getHotelId() { return hotelId; }
    public void setHotelId(int hotelId) { this.hotelId = hotelId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

}