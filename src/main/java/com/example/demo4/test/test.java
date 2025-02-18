/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temreclamatione file, choose Tools | Temreclamationes
 * and open the temreclamatione in the editor.
 */
package com.example.demo4.test;

import java.sql.Date;
import java.sql.SQLException;

import com.example.demo4.services.reclamationService;
import com.example.demo4.services.reponseService;


/**
 *
 * @author asus
 */
public class test {
    
      public static void main(String[] args) {   
          
          Date d=Date.valueOf("2022-06-11");
          Date d1=Date.valueOf("2020-04-12");
        try {
            //kifeh ya9ra el orde fel base de donnée , kifeh 3raf nom ev bch n3amarha f nom 

            
            


            reponseService ps=new reponseService();
            //ps.reponse(p);
          //  ps.reponse(p1);
           // ps.reponse(p2);

            //ps.reponse(p2);
            System.out.println("");
            reclamationService ab = new reclamationService();
            //ab.ajouterreclamation(e1);
            //ab.ajouterreclamation(e2);
           // ab.ajouterreclamation(e3);
            //ab.ajouter(p);
            //ab.modifierreclamation(e);
            //ab.supprimerreclamation(e3);
            System.out.println(ab.recupererreclamation());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
