package org.example.dao;

import org.example.models.Evenement;

import java.util.Optional;

public interface IEventDAO {
    Optional<Evenement> findEventById(int id);
}