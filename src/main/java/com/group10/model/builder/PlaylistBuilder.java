/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model.builder;

import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.common.Builder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author group10
 *
 * PATTERN BUILDER
 * Costruisce passo-passo una PlaylistComponent. Per ora supporta la sola 
 * modalità manuale, si forniscono un nome e la lista di tracce scelte 
 * dall'utente, poi build() restituisce la playlist pronta.
 * 
 */

public class PlaylistBuilder implements Builder<PlaylistComponent> {

    private String name;
    private final List<TrackComponent> tracks;

    public PlaylistBuilder() {
        this.name = "Nuova Playlist";   
        this.tracks = new ArrayList<>();
    }

    // imposta il nome della playlist da costruire
    public PlaylistBuilder setName(String name) {
        this.name = name.trim();
        return this;
    }
    public String getName() {
        return name;
    }
    
    // aggiunge una singola traccia scelta dall'utente
    public PlaylistBuilder addTrack(TrackComponent track) {
        this.tracks.add(track);
        return this;
    }

    // aggiunge in blocco una lista di tracce scelte dall'utente (modalità manuale)
    public PlaylistBuilder addTracks(Set<TrackComponent> selectedTracks) {
        this.tracks.addAll(selectedTracks);
        return this;
    }
    public PlaylistBuilder addTracks(List<TrackComponent> selectedTracks) {
        this.tracks.addAll(selectedTracks);
        return this;
    }
    
    public List<TrackComponent> getTracks() {
        return tracks;
    }

    // costruisce la playlist con il nome e le tracce accumulati finora
    @Override
    public PlaylistComponent build() {
        //controllo regola: il nome non può essere vuoto
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della playlist non puo' essere vuoto");
        }

        //il nome non può essere duplicato nell'app
        /*if (MusicCatalogue.getInstance().isPlaylistNameTaken(name)) {
            throw new IllegalArgumentException("Esiste già una playlist con questo nome");
        }*/
        return new PlaylistComponent(this);
    }
}