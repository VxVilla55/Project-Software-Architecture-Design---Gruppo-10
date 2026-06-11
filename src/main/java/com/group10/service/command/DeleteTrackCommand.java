/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.service.command;

import com.group10.controller.MainViewController;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.state.PlaybackEngine;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author group10
 */
public class DeleteTrackCommand implements Command {
    private final TrackComponent trackDeleted;
    private final List<PlaylistComponent> playlists; //le playlist in cui era
    private Integer indexInQueue;

    public DeleteTrackCommand(TrackComponent trackDeleted) {
        this.trackDeleted = trackDeleted;
        this.playlists = new ArrayList<>();
        indexInQueue = null;
    }

    @Override
    public void execute() {
        //prima rimozione solo da tutte le playlist
        for (PlaylistComponent playlist : MusicCatalogue.getInstance().getPlaylists().values()) {
            if (playlist.contains(trackDeleted)) {
                playlist.remove(trackDeleted);                
                //ma me le salvo
                playlists.add(playlist);
            }
        }
        
        //rimozione dalla coda di riproduzione di playbackengine
        indexInQueue = PlaybackEngine.getInstance().removeTrackFromQueue(trackDeleted);
        
        //elimina il focus alla traccia
        MainViewController.getInstance().setSelectedTrack(null);
        
        //rimozione dal catalogo
        MusicCatalogue.getInstance().removeTrack(trackDeleted);
        
        //aggiorna ui
        MusicCatalogue.getInstance().notifySubscribers();
    }

    @Override
    public void undo() {
        //riaggiunge la traccia al catalogo
        MusicCatalogue.getInstance().addTrack(trackDeleted);
        
        //rimette il focus alla traccia
        MainViewController.getInstance().setSelectedTrack(trackDeleted);
        
        //rimette nella coda di riproduzione di playbackengine
        //PlaybackEngine.getInstance().addTrackToQueueAtIndex(trackDeleted, indexInQueue);
        
        //riaggiunge la traccia alle playlist
        for (PlaylistComponent playlist : playlists) {
            playlist.add(trackDeleted);
        }
        
        MusicCatalogue.getInstance().notifySubscribers();
    }
}
