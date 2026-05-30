package com.group10.model;

public class TrackComponent implements Playable {
    
    private final String title;
    private final String author;
    private final int duration;
    private final String genre;
    private final int year;

    // Il costruttore accetta il TrackBuilder esterno
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