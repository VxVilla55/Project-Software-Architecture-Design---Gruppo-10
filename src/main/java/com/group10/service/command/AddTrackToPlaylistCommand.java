/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.service.command;

import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;

/**
 *
 * @author group10
 */
public class AddTrackToPlaylistCommand implements Command {
    private final TrackComponent track;
    private final String playlistName;

    public AddTrackToPlaylistCommand(TrackComponent track, String playlistName) {
        this.track = track;
        this.playlistName = playlistName;
    }

    @Override
    public void execute() {
        //aggiunta al catalogo
        MusicCatalogue.getInstance().addTrackToPlaylist(playlistName, track);        
        //aggiorna ui
        MusicCatalogue.getInstance().notifySubscribers();
    }

    @Override
    public void undo() {
        //rimozione al catalogo
        MusicCatalogue.getInstance().removeTrackFromPlaylist(playlistName, track);
        //aggiorna ui
        MusicCatalogue.getInstance().notifySubscribers();
    }
}
