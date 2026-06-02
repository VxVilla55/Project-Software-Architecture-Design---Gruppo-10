package com.group10.model; // Assicurati che il package coincida con la cartella!
import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import com.group10.model.state.PlaybackEngine;
import com.group10.model.state.PlayingState;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlaybackQueueTest {

    @Test
    public void testAccodamentoDuranteRiproduzione() {
        PlaybackEngine engine = PlaybackEngine.getInstance();
        engine.stop(); // Partiamo da una situazione pulita
        
        // 1. Creiamo e aggiungiamo la PRIMA traccia
        TrackComponent track1 = new TrackBuilder()
                .setTitle("Brano in esecuzione")
                .setAuthor("Autore A")
                .setDuration(200)
                .build();
        engine.addTrackToQueue(track1);
        
        // 2. Avviamo la riproduzione
        engine.play();
        
        // Verifichiamo che stia suonando la prima traccia
        assertEquals("Brano in esecuzione", engine.getCurrentTrack().getTitle(), 
            "Il motore dovrebbe riprodurre la prima traccia");
        assertTrue(engine.getState() instanceof PlayingState, 
            "Il motore deve essere in PlayingState");

        // 3. Creiamo e aggiungiamo la SECONDA traccia MENTRE il motore è in riproduzione
        TrackComponent track2 = new TrackBuilder()
                .setTitle("Brano accodato")
                .setAuthor("Autore B")
                .setDuration(180)
                .build();
        engine.addTrackToQueue(track2);
        
        // 4. Verifiche cruciali per la Task T6.6
        // Aggiungere una traccia NON deve aver cambiato la traccia corrente
        assertEquals("Brano in esecuzione", engine.getCurrentTrack().getTitle(), 
            "L'accodamento non deve interrompere o cambiare la traccia attualmente in riproduzione");
        
        // Aggiungere una traccia NON deve aver fermato la musica
        assertTrue(engine.getState() instanceof PlayingState, 
            "Il motore deve rimanere in riproduzione dopo aver accodato un nuovo brano");

        // 5. Test finale: se facciamo skip (next), deve passare esattamente alla traccia appena accodata
        engine.next();
        assertEquals("Brano accodato", engine.getCurrentTrack().getTitle(), 
            "Facendo next() si deve passare alla traccia che era stata accodata durante il play");
            
        engine.stop(); // Pulizia finale del timer
    }
}