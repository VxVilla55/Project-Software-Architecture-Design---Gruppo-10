/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.service.command;

import com.group10.model.MusicCatalogue;
import com.group10.model.TrackComponent;

/**
 *
 * @author group10
 */
public class AddTrackCommand implements Command {
    private final TrackComponent track;

    public AddTrackCommand(TrackComponent track) {
        this.track = track;
    }

    @Override
    public void execute() {
        //aggiunta al catalogo
        MusicCatalogue.getInstance().addTrack(track);        
        //aggiorna ui
        MusicCatalogue.getInstance().notifySubscribers();
    }

    @Override
    public void undo() {
        //rimozione al catalogo
        MusicCatalogue.getInstance().removeTrack(track);
        //aggiorna ui
        MusicCatalogue.getInstance().notifySubscribers();
    }
}
