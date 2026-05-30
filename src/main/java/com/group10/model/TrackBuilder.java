package com.group10.model;

public class TrackBuilder implements Builder<TrackComponent> {
    
    private String title;
    private String author;
    private int duration;
    private String genre = ""; 
    private int year = 2026; 

    public TrackBuilder title(String title) {
        this.title = title;
        return this;
    }

    public TrackBuilder author(String author) {
        this.author = author;
        return this;
    }

    public TrackBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    public TrackBuilder genre(String genre) {
        this.genre = genre;
        return this;
    }

    public TrackBuilder year(int year) {
        this.year = year;
        return this;
    }

    // Getter per permettere a TrackComponent di leggere i dati durante la creazione
    String getTitle() { return title; }
    String getAuthor() { return author; }
    int getDuration() { return duration; }
    String getGenre() { return genre; }
    int getYear() { return year; }

    @Override
    public TrackComponent build() {
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