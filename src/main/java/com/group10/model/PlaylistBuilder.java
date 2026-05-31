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
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della playlist non può essere vuoto o nullo");
        }
        this.name = name.trim();
        return this;
    }

    // aggiunge una singola traccia scelta dall'utente
    public PlaylistBuilder addTrack(TrackComponent track) {
        this.tracks.add(track);
        return this;
    }

    // aggiunge in blocco una lista di tracce scelte dall'utente (modalità manuale)
    public PlaylistBuilder addTracks(List<TrackComponent> selectedTracks) {
        this.tracks.addAll(selectedTracks);
        return this;
    }

    // costruisce la playlist con il nome e le tracce accumulati finora
    @Override
    public PlaylistComponent build() {
        //controllo regola: il nome non può essere vuoto
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della playlist non puo' essere vuoto");
        }

        //il nome non può essere duplicato nell'app
        if (MusicCatalogue.getInstance().isPlaylistNameTaken(name)) {
            throw new IllegalArgumentException("Esiste già una playlist con questo nome");
        }

        PlaylistComponent playlist = new PlaylistComponent(name);
        for (TrackComponent track : tracks) {
            playlist.add(track);
        }
        return playlist;
    }
}