package org.example.dao;

import org.example.models.Transport;

import java.util.List;

public interface ITransportDAO {
    List<Transport> findAll();
    Transport findById(int id);
}