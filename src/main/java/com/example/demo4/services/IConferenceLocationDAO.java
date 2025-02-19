package org.example.dao;

import org.example.models.ConferenceLocation;

import java.util.List;

public interface IConferenceLocationDAO {
    List<ConferenceLocation> findAll();
    ConferenceLocation findById(int id);

    List<ConferenceLocation> findByLocation(String location);
}