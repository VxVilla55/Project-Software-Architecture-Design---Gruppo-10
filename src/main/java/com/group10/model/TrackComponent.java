package com.group10.model;

import com.group10.model.builder.TrackBuilder;
import com.group10.model.common.Playable;
import com.group10.model.state.PlaybackEngine;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author group10
 *
 * Nel PATTERN COMPOSITE è l'elemento Composite:
 * rappresenta una traccia come insieme
 */
public class TrackComponent implements Comparable<TrackComponent>, Playable {

    public enum Tag {
        FAVORITE, EXPLICIT, NEW_RELEASE
    }

    private final String title;
    private final String author;
    private final int duration;
    private final String genre;
    private final int year;
    private final Set<Tag> tags;
    private int playCount;
    private final String coverImagePath;

    public TrackComponent(TrackBuilder builder) {
        this.title = builder.getTitle();
        this.author = builder.getAuthor();
        this.duration = builder.getDuration();
        this.genre = builder.getGenre();
        this.year = builder.getYear();
        this.tags = builder.getTags();
        this.playCount = builder.getPlayCount();
        this.coverImagePath = builder.getCoverImagePath();
    }

    public TrackComponent() {
        this.title = "--";
        this.author = "--";
        this.duration = 1;
        this.genre = "";
        this.year = 2026;
        this.tags = new HashSet<>();
        this.playCount = 0;
        this.coverImagePath = null;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public int getYear() { return year; }
    public String getCoverImagePath() { return coverImagePath; }

    public Set<Tag> getTags() {
        return this.tags;
    }
    
    public void addTags(ArrayList<Tag> tags) {
        this.tags.addAll(tags);
    }
    
    public void removeTags(ArrayList<Tag> tags) {
        this.tags.removeAll(tags);
    }
    
    public boolean hasTag(Tag tag) {
        return this.tags != null && this.tags.contains(tag);
    }
    
    @Override
    public int getDurationInSeconds() {
        return this.duration; 
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
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
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
        if (this.title.compareToIgnoreCase(o.getTitle()) != 0) {
            return this.title.compareToIgnoreCase(o.getTitle());
        }
        if (this.author.compareToIgnoreCase(o.getAuthor()) != 0) {
            return this.author.compareToIgnoreCase(o.getAuthor());
        }
        return 0;
    } 
    public int getPlayCount() {
        return this.playCount;
    }
    public void incrementPlayCount() {
        this.playCount++;
    }
    
    @Override
    public void playOnEngine(PlaybackEngine engine) {
        engine.addTrackToQueue(this);
    }
}