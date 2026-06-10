package com.group10.model.strategy;

import com.group10.model.state.PlaybackEngine;

/**
 *
 * @author group10
 *
 * Strategy del pattern omonimo: definisce il comportamento da adottare
 * al termine di una traccia (avanzare, ripetere la coda o la traccia)
 *
 * PATTERN: questa è la Strategy; PlaybackEngine è il Context che la usa
 */
public interface PlaybackMode {

    // azione da eseguire quando la traccia corrente finisce
    void onTrackEnd(PlaybackEngine engine);
}
