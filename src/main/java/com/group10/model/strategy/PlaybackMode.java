package com.group10.model.strategy;

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
}
