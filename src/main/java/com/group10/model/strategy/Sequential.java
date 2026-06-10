package com.group10.model.strategy;

import com.group10.model.state.PlaybackEngine;

/**
 *
 * @author group10
 *
 * Strategy concreta sequenziale: a fine traccia passa alla
 * successiva, oppure ferma la riproduzione se la coda è terminata
 *
 * PATTERN: ConcreteStrategy di RepeatMode
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
}
