/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model;

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
    TrackComponent(TrackBuilder builder) {
        this.title = builder.getTitle();
        this.author = builder.getAuthor();
        this.duration = builder.getDuration();
        this.genre = builder.getGenre();
        this.year = builder.getYear();
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public int getDurationInSeconds() {
        return duration;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }
}