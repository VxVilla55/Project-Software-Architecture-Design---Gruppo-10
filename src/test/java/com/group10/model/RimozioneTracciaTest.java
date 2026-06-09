package com.group10.model;

import com.group10.model.builder.TrackBuilder;
import com.group10.model.builder.PlaylistBuilder;
import com.group10.model.state.PlaybackEngine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RimozioneTracciaTest {

    private MusicCatalogue catalogue;
    private PlaybackEngine engine;

    @BeforeEach
    public void setUp() {
        catalogue = MusicCatalogue.getInstance();
        engine = PlaybackEngine.getInstance();
        
        // Puliamo l'ambiente per assicurarci che i test non si accavallino
        catalogue.getTracks().clear();
        catalogue.getPlaylists().clear();
        engine.clearQueue(); 
    }

    @Test
    public void testRimozioneTracciaCascade() {
        // --- 1. SETUP ---
        // ATTENZIONE: Sostituisci ".setDuration(180)" con il metodo esatto
        // che usi nel tuo TrackBuilder per impostare la durata (es. .setLength(180) o altro)
        TrackComponent track = new TrackBuilder()
            .setTitle("Canzone Da Eliminare")
            .setAuthor("Test")
            .setDuration(180) // <-- Modifica solo questa parolina se ti dà errore rosso
            .build();
            
        PlaylistComponent playlist = new PlaylistBuilder()
            .setName("Playlist Di Prova")
            .build();

        // Popoliamo il catalogo
        catalogue.addTrack(track);
        catalogue.addPlaylist(playlist);
        
        // Inseriamo la traccia ovunque: nella playlist e nella coda di riproduzione
        catalogue.addTrackToPlaylist(playlist.getName(), track);
        engine.addTrackToQueue(track);

        // Controllo di sicurezza: verifichiamo che il setup sia andato a buon fine
        assertTrue(catalogue.getTracks().contains(track));
        assertTrue(playlist.getTracks().contains(track));
        assertNotNull(engine.getCurrentTrack(), "L'engine dovrebbe avere una traccia in riproduzione");


        // --- 2. AZIONE (Testiamo la cancellazione) ---
        catalogue.removeTrack(track);


        // --- 3. VERIFICA DEI RISULTATI ---
        
        // A) Verifica rimozione dalla libreria principale
        assertFalse(catalogue.getTracks().contains(track), "ERRORE: La traccia è ancora nel catalogo!");
        
        // B) Verifica rimozione a cascata dalla playlist
        assertFalse(playlist.getTracks().contains(track), "ERRORE: La traccia è rimasta bloccata nella playlist!");
        
        // C) Verifica rimozione dalla coda 
        // Se eliminiamo l'unica traccia in riproduzione, il lettore si ferma e currentTrack diventa null
        assertNull(engine.getCurrentTrack(), "ERRORE: La traccia è ancora nella coda di riproduzione!");
    }
}