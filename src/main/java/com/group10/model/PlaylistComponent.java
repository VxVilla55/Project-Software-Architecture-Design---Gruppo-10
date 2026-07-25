/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model;

import com.group10.model.builder.PlaylistBuilder;
import com.group10.model.common.Playable;
import com.group10.model.state.PlaybackEngine;
import com.group10.service.filter.TrackFilterStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
    private int playCount; //quante volte la playlist è stata avviata o accodata

    public PlaylistComponent() {
        this.name = validateAndTrimName("Nuova Playlist");
        this.staticTracks = new ArrayList<>();
        this.strategies = new ArrayList<>();
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
        this.playCount = builder.getPlayCount();
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

        // Teniamo traccia delle esistenti per evitare duplicati
        Set<TrackComponent> existingTracks = new HashSet<>(staticTracks);

        for (TrackComponent t : MusicCatalogue.getInstance().getTracks()) {
            if (existingTracks.contains(t)) continue;

            boolean matchesAll = true;
            for (TrackFilterStrategy s : strategies) {
                if (!s.matches(t)) {
                    matchesAll = false;
                    break;
                }
            }

            if (matchesAll) {
                staticTracks.add(t);
                existingTracks.add(t);
            }
        }

        return staticTracks;
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


    // conteggio proprio della playlist: quante volte è stata avviata o accodata
    public int getPlayCount() {
        return this.playCount;
    }

    public void incrementPlayCount() {
        this.playCount++;
    }

    // usato dalla persistenza per ripristinare il conteggio salvato
    public void setPlayCount(int playCount) {
        this.playCount = playCount;
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
        // il Composite si riproduce accodando in blocco le proprie tracce
        engine.setCurrentPlaylist(this);
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
    




