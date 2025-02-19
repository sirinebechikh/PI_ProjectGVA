package org.example.models;

import java.sql.Timestamp;

public class Flight {
    private int flightId;
    private String destination;
    private String airline;
    private Timestamp departureTime;
    private double price;

    public Flight() {}

    public Flight(int flightId, String destination, String airline, Timestamp departureTime, double price) {
        this.flightId = flightId;
        this.destination = destination;
        this.airline = airline;
        this.departureTime = departureTime;
        this.price = price;
    }

    // Getters and Setters
    public int getFlightId() { return flightId; }
    public void setFlightId(int flightId) { this.flightId = flightId; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getAirline() { return airline; }
    public void setAirline(String airline) { this.airline = airline; }

    public Timestamp getDepartureTime() { return departureTime; }
    public void setDepartureTime(Timestamp departureTime) { this.departureTime = departureTime; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}