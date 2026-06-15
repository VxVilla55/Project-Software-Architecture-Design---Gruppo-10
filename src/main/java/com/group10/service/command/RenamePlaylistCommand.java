/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.service.command;

import com.group10.controller.MainViewController;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.builder.PlaylistBuilder;
import com.group10.model.state.PlaybackEngine;
import java.util.TreeSet;

/**
 *
 * @author group10
 */
public class RenamePlaylistCommand implements Command {
    private final String newPlaylistName;
    private final PlaylistComponent oldPlaylist;
    private final PlaylistComponent newPlaylist;
    

    public RenamePlaylistCommand(PlaylistComponent playlist, String newPlaylistName) {
        this.oldPlaylist = playlist;
        this.newPlaylistName = newPlaylistName;
        this.newPlaylist = new PlaylistBuilder()
                .setName(newPlaylistName)
                .addTracks(oldPlaylist.getTracks())
                .build();
    }

    @Override
    public void execute() {
        //applica la modifica nel catalogo
        MusicCatalogue.getInstance().replacePlaylist(newPlaylist, oldPlaylist);
        MainViewController.getInstance().setSelectedPlaylist(newPlaylist);
        
        MusicCatalogue.getInstance().notifySubscribers();
    }

    @Override
    public void undo() {
        //applica la modifica nel catalogo
        MusicCatalogue.getInstance().replacePlaylist(oldPlaylist, newPlaylist);
        MainViewController.getInstance().setSelectedPlaylist(oldPlaylist);
        
        MusicCatalogue.getInstance().notifySubscribers();
    }
}
