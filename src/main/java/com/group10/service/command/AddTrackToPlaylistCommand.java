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
    private final PlaylistComponent playlist;

    public AddTrackToPlaylistCommand(TrackComponent track, PlaylistComponent playlist) {
        this.track = track;
        this.playlist = playlist;
    }

    @Override
    public void execute() {
        //aggiunta al catalogo
        MusicCatalogue.getInstance().addTrackToPlaylist(playlist.getName(), track);        
        //aggiorna ui
        MusicCatalogue.getInstance().notifySubscribers();
    }

    @Override
    public void undo() {
        //rimozione al catalogo
        MusicCatalogue.getInstance().removeTrackFromPlaylist(playlist.getName(), track);
        //aggiorna ui
        MusicCatalogue.getInstance().notifySubscribers();
    }
}
