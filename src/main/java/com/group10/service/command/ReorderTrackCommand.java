
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.service.command;

import com.group10.controller.MainViewController;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.builder.PlaylistBuilder;

/**
 *
 * @author group10
 */
public class ReorderTrackCommand implements Command{
    private final PlaylistComponent playlist;
    private final int fromIndex;
    private final int toIndex;

    public ReorderTrackCommand(PlaylistComponent playlist, int fromIndex,  int toIndex) {
        this.playlist = playlist;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    @Override
    public void execute() {
        //applica lo spostamento nella playlist
        playlist.moveTrack(fromIndex, toIndex);
    }

    @Override
    public void undo() {
        //applica lo spostamento nella playlist
        playlist.moveTrack(toIndex, fromIndex);
    }
}
