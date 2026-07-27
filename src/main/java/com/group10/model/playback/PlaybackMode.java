package com.group10.model.playback;

import com.group10.model.state.PlaybackEngine;

/**
 *
 * @author group10
 *
 * Strategy: definisce il comportamento da adottare
 * al termine di una traccia (avanza, ripete coda o traccia)
 *
 * PATTERN: questa è la Strategy, PlaybackEngine è il Context che la usa
 */
public interface PlaybackMode {

    // azione da eseguire quando la traccia corrente finisce
    void onTrackEnd(PlaybackEngine engine);

    // true se a fine/inizio coda la riproduzione riparte dall'altro capo (loop della coda).
    // Evita che il Context debba controllare il tipo concreto della strategia.
    default boolean loopsQueue() {
        return false;
    }

    // true se questa modalità ripete la singola traccia (usato dalla UI per scegliere l'icona)
    default boolean loopsTrack() {
        return false;
    }
}
