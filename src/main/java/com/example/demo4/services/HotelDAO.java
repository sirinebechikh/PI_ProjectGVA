package org.example.dao;

import org.example.models.Hotel;

import java.util.List;

public interface HotelDAO {
    List<Hotel> findAll();
    Hotel findById(int id);
    List<Hotel> findByLocation(String location);
}