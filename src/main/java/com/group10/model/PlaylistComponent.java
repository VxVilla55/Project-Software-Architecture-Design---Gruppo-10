/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model;

import com.group10.model.builder.PlaylistBuilder;
import com.group10.model.common.Playable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author group10
 * 
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
    private final Set<TrackComponent> tracks;

    public PlaylistComponent() {
        this.name = "Nuova Playlist";
        this.tracks = new TreeSet<>();
    }
    
    public PlaylistComponent(PlaylistBuilder builder) {
        
        this.name = builder.getName();
        this.tracks = new TreeSet<>(builder.getTracks());
    }
    
    public PlaylistComponent(String name) {
        this.tracks = new TreeSet<>();
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
    
    public Set<TrackComponent> getTracks() {
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
    
    //true se contiene la traccia indicata
    public boolean contains(TrackComponent track) {
        return tracks.contains(track);
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
    @Override
    public int getDurationInSeconds() {
        int totalDuration = 0;
        for (TrackComponent track : this.tracks) {
            totalDuration += track.getDurationInSeconds();
        }
        return totalDuration;
    }

    @Override
    public void playOnEngine(com.group10.model.state.PlaybackEngine engine) {
        for (TrackComponent track : this.tracks) {
            track.playOnEngine(engine);
        }
    }
}
    




