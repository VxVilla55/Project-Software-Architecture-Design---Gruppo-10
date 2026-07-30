/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model.persistence;


/**
 *
 * @author group10
 * Astrae il salvataggio e il caricamento del catalogo, cosi' il resto dell'app non
 * dipende dal formato concreto usato per persistere i dati.
 */
public interface PersistenceManager {
 
    void save();
 
    void load();
}
