package com.group10.model.builder;

import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.common.Builder;
import com.group10.service.filter.TrackFilterStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author group10
 *
 * PATTERN BUILDER
 * Costruisce passo-passo una PlaylistComponent. Supporta sia la modalità manuale
 * (si forniscono un nome e la lista di tracce scelte dall'utente) sia quella
 * automatica (si aggiungono delle TrackFilterStrategy); build() restituisce la
 * playlist pronta.
 */

public class PlaylistBuilder implements Builder<PlaylistComponent> {

    private String name;
    private final List<TrackComponent> tracks;
    private final List<TrackFilterStrategy> strategies;
    private int playCount;

    public PlaylistBuilder() {
        this.name = "Nuova Playlist";   
        this.tracks = new ArrayList<>();
        this.strategies = new ArrayList<>();
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

    public PlaylistBuilder addStrategy(TrackFilterStrategy strategy) {
        this.strategies.add(strategy);
        return this;
    }

    // conserva il conteggio di riproduzione (es. quando si rinomina una playlist)
    public PlaylistBuilder setPlayCount(int playCount) {
        this.playCount = playCount;
        return this;
    }
    public int getPlayCount() {
        return playCount;
    }

    public List<TrackComponent> getTracks() {
        return tracks;
    }
    public List<TrackFilterStrategy> getStrategies() {
        return strategies;
    }

    // costruisce la playlist con il nome e le tracce accumulati finora
    @Override
    public PlaylistComponent build() {
        //controllo regola: il nome non può essere vuoto
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della playlist non puo' essere vuoto");
        }

        return new PlaylistComponent(this);
    }
}