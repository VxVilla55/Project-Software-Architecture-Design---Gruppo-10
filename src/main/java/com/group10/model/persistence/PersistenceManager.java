/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model.persistence;

import com.group10.model.MusicCatalogue;


/**
 *
 * @author group10
 * 
 * Astrazione per il salvataggio e il caricamento del catalogo:
 * disaccoppia MusicCatalogue dal formato di persistenza concreto
 *
 */

public interface PersistenceManager {
 
    void save(MusicCatalogue catalogue);
 
    void load(MusicCatalogue catalogue);
}
