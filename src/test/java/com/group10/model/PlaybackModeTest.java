package com.group10.model;

import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import com.group10.model.playback.RepeatPlaylist;
import com.group10.model.playback.RepeatTrack;
import com.group10.model.playback.Sequential;
import com.group10.model.state.PlaybackEngine;
import com.group10.model.state.StoppedState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author group10
 * Test delle ConcreteStrategy che decidono cosa fare a fine traccia (pattern Strategy).
 * Si verifica sia l'effetto di onTrackEnd() sulla coda, sia le due query di dominio
 * (loopsQueue/loopsTrack) che il resto del codice usa al posto di instanceof.
 */
class PlaybackModeTest {

    private PlaybackEngine engine;

    @BeforeEach
    void setUp() {
        engine = PlaybackEngine.getInstance();
        engine.stopSimulation();
        engine.clearQueue();
        engine.setCurrentPlaylist(null);
        if(engine.isShuffled()) {
            engine.toggleShuffle();
        }
    }

    @Test
    void sequential_doesNotLoopQueueNorTrack() {
        Sequential mode = new Sequential();
        assertFalse(mode.loopsQueue());
        assertFalse(mode.loopsTrack());
    }

    @Test
    void repeatPlaylist_loopsQueueButNotTrack() {
        RepeatPlaylist mode = new RepeatPlaylist();
        assertTrue(mode.loopsQueue());
        assertFalse(mode.loopsTrack());
    }

    @Test
    void repeatTrack_loopsTrackButNotQueue() {
        RepeatTrack mode = new RepeatTrack();
        assertTrue(mode.loopsTrack());
        assertFalse(mode.loopsQueue());
    }
}
