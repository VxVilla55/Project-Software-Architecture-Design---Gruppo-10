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
 * PATTERN: Command (ConcreteCommand). Elimina una traccia dal catalogo e la ripristina
 * con l'undo, memorizzando le playlist che la contenevano e la sua posizione in coda.
 *
 * @author group10
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
        //ripulisce lo stato salvato per non accumulare tra esecuzioni successive
        playlists.clear();

        //memorizza (senza rimuovere) le playlist che contenevano la traccia, per l'undo.
        //la rimozione effettiva la fa MusicCatalogue.removeTrack(), che toglie la traccia
        //da catalogo, playlist e coda in un colpo solo: qui non va duplicata
        for (PlaylistComponent playlist : MusicCatalogue.getInstance().getPlaylists().values()) {
            if (playlist.contains(trackDeleted)) {
                playlists.add(playlist);
            }
        }

        //rimuove dalla coda memorizzando la posizione (per ripristinarla in undo)
        indexInQueue = PlaybackEngine.getInstance().removeTrackFromQueue(trackDeleted);

        //elimina il focus alla traccia
        MainViewController.getInstance().setSelectedTrack(null);

        //rimozione effettiva dal catalogo (che rimuove anche dalle playlist rimaste)
        MusicCatalogue.getInstance().removeTrack(trackDeleted);
    }

    @Override
    public void undo() {
        //riaggiunge la traccia al catalogo
        MusicCatalogue.getInstance().addTrack(trackDeleted);

        //rimette il focus alla traccia
        MainViewController.getInstance().setSelectedTrack(trackDeleted);

        //ripristina la traccia nella coda alla posizione che aveva, se era in coda
        if (indexInQueue != null) {
            PlaybackEngine.getInstance().addTrackToQueueAtIndex(trackDeleted, indexInQueue);
            PlaybackEngine.getInstance().notifySubscribers();
        }

        //riaggiunge la traccia alle playlist da cui era stata tolta
        for (PlaylistComponent playlist : playlists) {
            playlist.add(trackDeleted);
        }
    }
}
