/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.service.filter;

import com.group10.model.TrackComponent;

/**
 *
 * @author group10
 * PATTERN: Strategy. ConcreteStrategy, accetta le tracce con un certo genere.
 */
public class GenreFilterStrategy  implements TrackFilterStrategy {
    private final String genre;

    public GenreFilterStrategy (String genre) {
        this.genre = genre;
    }
    
    @Override
    public boolean matches(TrackComponent track) {
        return genre.equalsIgnoreCase(track.getGenre());
    }
}