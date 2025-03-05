package org.example.dao;

import org.example.models.Flight;
import java.sql.Timestamp;
import java.util.List;

public interface FlightDAO {
    List<Flight> findAll();
    Flight findById(int id);
    List<Flight> findByDestination(String destination);
    List<Flight> findByDestinationAndDates(String destination, Timestamp departureDate, Timestamp returnDate);
}