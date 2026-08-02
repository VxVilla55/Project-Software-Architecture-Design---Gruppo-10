package com.group10.model;

import com.group10.model.builder.TrackBuilder;
import com.group10.model.builder.PlaylistBuilder;
import com.group10.model.state.PlaybackEngine;

import com.group10.TestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrackRemovalCascadeTest {

    private MusicCatalogue catalogue;
    private PlaybackEngine engine;

    @BeforeEach
    public void setUp() {
        // ambiente pulito, cosi' i test non si accavallano tra loro
        TestSupport.resetSingletons();
        catalogue = MusicCatalogue.getInstance();
        engine = PlaybackEngine.getInstance();
    }

    @Test
    public void removeTrack_removesItFromCatalogueAndPlaylistAndQueue() {
        // --- 1. SETUP ---
        TrackComponent track = new TrackBuilder()
            .setTitle("Track To Delete")
            .setAuthor("Test")
            .setDuration(180)
            .build();
            
        PlaylistComponent playlist = new PlaylistBuilder()
            .setName("Test Playlist")
            .build();

        // Popoliamo il catalogo
        catalogue.addTrack(track);
        catalogue.addPlaylist(playlist);
        
        // Inseriamo la traccia ovunque: nella playlist e nella coda di riproduzione
        catalogue.addTrackToPlaylist(playlist.getName(), track);
        engine.addTrackToQueue(track);

        // Controllo di sicurezza: verifichiamo che il setup sia andato a buon fine
        assertTrue(catalogue.getTracks().contains(track));
        assertTrue(playlist.contains(track));
        assertNotNull(engine.getCurrentTrack(), "L'engine dovrebbe avere una traccia in riproduzione");


        // --- 2. AZIONE (Testiamo la cancellazione) ---
        catalogue.removeTrack(track);


        // --- 3. VERIFICA DEI RISULTATI ---
        
        // A) Verifica rimozione dalla libreria principale
        assertFalse(catalogue.getTracks().contains(track), "ERRORE: La traccia è ancora nel catalogo!");
        
        // B) Verifica rimozione a cascata dalla playlist
        assertFalse(playlist.contains(track), "ERRORE: La traccia è rimasta bloccata nella playlist!");
        
        // C) Verifica rimozione dalla coda 
        // Se eliminiamo l'unica traccia in riproduzione, il lettore si ferma e currentTrack diventa null
        assertNull(engine.getCurrentTrack(), "ERRORE: La traccia è ancora nella coda di riproduzione!");
    }
}