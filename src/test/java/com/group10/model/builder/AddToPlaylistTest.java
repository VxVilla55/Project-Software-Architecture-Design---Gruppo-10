package com.group10.model.builder;

import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.common.Subscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * T6.5 - JUnit: aggiunta singola/multipla di tracce a una PlaylistComponent
 *
 * Copre:
 * - aggiunta singola (una traccia)
 * - aggiunta multipla (più tracce, ordine preservato)
 * - aggiunta della stessa traccia due volte (duplicati ammessi)
 * - la playlist parte vuota
 * - getDurationInSeconds() si aggiorna correttamente dopo le aggiunte
 * - Interazione con MusicCatalogue e notifiche ai Subscriber
 */
public class AddToPlaylistTest {

    private PlaylistComponent playlist;
    private TrackComponent track1;
    private TrackComponent track2;
    private TrackComponent track3;
    
    private MusicCatalogue catalogue;

    @BeforeEach
    public void setUp() {
        playlist = new PlaylistComponent("Test Playlist");
        
        // Prepariamo anche il catalogo pulito per i test di integrazione
        catalogue = MusicCatalogue.getInstance();
        catalogue.getTracks().clear();
        catalogue.getPlaylists().clear();

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

    // ==========================================
    // TEST ORIGINALI SU PlaylistComponent
    // ==========================================

    @Test
    public void playlistDevePartireVuota() {
        assertTrue(playlist.isEmpty());
        assertEquals(0, playlist.getSize());
    }

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
        List<TrackComponent> tracks = new ArrayList<>(playlist.getTracks());
        assertEquals(track1, tracks.get(0));
        assertEquals(track2, tracks.get(1));
        assertEquals(track3, tracks.get(2));
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

  @Test
    public void aggiuntaDuplicato_nonAmmesso() {
        playlist.add(track1);
        playlist.add(track1); // Tento di aggiungere di nuovo la stessa traccia
        
        // La dimensione deve rimanere 1, il duplicato viene ignorato
        assertEquals(1, playlist.getSize(), "I duplicati non devono essere inseriti nella playlist");
    }

    @Test
    public void aggiuntaDuplicato_durataInvariata() {
        playlist.add(track1);
        playlist.add(track1); // Tento di aggiungere di nuovo la stessa traccia
        
        // La durata totale deve rimanere quella della singola traccia, non raddoppiare
        assertEquals(track1.getDurationInSeconds(), playlist.getDurationInSeconds(), 
                "La durata non deve raddoppiare se l'aggiunta del duplicato viene bloccata");
    }
 

    @Test
    public void catalogue_addTrackToPlaylist_aggiungeCorrettamente() {
        catalogue.addPlaylist(playlist);
        
        catalogue.addTrackToPlaylist(playlist.getName(), track1);
        
        PlaylistComponent retrieved = catalogue.getPlaylist(playlist.getName());
        assertTrue(retrieved.getTracks().contains(track1), "La traccia deve essere presente nella playlist salvata nel catalogo");
        assertEquals(1, retrieved.getSize());
    }

    @Test
    public void catalogue_addTrackToPlaylist_notificaISubscriber() {
        // Creiamo un subscriber fittizio (usando una classe anonima) per contare gli update
        final int[] updateCount = {0};
        Subscriber mockSubscriber = new Subscriber() {
            @Override
            public void update() {
                updateCount[0]++;
            }
        };
        
        catalogue.addSubscriber(mockSubscriber);
        catalogue.addPlaylist(playlist); // Questo causerà il 1° update
        
        // L'aggiunta di una traccia alla playlist tramite catalogo deve chiamare notifySubscribers()
        catalogue.addTrackToPlaylist(playlist.getName(), track1); // Questo causerà il 2° update
        
        assertEquals(2, updateCount[0], "Il subscriber doveva essere notificato due volte (1 per addPlaylist, 1 per addTrackToPlaylist)");
        
        // Pulizia finale
        // (Nota: hai un metodo chiamato removeTracks che sembra essere usato per rimuovere i subscriber, lo uso qui)
        catalogue.removeTracks(mockSubscriber); 
    }
}