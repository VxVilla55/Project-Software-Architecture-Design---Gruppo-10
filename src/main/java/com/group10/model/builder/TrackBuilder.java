package com.group10.model.builder;

import java.util.HashSet;
import java.util.Set;

import com.group10.model.TrackComponent;
import com.group10.model.common.Builder;

// IL TRUCCO È QUI: Importiamo l'Enum annidato dentro TrackComponent
import com.group10.model.TrackComponent.Tag; 

/**
 * @author group10
 *
 * PATTERN BUILDER
 */
public class TrackBuilder implements Builder<TrackComponent> {
    
    private String title;
    private String author;
    private int duration;
    private String genre; 
    private int year = 2026; 
    
    private Set<Tag> tags = new HashSet<>();

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

    public TrackBuilder addTag(Tag tag) {
        if (tag != null) {
            this.tags.add(tag);
        }
        return this;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getDuration() { return duration; }
    public String getGenre() { return genre; }
    public int getYear() { return year; }
    
    public Set<Tag> getTags() {
        return tags;
    }

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