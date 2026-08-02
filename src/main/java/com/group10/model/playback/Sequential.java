package com.group10.model.playback;

import com.group10.model.state.PlaybackEngine;

/**
 *
 * @author group10
 * PATTERN: Strategy. ConcreteStrategy, modalita' normale: a fine traccia passa
 * alla prossima, o si ferma se la coda e' finita.
 */
public class Sequential implements PlaybackMode {

    // avanza alla successiva, o ferma se è l'ultima della coda
    @Override
    public void onTrackEnd(PlaybackEngine engine) {
        if (engine.hasNext()) {
            engine.next();
        } else {
            engine.stop();
        }
    }

    // modalita' sequenziale: nessun loop di coda
    @Override
    public boolean loopsQueue() {
        return false;
    }

    // modalita' sequenziale: nessun loop di traccia
    @Override
    public boolean loopsTrack() {
        return false;
    }

}
