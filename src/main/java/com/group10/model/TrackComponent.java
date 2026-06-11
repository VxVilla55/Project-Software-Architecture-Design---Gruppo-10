/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model;

import com.group10.model.builder.TrackBuilder;
import com.group10.model.common.Playable;
import com.group10.model.state.PlaybackEngine;
import com.group10.model.common.Playable;
import java.util.Objects;


/**
 *
 * @author group10
 *
 * Nel PATTERN COMPOSITE è l'elemento Composite:
 * rappresenta una traccia come insieme
 */

public class TrackComponent implements Comparable<TrackComponent>, Playable {
    
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
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.title);
        hash = 29 * hash + Objects.hashCode(this.author);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        //se sono lo stesso identico oggetto in memoria, sono uguali
        if (this == obj) return true;
        
        //se l'altro oggetto è nullo o è di una classe diversa, non sono uguali
        if (obj == null || getClass() != obj.getClass()) return false;
        
        //confrontiamo titolo e autore
        TrackComponent other = (TrackComponent) obj;
        
        boolean isTitleEqual = (this.getTitle() != null && this.getTitle().equals(other.getTitle()));
        boolean isAuthorEqual = (this.getAuthor() != null && this.getAuthor().equals(other.getAuthor()));
        
        return isTitleEqual && isAuthorEqual;
    }
    
    @Override
    public int compareTo(TrackComponent o) {
        if (this.equals(o)) {
            return 0;
        }
        //prima confronto il titolo
        if (this.title.compareToIgnoreCase(o.getTitle()) != 0) {
            return this.title.compareToIgnoreCase(o.getTitle());
        }
        //poi confronto gli autori
        if (this.author.compareToIgnoreCase(o.getAuthor()) != 0) {
            return this.author.compareToIgnoreCase(o.getAuthor());
        }
        return 0;
    } 
}