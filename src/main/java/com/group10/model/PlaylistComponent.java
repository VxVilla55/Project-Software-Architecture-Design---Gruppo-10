/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model;

import com.group10.model.builder.PlaylistBuilder;
import com.group10.model.common.Playable;
import com.group10.model.state.PlaybackEngine;
import com.group10.service.strategy.TrackFilterStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
public class PlaylistComponent implements Playable,Comparable<PlaylistComponent> {

    private String name;

    // lista ordinata delle tracce: l'ordine di inserimento è l'ordine di riproduzione
    //private Set<TrackComponent> tracks;
    private List<TrackComponent> staticTracks; //elenco statico di playlist
    private List<TrackFilterStrategy> strategies = new ArrayList<>(); //elenco dinamico (se creata automaticamente)

    public PlaylistComponent() {
        this.name = validateAndTrimName("Nuova Playlist");
    }
    
    public PlaylistComponent(String name) {
        this.name = validateAndTrimName(name);
        this.staticTracks = new ArrayList<>();
        this.strategies = new ArrayList<>();
    }
    
    /*public PlaylistComponent(String name, List<TrackComponent> tracks) {
        this.name = validateAndTrimName(name);
        this.staticTracks = new ArrayList<>(tracks);
        this.strategies = Collections.emptyList();
    }
    public PlaylistComponent(String name, List<TrackFilterStrategy> strategies) {
        this.name = validateAndTrimName(name);
        this.staticTracks = null;
        this.strategies = new ArrayList<>(strategies);
    }*/
    public PlaylistComponent(PlaylistBuilder builder) {
        this.name = validateAndTrimName(builder.getName());
        this.staticTracks = new ArrayList<>(builder.getTracks());
        this.strategies = new ArrayList<>(builder.getStrategies());
    }


    public boolean isAuto() {
        return strategies != null && !strategies.isEmpty();
    }
    
 
    public String getName() {
        return name;
    }
    
    public void setName(String newName) {
        this.name = validateAndTrimName(newName);
    }
    
    public int getSize() {
        return getTracks().size();
    }
    /*
    public List<TrackComponent> getTracks() {
        if (isAuto()) {
            // ricalcola ogni volta dal catalogo live
            return MusicCatalogue.getInstance().getTracks().stream()
                .filter(t -> strategies.stream().allMatch(s -> s.matches(t)))
                .collect(Collectors.toList());
        }
        return Collections.unmodifiableList(staticTracks);
    }*/
    public List<TrackComponent> getTracks() {
        if (strategies.isEmpty()) {
            return Collections.unmodifiableList(staticTracks);
        }
        //lista di unione: tracce filtrate + tracce aggiunte manualmente
        List<TrackComponent> result = new ArrayList<>(staticTracks);

        //soluzione migliore per evitare duplicati
        Set<TrackComponent> existingTracks = new java.util.HashSet<>(result);
        for (TrackComponent t : MusicCatalogue.getInstance().getTracks()) {
            //flag per verificare se la traccia passa TUTTE le strategie
            boolean matchesAll = true;
            //applichiamo tutti i filtri
            for (TrackFilterStrategy s : strategies) {
                //appena ne fallisce uno la traccia può essere skippata
                if (!s.matches(t)) {
                    matchesAll = false; 
                    break;
                }
            }
            //skip delle traccia se non ha passato un filtro o è già presente
            if (matchesAll && !existingTracks.contains(t)) {
                result.add(t);
                existingTracks.add(t);
            }
        }
        return result;
    }
    
    // aggiunge una traccia in coda
    public void add(TrackComponent track) {
        if (!contains(track)) {
            staticTracks.add(track);
        }
    }
  
    // rimuove la traccia indicata
    public boolean remove(TrackComponent track) {
        return staticTracks.remove(track);
    }
    
    //true se contiene la traccia indicata
    public boolean contains(TrackComponent track) {
        return getTracks().contains(track);
    }
  
    public boolean isEmpty() {
        return getTracks().isEmpty();
    }


    public int getPlayCount() {
        // Soluzione moderna ed elegante con gli Stream di Java 8
        return this.getTracks().stream()
                   .mapToInt(TrackComponent::getPlayCount)
                   .sum();        
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
        for (TrackComponent track : this.getTracks()) {
            totalDuration += track.getDurationInSeconds();
        }
        return totalDuration;
    }
    @Override
    public void playOnEngine(PlaybackEngine engine) {
        engine.setCurrentPlaylist(this);
        System.out.println("Playlist settata: " + getName());
        /* for (TrackComponent track : this.tracks) {
            track.playOnEngine(engine);
        } */
        engine.addListToQueue(new ArrayList<>(this.getTracks()));
    }

    
    public int compareTo(PlaylistComponent other) {
        if (other == null) return 1;
        return this.name.compareTo(other.name); // Ordina alfabeticamente per nome
    }
    
    public void updateTrack(TrackComponent oldTrack, TrackComponent updatedTrack) {
        int index = staticTracks.indexOf(oldTrack);
        if (index != -1) {
            staticTracks.set(index, updatedTrack);
        }
    }
    public void moveTrack(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex >= staticTracks.size() || toIndex < 0 || toIndex >= staticTracks.size()) 
            return;

        TrackComponent trackToMove = staticTracks.remove(fromIndex);
        staticTracks.add(toIndex, trackToMove);
    }
}
    




