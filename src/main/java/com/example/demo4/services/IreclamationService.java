/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temreclamatione file, choose Tools | Temreclamationes
 * and open the temreclamatione in the editor.
 */
package com.example.demo4.services;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author asus
 */
public interface IreclamationService<T> {
    
       public void ajouterreclamation(T t) throws SQLException;
    public void modifierreclamation(T t) throws SQLException;
    public void supprimerreclamation(T t) throws SQLException;
    public List<T> recupererreclamation() throws SQLException;
    
}
