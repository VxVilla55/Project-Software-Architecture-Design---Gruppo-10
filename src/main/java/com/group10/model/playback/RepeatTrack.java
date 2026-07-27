package com.group10.model.playback;

import com.group10.model.state.PlaybackEngine;

/**
 *
 * @author group10
 *
 * Strategy concreta con ripetizione della singola traccia: a fine
 * traccia la riproduce di nuovo dall'inizio
 *
 * PATTERN: ConcreteStrategy di PlaybackMode
 */
public class RepeatTrack implements PlaybackMode {

    // riproduce di nuovo la traccia corrente dall'inizio
    @Override
    public void onTrackEnd(PlaybackEngine engine) {
        engine.replayCurrent();
    }

    @Override
    public PlaybackMode nextMode() {
        return new Sequential();
    }
}
