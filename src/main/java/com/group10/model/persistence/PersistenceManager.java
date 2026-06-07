/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model.persistence;

import com.group10.model.MusicCatalogue;


/**
 *
 * @author group10
 */

public interface PersistenceManager {

    void save(MusicCatalogue catalogue);

    CatalogueData load();
}
