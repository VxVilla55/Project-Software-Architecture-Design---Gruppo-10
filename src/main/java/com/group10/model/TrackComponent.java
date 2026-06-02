/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model;

import com.group10.model.builder.TrackBuilder;
import com.group10.model.common.Playable;
import com.group10.model.state.PlaybackEngine;
import com.group10.model.common.Playable;


/**
 *
 * @author group10
 *
 * Nel PATTERN COMPOSITE è l'elemento Composite:
 * rappresenta una traccia come insieme
 */

public class TrackComponent implements Playable {
    
    private final String title;
    private final String author;
    private final int duration;
    private final String genre;
    private final int year;

    //la creazione della Traccia effettiva avviene mediante TrackBuilder che comprende la logica di validazione
    public TrackComponent(TrackBuilder builder) {
        this.title = builder.getTitle();
        this.author = builder.getAuthor();
        this.duration = builder.getDuration();
        this.genre = builder.getGenre();
        this.year = builder.getYear();
    }
    
    public TrackComponent() {
        this.title = "--";
        this.author = "--";
        this.duration = 1;
        this.genre = "";
        this.year = 2026;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }
@Override
    public int getDurationInSeconds() {
        return this.duration; // Sostituisci "duration" col nome esatto della tua variabile
    }

    @Override
    public void playOnEngine(com.group10.model.state.PlaybackEngine engine) {
        engine.addTrackToQueue(this); // Aggiunge solo se stessa
    }
}