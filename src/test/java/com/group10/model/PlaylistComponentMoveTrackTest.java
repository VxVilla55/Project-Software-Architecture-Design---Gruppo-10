package com.group10.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class PlaylistComponentMoveTrackTest {

    private PlaylistComponent playlist;
    private TrackComponent t0, t1, t2, t3, t4;

    @BeforeEach
    void setUp() throws Exception {
        playlist = new PlaylistComponent("Test Playlist");

        t0 = new TrackComponent();
        t1 = new TrackComponent();
        t2 = new TrackComponent();
        t3 = new TrackComponent();
        t4 = new TrackComponent();

        setStaticTracks(playlist, new ArrayList<>(List.of(t0, t1, t2, t3, t4)));
    }

    /** Inserisce la lista di tracce direttamente nel campo privato staticTracks. */
    private static void setStaticTracks(PlaylistComponent pl, List<TrackComponent> tracks) throws Exception {
        Field f = PlaylistComponent.class.getDeclaredField("staticTracks");
        f.setAccessible(true);
        f.set(pl, tracks);
    }

    private void assertOrder(List<TrackComponent> result, TrackComponent... expected) {
        assertEquals(expected.length, result.size(), "La dimensione della playlist è cambiata");
        for (int i = 0; i < expected.length; i++) {
            assertSame(expected[i], result.get(i), "Traccia errata in posizione " + i);
        }
    }

    @Test
    @DisplayName("Spostamento in avanti: l'ordine viene aggiornato correttamente")
    void moveTrack_forward_reordersCorrectly() {
        playlist.moveTrack(1, 3); // t1 da indice 1 a indice 3
        assertOrder(playlist.getTracks(), t0, t2, t3, t1, t4);
    }

    @Test
    @DisplayName("Spostamento all'indietro: l'ordine viene aggiornato correttamente")
    void moveTrack_backward_reordersCorrectly() {
        playlist.moveTrack(3, 0); // t3 da indice 3 a indice 0
        assertOrder(playlist.getTracks(), t3, t0, t1, t2, t4);
    }

    @Test
    @DisplayName("Spostamento alla stessa posizione: ordine inalterato")
    void moveTrack_samePosition_noChange() {
        playlist.moveTrack(2, 2);
        assertOrder(playlist.getTracks(), t0, t1, t2, t3, t4);
    }

    @Test
    @DisplayName("Spostamento in testa (head)")
    void moveTrack_toHead() {
        playlist.moveTrack(4, 0); // t4 in prima posizione
        assertOrder(playlist.getTracks(), t4, t0, t1, t2, t3);
    }

    @Test
    @DisplayName("Spostamento in coda (tail)")
    void moveTrack_toTail() {
        playlist.moveTrack(0, 4); // t0 in ultima posizione
        assertOrder(playlist.getTracks(), t1, t2, t3, t4, t0);
    }

    @Test
    @DisplayName("fromIndex negativo: nessuna eccezione, nessuna modifica")
    void moveTrack_fromIndexNegative_noChange() {
        assertDoesNotThrow(() -> playlist.moveTrack(-1, 2));
        assertOrder(playlist.getTracks(), t0, t1, t2, t3, t4);
    }

    @Test
    @DisplayName("toIndex negativo: nessuna eccezione, nessuna modifica")
    void moveTrack_toIndexNegative_noChange() {
        assertDoesNotThrow(() -> playlist.moveTrack(2, -1));
        assertOrder(playlist.getTracks(), t0, t1, t2, t3, t4);
    }

    @Test
    @DisplayName("fromIndex fuori range (>= size): nessuna eccezione, nessuna modifica")
    void moveTrack_fromIndexOutOfRange_noChange() {
        assertDoesNotThrow(() -> playlist.moveTrack(10, 0));
        assertOrder(playlist.getTracks(), t0, t1, t2, t3, t4);
    }

    @Test
    @DisplayName("toIndex fuori range (>= size): nessuna eccezione, nessuna modifica")
    void moveTrack_toIndexOutOfRange_noChange() {
        assertDoesNotThrow(() -> playlist.moveTrack(0, 5));
        assertOrder(playlist.getTracks(), t0, t1, t2, t3, t4);
    }

    @Test
    @DisplayName("Playlist con una sola traccia: moveTrack(0,0) non altera nulla")
    void moveTrack_singleTrackPlaylist_noChange() throws Exception {
        PlaylistComponent single = new PlaylistComponent("Singola");
        TrackComponent onlyTrack = new TrackComponent();
        setStaticTracks(single, new ArrayList<>(List.of(onlyTrack)));

        single.moveTrack(0, 0);

        assertEquals(1, single.getSize());
        assertSame(onlyTrack, single.getTracks().get(0));
    }

    @Test
    @DisplayName("Playlist vuota: moveTrack non lancia eccezioni")
    void moveTrack_emptyPlaylist_noException() throws Exception {
        PlaylistComponent empty = new PlaylistComponent("Vuota");
        setStaticTracks(empty, new ArrayList<>());

        assertDoesNotThrow(() -> empty.moveTrack(0, 0));
        assertTrue(empty.isEmpty());
    }

    @Test
    @DisplayName("Più riordini consecutivi (simula più drag&drop in sequenza)")
    void moveTrack_multipleSequentialMoves_reordersCorrectly() {
        playlist.moveTrack(0, 4); // -> t1,t2,t3,t4,t0
        playlist.moveTrack(0, 2); // -> t2,t3,t1,t4,t0
        assertOrder(playlist.getTracks(), t2, t3, t1, t4, t0);
    }

    @Test
    @DisplayName("Il riordino non altera la durata totale della playlist")
    void moveTrack_doesNotAffectTotalDuration() {
        int durationBefore = playlist.getDurationInSeconds();
        playlist.moveTrack(1, 3);
        assertEquals(durationBefore, playlist.getDurationInSeconds(),
                "La durata totale deve dipendere solo dal contenuto, non dall'ordine");
    }
}