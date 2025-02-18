package org.example;

import Entities.Utilisateur;
import Service.ServiceUtilisateur;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ServiceUtilisateur service = new ServiceUtilisateur();

        // Create a new user
        Utilisateur newUser = new Utilisateur("John Doe", "joh.doe@example.com", "password123", "1234567890");
        service.create(newUser);
        System.out.println("🔹 Utilisateur créé: " + newUser);

        // Read and display all users
        List<Utilisateur> utilisateurs = service.read();
        System.out.println("\n📋 Liste des utilisateurs:");
        for (Utilisateur u : utilisateurs) {
            System.out.println(u);
        }

        // Update the first user if exists
        if (!utilisateurs.isEmpty()) {
            Utilisateur userToUpdate = utilisateurs.get(0);
            userToUpdate.setNom("Jane Do");
            userToUpdate.setEmail("jane.doe@example.com");
            service.update(userToUpdate);
            System.out.println("\n🔄 Utilisateur mis à jour: " + userToUpdate);
        }
/*
        // Delete the first user if exists
        if (!utilisateurs.isEmpty()) {
            int idToDelete = utilisateurs.get(0).getId();
            service.delete(idToDelete);
            System.out.println("\n🗑️ Utilisateur avec ID " + idToDelete + " supprimé.");
        }*/
    }
}
