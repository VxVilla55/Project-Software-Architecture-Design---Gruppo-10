package com.group10.model.builder;

import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * T6.5 - JUnit: aggiunta singola/multipla di tracce a una PlaylistComponent
 *
 * Copre:
 *  - aggiunta singola (una traccia)
 *  - aggiunta multipla (più tracce, ordine preservato)
 *  - aggiunta della stessa traccia due volte (duplicati ammessi)
 *  - la playlist parte vuota
 *  - getDurationInSeconds() si aggiorna correttamente dopo le aggiunte
 */
public class AddToPlaylistTest {

    private PlaylistComponent playlist;
    private TrackComponent track1;
    private TrackComponent track2;
    private TrackComponent track3;

    @BeforeEach
    public void setUp() {
        playlist = new PlaylistComponent("Test Playlist");

        track1 = new TrackBuilder()
                .setTitle("Bohemian Rhapsody")
                .setAuthor("Queen")
                .setDuration(354)
                .setGenre("Rock")
                .setYear(1975)
                .build();

        track2 = new TrackBuilder()
                .setTitle("Hotel California")
                .setAuthor("Eagles")
                .setDuration(391)
                .setGenre("Rock")
                .setYear(1977)
                .build();

        track3 = new TrackBuilder()
                .setTitle("Stairway to Heaven")
                .setAuthor("Led Zeppelin")
                .setDuration(482)
                .setGenre("Rock")
                .setYear(1971)
                .build();
    }

    // --- Stato iniziale ---

    @Test
    public void playlistDevePartireVuota() {
        assertTrue(playlist.isEmpty());
        assertEquals(0, playlist.getSize());
    }

    // --- Aggiunta singola ---

    @Test
    public void aggiuntaSingola_dimensioneCorretta() {
        playlist.add(track1);
        assertEquals(1, playlist.getSize());
    }

    @Test
    public void aggiuntaSingola_trackPresente() {
        playlist.add(track1);
        assertTrue(playlist.getTracks().contains(track1));
    }

    @Test
    public void aggiuntaSingola_playlistNonVuota() {
        playlist.add(track1);
        assertFalse(playlist.isEmpty());
    }

    @Test
    public void aggiuntaSingola_durataAggiornata() {
        playlist.add(track1);
        assertEquals(track1.getDurationInSeconds(), playlist.getDurationInSeconds());
    }

    // --- Aggiunta multipla ---

    @Test
    public void aggiunzioneMultipla_dimensioneCorretta() {
        playlist.add(track1);
        playlist.add(track2);
        playlist.add(track3);
        assertEquals(3, playlist.getSize());
    }

    @Test
    public void aggiunzioneMultipla_tutteLeTraccePresenti() {
        playlist.add(track1);
        playlist.add(track2);
        playlist.add(track3);

        assertTrue(playlist.getTracks().contains(track1));
        assertTrue(playlist.getTracks().contains(track2));
        assertTrue(playlist.getTracks().contains(track3));
    }

    @Test
    public void aggiunzioneMultipla_ordinePreservato() {
        playlist.add(track1);
        playlist.add(track2);
        playlist.add(track3);

        assertEquals(track1, playlist.getTracks().get(0));
        assertEquals(track2, playlist.getTracks().get(1));
        assertEquals(track3, playlist.getTracks().get(2));
    }

    @Test
    public void aggiunzioneMultipla_durataCorrettaSomma() {
        playlist.add(track1);
        playlist.add(track2);
        playlist.add(track3);

        int expected = track1.getDurationInSeconds()
                     + track2.getDurationInSeconds()
                     + track3.getDurationInSeconds();
        assertEquals(expected, playlist.getDurationInSeconds());
    }

    // --- Duplicati ---

    @Test
    public void aggiuntaDuplicato_ammesso() {
        playlist.add(track1);
        playlist.add(track1);
        assertEquals(2, playlist.getSize());
    }

    @Test
    public void aggiuntaDuplicato_durataRaddoppiata() {
        playlist.add(track1);
        playlist.add(track1);
        assertEquals(track1.getDurationInSeconds() * 2, playlist.getDurationInSeconds());
    }
}