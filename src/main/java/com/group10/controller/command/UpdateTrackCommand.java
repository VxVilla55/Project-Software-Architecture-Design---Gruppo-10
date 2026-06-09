/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.controller.command;

import com.group10.controller.MainViewController;
import com.group10.model.MusicCatalogue;
import com.group10.model.TrackComponent;

/**
 *
 * @author group10
 */
public class UpdateTrackCommand implements Command {
    private final TrackComponent oldTrack;
    private final TrackComponent newTrack;

    public UpdateTrackCommand(TrackComponent oldTrack, TrackComponent newTrack) {
        this.oldTrack = oldTrack;
        this.newTrack = newTrack;
    }

    @Override
    public void execute() {
        //applica la modifica nel catalogo
        MainViewController.getInstance().setSelectedTrack(newTrack);
        MusicCatalogue.getInstance().replaceTrack(oldTrack, newTrack);
    }

    @Override
    public void undo() {
        //applica la modifica nel catalogo
        MusicCatalogue.getInstance().replaceTrack(newTrack, oldTrack);
        MainViewController.getInstance().setSelectedTrack(oldTrack);
    }
}
