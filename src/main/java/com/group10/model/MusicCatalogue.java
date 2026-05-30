/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alfon
 * 
 * Singleton: classe che modella lo stato dell'App
 */
public class MusicCatalogue implements Publisher{
    
    private static MusicCatalogue singleton;
    
    private List<TrackComponent> tracks;
    private List<PlaylistComponent> playlists;
    private List<Subscriber> subscribers;
    
    
    public MusicCatalogue() {
        tracks = new ArrayList<>();
        playlists = new ArrayList<>();
        subscribers = new ArrayList<>();
    }
    
    //metodo previsto dal pattern Singleton
    public static MusicCatalogue getInstance() {
        if (singleton == null) {
            singleton = new MusicCatalogue();
        }
        return singleton;
    }

    public void addTrack (TrackComponent track) {
        tracks.add(track);
    }
    public void removeTrack (TrackComponent track) {
        tracks.remove(track);
    }
    
    public void addPlaylist (PlaylistComponent playlist) {
        // univocita' del nome, non si aggiunge una playlist con un nome gia' presente
        if (isPlaylistNameTaken(playlist.getName())) {
            throw new IllegalArgumentException(
                    "Esiste già una playlist con questo nome: " + playlist.getName());
        }
        playlists.add(playlist);
    }
    public void removePlaylist (PlaylistComponent playlist) {
        playlists.remove(playlist);
    }

    // true se esiste gia' una playlist con questo nome (ignora maiuscole/minuscole e spazi)
    // il controller la chiama PRIMA di creare, per mostrare l'errore giusto all'utente
    public boolean isPlaylistNameTaken(String name) {
        if (name == null) {
            return false;
        }
        String newName = name.trim();
        for (PlaylistComponent playlist : playlists) {
            if (playlist.getName().equalsIgnoreCase(newName)) {
                return true;
            }
        }
        return false;
    }
    
    //per poter aggiungere nuovi Osservaroti/Subscriber a questo elemento
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }
    public void removeTracks(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers() {
        for (Subscriber s: subscribers) {
            s.update();
        }
    }    
}