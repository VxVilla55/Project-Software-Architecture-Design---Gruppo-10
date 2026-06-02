package com.group10.model.common;

import com.group10.model.state.PlaybackEngine;

/**
 * Component del pattern Composite.
 * Astrae tutto ciò che è "riproducibile": sia una singola TrackComponent
 * sia una PlaylistComponent (Composite). 
 */
public interface Playable {

    // durata totale in secondi
    int getDurationInSeconds();

    // Invia l'elemento (traccia o intera playlist) al motore di riproduzione
    void playOnEngine(PlaybackEngine engine);

}