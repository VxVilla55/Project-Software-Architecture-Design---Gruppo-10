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
 * PATTERN: Command. ConcreteCommand, elimina una playlist dal catalogo (annullabile).
 */
public class DeletePlaylistCommand implements Command {
    private final PlaylistComponent playlistDeleted;

    public DeletePlaylistCommand(PlaylistComponent playlistDeleted) {
        this.playlistDeleted = playlistDeleted;
    }

    @Override
    public void execute() {
        //rimozione della playlist dal catalogo
        MusicCatalogue.getInstance().removePlaylist(playlistDeleted);
    }

    @Override
    public void undo() {
        //aggiunta della playlist dal catalogo
        MusicCatalogue.getInstance().addPlaylist(playlistDeleted);
    }
}
