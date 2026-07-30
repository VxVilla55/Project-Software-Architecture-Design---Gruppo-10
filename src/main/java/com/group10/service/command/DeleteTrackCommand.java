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
 * PATTERN: Command. ConcreteCommand, elimina una traccia dal catalogo e la ripristina
 * con l'undo, memorizzando le playlist che la contenevano e la sua posizione in coda.
 */
public class DeleteTrackCommand implements Command {
    private final TrackComponent trackDeleted;
    private final List<PlaylistComponent> playlists; //le playlist in cui era
    private Integer indexInQueue;

    public DeleteTrackCommand(TrackComponent trackDeleted) {
        this.trackDeleted = trackDeleted;
        this.playlists = new ArrayList<>();
        indexInQueue = null;
    }

    @Override
    public void execute() {
        // pulisco lo stato salvato, altrimenti si accumula se il comando viene rieseguito
        playlists.clear();

        // mi segno le playlist che la contenevano (senza toglierla): la rimozione vera
        // la fa gia' removeTrack() piu' sotto, qui servono solo per l'undo
        for (PlaylistComponent playlist : MusicCatalogue.getInstance().getPlaylists().values()) {
            if (playlist.contains(trackDeleted)) {
                playlists.add(playlist);
            }
        }

        // salvo anche la posizione in coda cosi' la posso rimettere li' con l'undo
        indexInQueue = PlaybackEngine.getInstance().removeTrackFromQueue(trackDeleted);

        MainViewController.getInstance().setSelectedTrack(null);
        MusicCatalogue.getInstance().removeTrack(trackDeleted);
    }

    @Override
    public void undo() {
        MusicCatalogue.getInstance().addTrack(trackDeleted);
        MainViewController.getInstance().setSelectedTrack(trackDeleted);

        //rimette la traccia in coda alla posizione di prima, se ci stava
        if (indexInQueue != null) {
            PlaybackEngine.getInstance().addTrackToQueueAtIndex(trackDeleted, indexInQueue);
            PlaybackEngine.getInstance().notifySubscribers();
        }

        for (PlaylistComponent playlist : playlists) {
            playlist.add(trackDeleted);
        }
    }
}
