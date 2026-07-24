package com.group10.model;

import com.group10.model.builder.TrackBuilder;
import com.group10.model.state.PlaybackEngine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author group10
 *
 * Verifica i quattro scenari dello skip playlist:
 * 1) tracce sciolte, nessuna playlist -> skip ferma e svuota;
 * 2) playlist in attesa -> skip la riproduce, poi avanza nel catalogo;
 * 3) piu playlist in attesa -> consumate in ordine di inserimento;
 * 4) contesto playlist + una in attesa -> parte l'attesa, poi il ciclo prosegue.
 */
public class SkipPlaylistTest {

    private PlaybackEngine engine;
    private PlaylistComponent p1;
    private PlaylistComponent p2;
    private PlaylistComponent p3;

    // reset del Singleton via reflection, per isolare ogni test
    @BeforeEach
    void setUp() throws Exception {
        Field f = PlaybackEngine.class.getDeclaredField("instance");
        f.setAccessible(true);
        f.set(null, null);

        engine = PlaybackEngine.getInstance();
        p1 = makePlaylist("P1");
        p2 = makePlaylist("P2");
        p3 = makePlaylist("P3");
    }

    @AfterEach
    void tearDown() {
        engine.stopSimulation(); // ferma il timer di riproduzione lasciato attivo dai play()
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

    // Scenario 1: tracce sciolte dalla home, nessuna playlist -> skip ferma e svuota
    @Test
    void skip_daTracceSciolte_fermaESvuota() {
        engine.addTrackToQueue(makeTrack("loose1"));
        engine.addTrackToQueue(makeTrack("loose2"));
        engine.play();
        engine.setCurrentPlaylist(null);
        assertFalse(engine.getQueue().isEmpty());

        engine.skipToNextPlaylist(List.of(p1, p2, p3));

        assertTrue(engine.getQueue().isEmpty(), "la coda deve svuotarsi");
        assertNull(engine.getCurrentTrack(), "non deve esserci piu una traccia corrente");
    }

    // Scenario 2: una playlist in attesa -> parte quella, poi si avanza nel catalogo
    @Test
    void skip_unaPlaylistInAttesa_partePoiAvanza() {
        List<PlaylistComponent> catalogue = List.of(p1, p2, p3);
        engine.addTrackToQueue(makeTrack("loose"));
        engine.play();

        engine.addPendingPlaylist(p1);

        engine.skipToNextPlaylist(catalogue);
        assertEquals(p1, engine.getCurrentPlaylist(), "parte la playlist accodata");

        engine.skipToNextPlaylist(catalogue);
        assertEquals(p2, engine.getCurrentPlaylist(), "poi si avanza alla successiva");

        engine.skipToNextPlaylist(catalogue);
        assertEquals(p3, engine.getCurrentPlaylist());
    }

    // Scenario 3: piu playlist in attesa -> consumate nell'ordine di inserimento
    @Test
    void skip_piuPlaylistInAttesa_ordineDiInserimento() {
        List<PlaylistComponent> catalogue = List.of(p1, p2, p3);
        engine.addTrackToQueue(makeTrack("loose"));
        engine.play();

        engine.addPendingPlaylist(p3);
        engine.addPendingPlaylist(p2);

        engine.skipToNextPlaylist(catalogue);
        assertEquals(p3, engine.getCurrentPlaylist(), "prima la playlist accodata per prima (P3)");

        engine.skipToNextPlaylist(catalogue);
        assertEquals(p2, engine.getCurrentPlaylist(), "poi la seconda accodata (P2)");
    }

    // Scenario 4: sto su P2, accodo P1 -> skip fa partire P1, poi il ciclo prosegue verso P2
    @Test
    void skip_conContestoPlaylist_dopoAttesaProsegueIlCiclo() {
        List<PlaylistComponent> catalogue = List.of(p1, p2, p3);
        p2.playOnEngine(engine);
        engine.play();
        assertEquals(p2, engine.getCurrentPlaylist());

        engine.addPendingPlaylist(p1);

        engine.skipToNextPlaylist(catalogue);
        assertEquals(p1, engine.getCurrentPlaylist(), "prima parte la playlist in attesa");

        engine.skipToNextPlaylist(catalogue);
        assertEquals(p2, engine.getCurrentPlaylist(), "poi il ciclo avanza (P1 -> P2)");
    }
}
