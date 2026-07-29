package com.group10.model.playback;

import com.group10.model.state.PlaybackEngine;

/**
 *
 * @author group10
 * PATTERN: Strategy. ConcreteStrategy, ripete solo la traccia corrente,
 * da capo ogni volta che finisce.
 */
public class RepeatTrack implements PlaybackMode {

    // riproduce di nuovo la traccia corrente dall'inizio
    @Override
    public void onTrackEnd(PlaybackEngine engine) {
        engine.replayCurrent();
    }

    @Override
    public boolean loopsTrack() {
        return true;
    }
}
