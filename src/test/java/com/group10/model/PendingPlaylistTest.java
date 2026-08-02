package com.group10.model;

import com.group10.model.builder.TrackBuilder;
import com.group10.model.state.PlaybackEngine;

import com.group10.TestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author group10
 *
 * Verifica il meccanismo delle playlist "in attesa" usato dallo skip playlist:
 * accodamento, consumo in ordine di inserimento (FIFO) e avvio di una playlist.
 * La decisione dei tre casi dello skip vive nel controller; qui si coprono i mattoni
 * su cui si appoggia (equivale al cuore degli scenari 2 e 3).
 */
public class PendingPlaylistTest {

    private PlaybackEngine engine;
    private PlaylistComponent p1;
    private PlaylistComponent p2;
    private PlaylistComponent p3;

    // reset del Singleton via reflection, per isolare ogni test
    @BeforeEach
    void setUp() {
        TestSupport.resetSingletons();
        engine = PlaybackEngine.getInstance();
        p1 = makePlaylist("P1");
        p2 = makePlaylist("P2");
        p3 = makePlaylist("P3");
    }

    @AfterEach
    void tearDown() {
        engine.stopSimulation(); // ferma il timer lasciato attivo da startPlaylist -> play
    }

    // durata lunghissima cosi il timer non fa scattare la fine traccia durante il test
    private TrackComponent makeTrack(String title) {
        return new TrackBuilder().setTitle(title).setAuthor("A").setDuration(9999).build();
    }

    private PlaylistComponent makePlaylist(String name) {
        PlaylistComponent p = new PlaylistComponent(name);
        p.add(makeTrack(name + "-t1"));
        return p;
    }

    @Test
    void pending_consumateInOrdineDiInserimento() {
        engine.addPendingPlaylist(p3);
        engine.addPendingPlaylist(p2);

        assertEquals(p3, engine.getPendingPlaylist(), "prima esce la playlist accodata per prima");
        assertEquals(p2, engine.getPendingPlaylist(), "poi la seconda accodata");
        assertNull(engine.getPendingPlaylist(), "esaurite le attese, poll restituisce null");
    }

    @Test
    void getPendingPlaylist_whenNonePending_returnsNull() {
        assertNull(engine.getPendingPlaylist());
    }

    @Test
    void startPlaylist_impostaCorrenteECaricaCoda() {
        engine.startPlaylist(p1);

        assertEquals(p1, engine.getCurrentPlaylist(), "la playlist avviata diventa quella corrente");
        assertEquals("P1-t1", engine.getCurrentTrack().getTitle(), "la coda parte dalla prima traccia");
        assertEquals(1, engine.getQueue().size(), "la coda contiene le tracce della playlist");
    }
}
