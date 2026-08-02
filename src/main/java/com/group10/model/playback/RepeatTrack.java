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

    // il loop qui riguarda solo la traccia, non l'intera coda
    @Override
    public boolean loopsQueue() {
        return false;
    }

    @Override
    public boolean loopsTrack() {
        return true;
    }
}
