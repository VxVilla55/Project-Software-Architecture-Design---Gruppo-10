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
        playlists.add(playlist);
    }
    public void removePlaylist (PlaylistComponent playlist) {
        playlists.remove(playlist);
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
