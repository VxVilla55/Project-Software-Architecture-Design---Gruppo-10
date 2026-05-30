/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author group10
 * Composite del pattern omonimo: rappresenta una playlist come insieme
 * ordinato di elementi Playable (Track)
 *
 * L'ordine di inserimento coincide con l'ordine di riproduzione; 
 *
 * PATTERN: Playable è il Component,Track è la Leaf, questa classe è il Composite
 */
public class PlaylistComponent implements Playable {

    private String name;

    // lista ordinata delle tracce: l'ordine di inserimento è l'ordine di riproduzione
    private final List<TrackComponent> tracks;

    public PlaylistComponent() {
        this.name = "Nuova Playlist";
        this.tracks = new ArrayList<>();
    }
    
    public PlaylistComponent(String name) {
        this.tracks = new ArrayList<>();
        this.name = validateAndTrimName(name);
    }
 
    public String getName() {
        return name;
    }
    
    public void setName(String newName) {
        this.name = validateAndTrimName(newName);
    }
    
    public int getSize() {
        return tracks.size();
    }
    
    @Override
    public int getDurationInSeconds() {
        int total = 0;
        for (TrackComponent track : tracks) {
            total += track.getDurationInSeconds();
        }
        return total;
    }
    
    public List<TrackComponent> getTracks() {
        return tracks; 
    }
    
    // aggiunge una traccia in coda
    public void add(TrackComponent track) {
        tracks.add(track);
    }
  
    // rimuove la traccia indicata
    public boolean remove(TrackComponent track) {
        return tracks.remove(track);
    }
  
    public boolean isEmpty() {
        return tracks.isEmpty();
    }

    private static String validateAndTrimName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della playlist non può essere vuoto o nullo");
        }
        return name.trim();
    }
    
}
    




