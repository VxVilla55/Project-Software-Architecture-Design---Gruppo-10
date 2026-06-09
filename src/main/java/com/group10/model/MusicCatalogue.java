/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model;

import com.group10.model.common.Publisher;
import com.group10.model.common.Subscriber;
import com.group10.model.persistence.JsonPersistenceManager;
import com.group10.model.persistence.PersistenceManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author group10
 * 
 * Singleton: classe che modella lo stato dell'App
 */
public class MusicCatalogue implements Publisher{
    
    private static MusicCatalogue singleton;
    
    private List<TrackComponent> tracks;
    private final Map<String, PlaylistComponent> playlists;
    private List<Subscriber> subscribers;
    private final PersistenceManager persistence = new JsonPersistenceManager();
    
    public MusicCatalogue() {
        tracks = new ArrayList<>();
        playlists = new TreeMap<>();
        subscribers = new ArrayList<>();
    }
    
    //metodo previsto dal pattern Singleton
    public static MusicCatalogue getInstance() {
        if (singleton == null) {
            singleton = new MusicCatalogue();
        }
        return singleton;
    }

    public List<TrackComponent> getTracks() {
        return this.tracks;
    }

    public Map<String, PlaylistComponent> getPlaylists() {
        return this.playlists;
    }

    public void addTrack (TrackComponent track) {
        tracks.add(track);
        save();
        notifySubscribers();
    }
   public void removeTrack(TrackComponent track) {
        // 1. CASCATA: Rimuovi la traccia da tutte le playlist esistenti
        for (PlaylistComponent playlist : playlists.values()) {
            playlist.remove(track); 
        }
        
        // 2. Rimuovi la traccia dalla libreria principale
        tracks.remove(track);
        save();

        // 3. Notifica gli iscritti (es. la grafica) che ci sono stati cambiamenti
        notifySubscribers();
    }
    
    public void addPlaylist (PlaylistComponent playlist) {
        // univocita' del nome, non si aggiunge una playlist con un nome gia' presente
        if (isPlaylistNameTaken(playlist.getName())) {
            throw new IllegalArgumentException(
                "Esiste già una playlist con questo nome: " + playlist.getName());
        }
        playlists.put(playlist.getName(), playlist);
        save();
        notifySubscribers();
    }
    public void removePlaylist (PlaylistComponent playlist) {
        playlists.remove(playlist);
        save();
        notifySubscribers();
    }
    
    public PlaylistComponent getPlaylist (String playlistName) {
        return playlists.get(playlistName);
    }
    
    public void addTrackToPlaylist(String playlistName, TrackComponent track) {
        PlaylistComponent playlist = getPlaylist(playlistName);
        playlist.add(track);
        save();
        notifySubscribers();
    }

    public void removeTrackFromPlaylist(String playlistName, TrackComponent track) {
        PlaylistComponent playlist = getPlaylist(playlistName);
        playlist.remove(track);
        save();
        notifySubscribers();
    }

    // true se esiste gia' una playlist con questo nome (ignora maiuscole/minuscole e spazi)
    // il controller la chiama PRIMA di creare, per mostrare l'errore giusto all'utente
    public boolean isPlaylistNameTaken(String name) {
        if (name == null) {
            return false;
        }
        String newName = name.trim();
        if(!playlists.containsKey(newName)) {
            return false;
        }
        return true;
    }
    
    //per poter aggiungere nuovi Osservaroti/Subscriber a questo elemento
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }
    public void removeTracks(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
    
     public void load() {
        persistence.load(this);
    }
    
    public void save() {
        persistence.save(this);
    }
    
    @Override
    public void notifySubscribers() {
        for (Subscriber s: subscribers) {
            s.update();
        }
    }
}