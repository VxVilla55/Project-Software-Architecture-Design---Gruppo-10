package com.group10.model.playback;

import com.group10.model.state.PlaybackEngine;

/**
 *
 * @author group10
 * PATTERN: Strategy. ConcreteStrategy, ripete tutta la playlist: a fine coda
 * si riparte dalla prima traccia.
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

    // in loop-playlist la coda fa wrap-around: dall'ultima si torna alla prima e viceversa
    @Override
    public boolean loopsQueue() {
        return true;
    }
}
