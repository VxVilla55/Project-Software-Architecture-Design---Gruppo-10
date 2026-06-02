/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model.builder;

import com.group10.model.TrackComponent;
import com.group10.model.common.Builder;

/**
 *
 * @author group10
 *
 * PATTERN BUILDER
 * Costruisce passo-passo una TrackComponent. 
 * Riceve i parametri della Component, li valida
 * e in caso di validità la build() restituisce la TrackComponent pronta.
 * 
 */

public class TrackBuilder implements Builder<TrackComponent> {
    
    private String title;
    private String author;
    private int duration;
    private String genre; 
    private int year = 2026; 

    public TrackBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public TrackBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public TrackBuilder setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public TrackBuilder setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public TrackBuilder setYear(int year) {
        this.year = year;
        return this;
    }

    //getter per permettere a TrackComponent di accedere alle sue proprietà
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public int getDuration() {
        return duration;
    }
    public String getGenre() {
        return genre;
    }
    public int getYear() {
        return year;
    }

    @Override
    public TrackComponent build() {
        //validazione dei campi
        if (this.title == null || this.title.trim().isEmpty()) {
            throw new IllegalStateException("Errore: Il titolo e' obbligatorio.");
        }
        
        if (this.author == null || this.author.trim().isEmpty()) {
            throw new IllegalStateException("Errore: L'autore e' obbligatorio.");
        }
        
        if (this.duration <= 0) {
            throw new IllegalStateException("Errore: La durata deve essere maggiore di 0.");
        }

        if (this.year < 1000 || this.year > 2100) {
            throw new IllegalStateException("Errore: Anno di pubblicazione non valido.");
        }

        return new TrackComponent(this);
    }
}