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
 */

public class JsonPersistenceManager implements PersistenceManager {

    private static final Path DATA_DIR = Paths.get("data");
    private static final Path CATALOGUE_FILE = DATA_DIR.resolve("catalogue.json");

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void save(MusicCatalogue catalogue) {
        try {
            Files.createDirectories(DATA_DIR);
            try (Writer writer = Files.newBufferedWriter(CATALOGUE_FILE)) {
                gson.toJson(toFile(catalogue), writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(MusicCatalogue catalogue) {
        if (!Files.exists(CATALOGUE_FILE)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(CATALOGUE_FILE)) {
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
                        .build();
                catalogue.addTrack(track);
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
                catalogue.addPlaylist(playlist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CatalogueFile toFile(MusicCatalogue catalogue) {
        CatalogueFile data = new CatalogueFile();
        for (TrackComponent track : catalogue.getTracks()) {
            TrackData t = new TrackData();
            t.title = track.getTitle();
            t.author = track.getAuthor();
            t.duration = track.getDurationInSeconds();
            t.genre = track.getGenre();
            t.year = track.getYear();
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

    private String key(String title, String author) {
        return title + "::" + author;
    }
}