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
 * PATTERN: Command. ConcreteCommand, rinomina una playlist (annullabile): costruisce
 * subito la versione con il nome nuovo, cosi' execute()/undo() si limitano a scambiare
 * vecchia e nuova playlist nel catalogo.
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
                .setPlayCount(oldPlaylist.getPlayCount()) // il rinomino non azzera i suoi ascolti
                .build();
    }

    @Override
    public void execute() {
        //applica la modifica nel catalogo
        MusicCatalogue.getInstance().replacePlaylist(newPlaylist, oldPlaylist);
        if (MainViewController.getInstance().getSelectedPlaylist() == oldPlaylist) {
            MainViewController.getInstance().setSelectedPlaylist(newPlaylist);
        }
    }

    @Override
    public void undo() {
        //applica la modifica nel catalogo
        MusicCatalogue.getInstance().replacePlaylist(oldPlaylist, newPlaylist);
        if (MainViewController.getInstance().getSelectedPlaylist() == newPlaylist) {
            MainViewController.getInstance().setSelectedPlaylist(oldPlaylist);
        }
    }
}
