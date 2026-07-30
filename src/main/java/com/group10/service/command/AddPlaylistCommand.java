/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.service.command;

import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;

/**
 *
 * @author group10
 * PATTERN: Command. ConcreteCommand, aggiunge una playlist al catalogo (annullabile).
 */
public class AddPlaylistCommand implements Command {
    private final PlaylistComponent playlist;
    
    public AddPlaylistCommand(PlaylistComponent playlist) {
        this.playlist = playlist;
    }

    @Override
    public void execute() {
        //aggiunge la playlist al catalogo globale
        MusicCatalogue.getInstance().addPlaylist(playlist);
    }

    @Override
    public void undo() {
        // rimuove la playlist se l'utente annulla l'azione
        MusicCatalogue.getInstance().removePlaylist(playlist);
    }
}
