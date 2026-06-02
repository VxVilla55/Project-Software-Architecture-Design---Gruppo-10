package com.group10.model;

import com.group10.model.common.Playable;
import com.group10.model.builder.TrackBuilder;
import com.group10.model.state.PlaybackEngine;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CompositePlayTest {

    @Test
    public void testPlayUniformePlaylist() {
        PlaybackEngine engine = PlaybackEngine.getInstance();
        engine.stop(); 
        engine.clearQueue(); // Assicuriamoci che la coda sia vuota
        
        // 1. Creiamo due tracce
        TrackComponent t1 = new TrackBuilder().setTitle("Brano 1").setAuthor("A").setDuration(100).build();
        TrackComponent t2 = new TrackBuilder().setTitle("Brano 2").setAuthor("B").setDuration(150).build();
        
        // 2. Creiamo una playlist (usa il tuo Builder della playlist se preferisci!)
        PlaylistComponent playlist = new PlaylistComponent("La mia Playlist");
        playlist.getTracks().add(t1); 
        playlist.getTracks().add(t2);
        
        // 3. Le trattiamo in modo UNIFORME tramite l'interfaccia Playable
        Playable elementoDaRiprodurre = playlist;
        
        // Verifichiamo il calcolo uniforme della durata (100 + 150)
        assertEquals(250, elementoDaRiprodurre.getDurationInSeconds(), 
            "La durata della playlist deve essere la somma delle durate delle tracce");
        
        // 4. Riproduciamo con l'unico metodo uniforme
        elementoDaRiprodurre.playOnEngine(engine);
        engine.play();
        
        // 5. Verifichiamo che il motore abbia sbrogliato correttamente la playlist in singole tracce
        assertEquals("Brano 1", engine.getCurrentTrack().getTitle(), "La prima traccia deve essere Brano 1");
        
        engine.next(); // Skip alla prossima traccia della coda
        
        assertEquals("Brano 2", engine.getCurrentTrack().getTitle(), "La seconda traccia deve essere Brano 2");
        
        engine.stop(); // Pulisce il timer a fine test
    }
}