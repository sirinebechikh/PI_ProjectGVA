package org.example.dao;

import org.example.models.Flight;

import java.util.List;

public interface FlightDAO {
    List<Flight> findAll();
    Flight findById(int id);
    List<Flight> findByDestination(String destination);
}