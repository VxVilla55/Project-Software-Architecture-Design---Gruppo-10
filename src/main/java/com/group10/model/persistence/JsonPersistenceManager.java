/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import com.group10.model.common.Subscriber;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author group10
 * 
 * Implementazione su file JSON di PersistenceManager (libreria Gson)
 *
 * Le tracce, essendo immutabili, vengono ricostruite tramite TrackBuilder
 * in fase di caricamento; le playlist salvano solo i riferimenti alle
 * tracce (titolo+autore) per non duplicarne i dati
 *
 */

public class JsonPersistenceManager implements PersistenceManager, Subscriber {

    private final Path catalogueFile;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    
    public JsonPersistenceManager() {
        this.catalogueFile = Paths.get("data", "catalogue.json");
    }

    
    // serializza lo stato corrente del catalogo sul file JSON
    @Override
    public void save() {
        
        try {
            Path parent = catalogueFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (Writer writer = Files.newBufferedWriter(catalogueFile)) {
                gson.toJson(toFile(MusicCatalogue.getInstance()), writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // legge il file JSON e popola il catalogo (resta vuoto se assente o corrotto)
    @Override
    public void load() {
        if (!Files.exists(catalogueFile)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(catalogueFile)) {
            CatalogueFile data = gson.fromJson(reader, CatalogueFile.class);
            if (data == null) {
                return;
            }
            Map<String, TrackComponent> byKey = new HashMap<>();
            for (TrackData t : data.tracks) {
                TrackComponent track = new TrackBuilder()
                        .setTitle(t.title)
                        .setAuthor(t.author)
                        .setDuration(t.duration)
                        .setGenre(t.genre)
                        .setYear(t.year)
                        .addAllTags(t.tags)
                        .setCoverImagePath(t.coverImagePath)
                        .build();

                MusicCatalogue.getInstance().addTrack(track);
                MusicCatalogue.getInstance().addGenre(t.genre);
                byKey.put(key(t.title, t.author), track);
            }
            for (PlaylistData p : data.playlists) {
                PlaylistComponent playlist = new PlaylistComponent(p.name);
                for (String trackKey : p.trackKeys) {
                    TrackComponent track = byKey.get(trackKey);
                    if (track != null) {
                        playlist.add(track);
                    }
                }
                MusicCatalogue.getInstance().addPlaylist(playlist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // converte il catalogo nella struttura dati salvabile su file
    private CatalogueFile toFile(MusicCatalogue catalogue) {
        CatalogueFile data = new CatalogueFile();
        for (TrackComponent track : catalogue.getTracks()) {
            TrackData t = new TrackData();
            t.title = track.getTitle();
            t.author = track.getAuthor();
            t.duration = track.getDurationInSeconds();
            t.genre = track.getGenre();
            t.year = track.getYear();
            t.tags.addAll(track.getTags());
            t.coverImagePath = track.getCoverImagePath();
            data.tracks.add(t);
        }
        for (PlaylistComponent playlist : catalogue.getPlaylists().values()) {
            PlaylistData p = new PlaylistData();
            p.name = playlist.getName();
            for (TrackComponent track : playlist.getTracks()) {
                p.trackKeys.add(key(track.getTitle(), track.getAuthor()));
            }
            data.playlists.add(p);
        }
        return data;
    }

    // chiave univoca di una traccia (titolo + autore)
    private String key(String title, String author) {
        return title + "::" + author;
    }

    @Override
    public void update() {
        save();
    }
}