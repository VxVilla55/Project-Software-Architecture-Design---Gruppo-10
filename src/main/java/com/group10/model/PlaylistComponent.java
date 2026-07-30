/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model;

import com.group10.model.builder.PlaylistBuilder;
import com.group10.model.common.Playable;
import com.group10.model.common.Subscriber;
import com.group10.model.state.PlaybackEngine;
import com.group10.service.filter.TrackFilterStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Composite del pattern omonimo: rappresenta una playlist come insieme
 * ordinato di elementi Playable (Track).
 *
 * Due modalità alla costruzione:
 *  manuale (strategies vuota):
 *     tracks = staticTracks
 *
 *  automatica (strategies non vuota):
 *      tracks = staticTracks, aggiornata ad ogni lettura mediante applicazione delle strategie.
 *      Ad ogni getTracks(), le strategy vengono applicate al catalogo per trovare tracce nuove da aggiungere a staticTracks.
 *      Le strategy eseguono filtri sul catalogo per identificare tracce che soddisfano i criteri:
 *      - se la traccia è già in staticTracks, viene mantenuta (con il loro ordine),
 *      - se la traccia è in excludedTracks, viene ignorata (rimossa dalla playlist e non va riaggiunta anche se soddisfa le strategy),
 *      - se la traccia non è in staticTracks e non è in excludedTracks, viene aggiunta a staticTracks se soddisfa tutte le strategy così che possa essere riordinata come le altre.
 * 
 * PATTERN: Playable è il Component, Track è la Leaf, questa classe è il Composite.
 */
public class PlaylistComponent implements Playable, Comparable<PlaylistComponent>, Subscriber {

    private String name;
    private final List<TrackComponent> staticTracks;
    //per la parte di playlist riempite automaticamente
    private final List<TrackFilterStrategy> strategies;
    private final Set<TrackComponent> excludedTracks;
    //per le statistiche di riproduzione
    private int playCount;

    public PlaylistComponent() {
        this.name           = validateAndTrimName("Nuova Playlist");
        this.staticTracks   = new ArrayList<>();
        this.strategies     = new ArrayList<>();
        this.excludedTracks = new HashSet<>();
    }

    public PlaylistComponent(String name) {
        this.name           = validateAndTrimName(name);
        this.staticTracks   = new ArrayList<>();
        this.strategies     = new ArrayList<>();
        this.excludedTracks = new HashSet<>();
    }

    public PlaylistComponent(PlaylistBuilder builder) {
        this.name           = validateAndTrimName(builder.getName());
        this.staticTracks   = new ArrayList<>(builder.getTracks());
        this.strategies     = new ArrayList<>(builder.getStrategies());
        this.excludedTracks = new HashSet<>();
        this.playCount      = builder.getPlayCount();
        if (isAuto())
            MusicCatalogue.getInstance().addSubscriber(this);
    }
    
    public boolean isAuto() {
        return !strategies.isEmpty();
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

    //chiamato dal catalogo per la sincronizzazione automatica delle playlist con i nuovi brani che soddisfano le strategie
    @Override
    public void update() {
        syncFromCatalogue();
    }

    private void syncFromCatalogue() {
        Set<TrackComponent> existing = new HashSet<>(staticTracks);
        for (TrackComponent t : MusicCatalogue.getInstance().getTracks()) {
            if (existing.contains(t) || excludedTracks.contains(t)) continue;
            boolean matchesAll = true;
            for (TrackFilterStrategy s : strategies) {
                if (!s.matches(t)) {
                    matchesAll = false;
                    break;
                }
            }
            if (matchesAll) {
                staticTracks.add(t);
                existing.add(t);
            }
        }
    }

    public List<TrackComponent> getTracks() {
        if (isAuto())
            syncFromCatalogue();
        return staticTracks;
    }

    public void add(TrackComponent track) {
        //in AUTO rimuove anche l'eventuale esclusione
        if (isAuto())
            excludedTracks.remove(track);
        // aggiunge in coda a staticTracks 
        if (!staticTracks.contains(track))
            staticTracks.add(track);
    }

    // rimuove da staticTracks; in AUTO aggiunge anche a excludedTracks
    // così la sincronizzazione non la riporta mai più
    public boolean remove(TrackComponent track) {
        boolean removed = staticTracks.remove(track);
        if (isAuto()) {
            //vedo prima se quella traccia eliminata verrebbe riaggiunta dalle strategy
            boolean matchesAll = true;
            for (TrackFilterStrategy s : strategies) {
                if (!s.matches(track)) {
                    matchesAll = false;
                    break;
                }
            }
            //se verrebbe messa la aggiungo a quelle che le strategy devono escludere
            //in questo modod se rimuovo una traccia e poi la modifico facendola rientrare nei requisiti validati dalle strategie viene aggiunta dinamicamente.
            if (matchesAll) {
                excludedTracks.add(track);
            }
        }
        return removed;
    }

    public boolean contains(TrackComponent track) {
        return getTracks().contains(track);
    }

    public boolean isEmpty() {
        return getTracks().isEmpty();
    }

    // funziona in entrambe le modalità: staticTracks è sempre la sorgente
    public void moveTrack(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex >= staticTracks.size()
                || toIndex < 0 || toIndex >= staticTracks.size()) return;
        TrackComponent t = staticTracks.remove(fromIndex);
        staticTracks.add(toIndex, t);
    }

    public void updateTrack(TrackComponent oldTrack, TrackComponent updatedTrack) {
        int index = staticTracks.indexOf(oldTrack);

        if (index != -1)
            staticTracks.set(index, updatedTrack);

        if (excludedTracks.remove(oldTrack))
            excludedTracks.add(updatedTrack);
    }

  //playcount per le statistiche di riproduzione
    public int getPlayCount(){ 
        return playCount;
    }
    public void incrementPlayCount() {
        playCount++;
    }
    public void setPlayCount(int count) {
        playCount = count;
    }


    //dall0interfacciaPlayable
    @Override
    public int getDurationInSeconds() {
        int total = 0;
        for (TrackComponent t : getTracks()) total += t.getDurationInSeconds();
        return total;
    }

    @Override
    public void playOnEngine(PlaybackEngine engine) {
        engine.setCurrentPlaylist(this);
        engine.addListToQueue(new ArrayList<>(getTracks()));
    }

    @Override
    public int compareTo(PlaylistComponent other) {
        if (other == null) return 1;
        return this.name.compareTo(other.name);
    }

    private static String validateAndTrimName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della playlist non può essere vuoto o nullo");
        }
        return name.trim();
    }
}