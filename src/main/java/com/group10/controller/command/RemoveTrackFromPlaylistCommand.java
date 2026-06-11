/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.controller.command;

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
public class RemoveTrackFromPlaylistCommand implements Command {
    private final TrackComponent track;
    private final PlaylistComponent playlist; //le playlist in cui era

    public RemoveTrackFromPlaylistCommand(TrackComponent track, PlaylistComponent playlist) {
        this.track = track;
        this.playlist = playlist;
    }

    @Override
    public void execute() {
        //prima rimozione solo da tutte le playlist
        playlist.remove(track);
        
        //aggiorna ui
        MusicCatalogue.getInstance().notifySubscribers();
    }

    @Override
    public void undo() {
        //prima rimozione solo da tutte le playlist
        playlist.add(track);
        
        //aggiorna ui
        MusicCatalogue.getInstance().notifySubscribers();
    }
}
