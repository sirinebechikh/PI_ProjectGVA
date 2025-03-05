package org.example.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.models.Hotel;

import java.io.IOException;
import java.util.List;

public interface HotelDAO {
    List<Hotel> findAll();
    Hotel findById(int id);
    List<Hotel> findByLocation(String location) throws IOException, InterruptedException;
    List<Hotel> findByRating(int rating); // New method for filtering by rating

}