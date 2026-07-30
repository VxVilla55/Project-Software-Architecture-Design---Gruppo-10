package com.group10.model.playback;

import com.group10.model.state.PlaybackEngine;

/**
 *
 * @author group10
 * PATTERN: Strategy (l'interfaccia Strategy).
 * Incapsula il comportamento da adottare quando una traccia finisce (vai avanti,
 * ripeti la playlist, ripeti la traccia). PlaybackEngine e' il Context: chiama
 * onTrackEnd() senza sapere quale modalita' e' attiva. E' il controller (client) a
 * scegliere quale ConcreteStrategy usare e a passarla al Context col setter
 * (setPlaybackMode).
 */
public interface PlaybackMode {

    // azione da eseguire quando la traccia corrente finisce
    void onTrackEnd(PlaybackEngine engine);

    // true se a fine/inizio coda si riparte dall'altro capo (loop della coda)
    default boolean loopsQueue() {
        return false;
    }

    // true se questa modalita' ripete la singola traccia, serve alla UI per l'icona
    default boolean loopsTrack() {
        return false;
    }
}
