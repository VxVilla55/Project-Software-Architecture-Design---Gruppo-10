/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group10.model;

/**
 *
 * @author group10
 * 
 * Classe Leaf del pattern Composite
 * Modella l'entità Traccia/Brano che possiamo riprodurre
 */
public class TrackComponent {
    private String title;       // necessario
    private String author;      // necessario
    private int duration;       // durata 
    private String genre;       // genere (opzionale)
    private int year;           // Anno di pubblicazione (opzionale)

    public TrackComponent(String title, String author, int duration, String genre, int releaseYear) {
        this.title = title;
        this.author = author;
        this.duration = duration;
        this.genre = genre;
        this.year = year;
    }

    //costruttore di default
    public TrackComponent() {
        title = "Nuova Traccia";
        author = "Autore";
        duration = 5;
        genre = "";
        year = 2026;
    }

    // metodi getter e setter
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return year;
    }
    public void setReleaseYear(int releaseYear) {
        this.year = year;
    }
}
