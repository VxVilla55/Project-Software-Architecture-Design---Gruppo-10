/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model;

import java.util.Comparator;
import java.util.stream.Collectors;

import com.group10.model.common.Publisher;
import com.group10.model.common.Subscriber;
import com.group10.model.state.PlaybackEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author group10
 * PATTERN: Singleton; e' anche Publisher del pattern Observer, notifica le viste e la
 * persistenza ad ogni modifica del catalogo.
 * Tiene lo stato dell'app: l'elenco delle tracce, delle playlist e dei generi disponibili.
 */
public class MusicCatalogue implements Publisher{
    
    private static MusicCatalogue singleton;
    
    private List<TrackComponent> tracks;
    private final Map<String, PlaylistComponent> playlists;
    private Set<String> genres;
    private List<Subscriber> subscribers;
    
    private MusicCatalogue() {
        tracks = new ArrayList<>();
        playlists = new TreeMap<>();
        genres = new TreeSet<>();
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
        notifySubscribers();
    }
   public void removeTrack(TrackComponent track) {
        if (track == null) return;

        tracks.remove(track);

        //la traccia va tolta anche da ogni playlist che la conteneva
        for (PlaylistComponent playlist : this.playlists.values()) {
            playlist.remove(track);
        }

        //e dalla coda di riproduzione, se presente
        PlaybackEngine.getInstance().removeTrackFromQueue(track);

        notifySubscribers();
    }

    public void addPlaylist (PlaylistComponent playlist) {
        // univocita' del nome, non si aggiunge una playlist con un nome gia' presente
        if (isPlaylistNameTaken(playlist.getName())) {
            throw new IllegalArgumentException(
                "Esiste già una playlist con questo nome: " + playlist.getName());
        }
        playlists.put(playlist.getName(), playlist);
        notifySubscribers();
    }
    
public void removePlaylist(PlaylistComponent p) {
    if (p != null) {
        this.playlists.remove(p.getName()); 
    }
}
    
    public PlaylistComponent getPlaylist (String playlistName) {
        return playlists.get(playlistName);
    }
    
    public void addTrackToPlaylist(String playlistName, TrackComponent track) {
        PlaylistComponent playlist = getPlaylist(playlistName);
        playlist.add(track);
        notifySubscribers();
    }

    public void removeTrackFromPlaylist(String playlistName, TrackComponent track) {
        PlaylistComponent playlist = getPlaylist(playlistName);
        playlist.remove(track);
        notifySubscribers();
    }

    // true se esiste gia' una playlist con questo nome (il controller la chiama prima
    // di crearla, cosi' puo' mostrare l'errore giusto)
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


    /**
     * T12.2 - Restituisce le prime 'n' tracce più ascoltate del catalogo,
     * ordinate in modo decrescente in base al playCount.
     */
    public List<TrackComponent> getTopTracks(int n) {
        return this.tracks.stream()
                .sorted(Comparator.comparingInt(TrackComponent::getPlayCount).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * T12.2 - Restituisce le prime 'n' playlist più ascoltate del catalogo,
     * ordinate in modo decrescente in base al playCount.
     */
    public List<PlaylistComponent> getTopPlaylists(int n) {
        return this.playlists.values().stream()
                .sorted(Comparator.comparingInt(PlaylistComponent::getPlayCount).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }
    
    
    //per poter aggiungere nuovi Osservatori/Subscriber a questo elemento
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }
    public void removeTracks(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
    
    public List<String> getGenres() {
        return genres.stream().toList();
    }
    
    public void addGenre(String genre) {
        genres.add(genre);
    }
    
    public void removeGenres(String genre) {
        genres.remove(genre);
    }


    
    @Override
    public void notifySubscribers() {
        for (Subscriber s: subscribers) {
            s.update();
        }
    }
    
    public void replaceTrack(TrackComponent oldTrack, TrackComponent updatedTrack) {
        if (oldTrack == null || updatedTrack == null) {
            return;
        }        
        tracks.replaceAll(track -> {
            if (track.equals(oldTrack)) {
                return updatedTrack;
            } else {
                return track;
            }
        });
        //aggiorno le playlist se contenevano la traccia vecchia
        for (PlaylistComponent playlist: playlists.values()) {
            if ( playlist.contains(oldTrack))
                playlist.updateTrack(oldTrack, updatedTrack);
        }
        notifySubscribers();
    }
    public void replacePlaylist(PlaylistComponent newPlaylist, PlaylistComponent oldPlaylist) {
        if (newPlaylist == null) {
            return;
        }
        
        removePlaylist(oldPlaylist);
        addPlaylist(newPlaylist);
        
        notifySubscribers();
    }
}