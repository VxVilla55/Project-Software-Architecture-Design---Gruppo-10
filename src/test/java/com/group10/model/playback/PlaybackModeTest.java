package com.group10.model.playback;

import com.group10.TestSupport;
import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
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
        TestSupport.resetSingletons();
        engine = PlaybackEngine.getInstance();
    }

    @AfterEach
    void tearDown() {
        TestSupport.stopPlaybackTimer();
    }

    private TrackComponent makeTrack(String title) {
        return new TrackBuilder()
                .setTitle(title)
                .setAuthor("Author")
                .setDuration(100)
                .build();
    }

    private void queueTwoTracks(TrackComponent first, TrackComponent second) {
        engine.addTrackToQueue(first);
        engine.addTrackToQueue(second);
        engine.setCurrentTrack(first);
        //il player parte fermo, i test verificano solo lo spostamento nella coda
        engine.changeState(new StoppedState());
    }

    // ---------- query di dominio (usate invece di instanceof) ----------

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

    // ---------- Sequential ----------

    @Test
    void sequential_onTrackEnd_withNextTrack_movesToNextTrack() {
        TrackComponent first = makeTrack("First");
        TrackComponent second = makeTrack("Second");
        queueTwoTracks(first, second);

        new Sequential().onTrackEnd(engine);

        assertEquals(second, engine.getCurrentTrack());
    }

    @Test
    void sequential_onTrackEnd_onLastTrack_keepsLastTrackAndStops() {
        //caso limite: fine coda in modalita' normale, la riproduzione si ferma
        TrackComponent first = makeTrack("First");
        TrackComponent last = makeTrack("Last");
        queueTwoTracks(first, last);
        engine.setCurrentTrack(last);
        engine.changeState(new StoppedState());

        new Sequential().onTrackEnd(engine);

        assertEquals(last, engine.getCurrentTrack(), "l'ultima traccia resta quella corrente");
        assertFalse(engine.isPlaying(), "a fine coda il player non deve restare in riproduzione");
    }

    // ---------- RepeatPlaylist ----------

    @Test
    void repeatPlaylist_onTrackEnd_withNextTrack_movesToNextTrack() {
        TrackComponent first = makeTrack("First");
        TrackComponent second = makeTrack("Second");
        queueTwoTracks(first, second);

        new RepeatPlaylist().onTrackEnd(engine);

        assertEquals(second, engine.getCurrentTrack());
    }

    @Test
    void repeatPlaylist_onTrackEnd_onLastTrack_restartsFromFirstTrack() {
        //caso limite: e' la differenza vera rispetto a Sequential
        TrackComponent first = makeTrack("First");
        TrackComponent last = makeTrack("Last");
        queueTwoTracks(first, last);
        engine.setCurrentTrack(last);
        engine.changeState(new StoppedState());

        new RepeatPlaylist().onTrackEnd(engine);

        assertEquals(first, engine.getCurrentTrack(), "a fine coda si torna alla prima traccia");
    }

    // ---------- RepeatTrack ----------

    @Test
    void repeatTrack_onTrackEnd_keepsSameTrackAndResetsTime() {
        TrackComponent first = makeTrack("First");
        TrackComponent second = makeTrack("Second");
        queueTwoTracks(first, second);
        engine.seek(42.0);

        new RepeatTrack().onTrackEnd(engine);

        assertEquals(first, engine.getCurrentTrack(), "la traccia corrente non deve cambiare");
        assertEquals(0.0, engine.getCurrentTime(), "la traccia deve ripartire da zero");
    }
}
