package com.group10.model.strategy;

import com.group10.model.state.PlaybackEngine;

/**
 *
 * @author group10
 *
 * Strategy concreta con ripetizione dell'intera playlist: a fine coda
 * riparte dalla prima traccia
 *
 * PATTERN: ConcreteStrategy di RepeatMode
 */
public class RepeatPlaylist implements PlaybackMode {

    // avanza alla successiva, o riparte dalla prima se la coda è finita
    @Override
    public void onTrackEnd(PlaybackEngine engine) {
        if (engine.hasNext()) {
            engine.next();
        } else {
            engine.playFromStart();
        }
    }
}
