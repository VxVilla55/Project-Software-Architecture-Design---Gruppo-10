package com.group10.model; // Assicurati che il package coincida con la cartella!
import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import com.group10.model.state.PlaybackEngine;
import com.group10.model.state.PlayingState;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlaybackQueueTest {

    private PlaybackEngine engine;

    @BeforeEach
    void setUp() {
        engine = PlaybackEngine.getInstance();
        engine.stop();
        engine.clearQueue();
        engine.setCurrentTrack(null);
    }

    @AfterEach
    void tearDown() {
        engine.stop();
        engine.clearQueue();
    }

    @Test
    public void testAccodamentoDuranteRiproduzione() {
        // prima traccia: durata LUNGA per evitare che finisca
        // durante il test e scatti il next() automatico del timer
        TrackComponent track1 = new TrackBuilder()
                .setTitle("Brano in esecuzione")
                .setAuthor("Autore A")
                .setDuration(9999) // durata lunghissima: il timer non scatterà
                .build();
        engine.addTrackToQueue(track1);

        // avvia riproduzione
        engine.play();

        assertEquals("Brano in esecuzione", engine.getCurrentTrack().getTitle(),
                "Il motore dovrebbe riprodurre la prima traccia");
        assertTrue(engine.getState() instanceof PlayingState,
                "Il motore deve essere in PlayingState");

        // accoda la seconda traccia MENTRE il motore è in play
        TrackComponent track2 = new TrackBuilder()
                .setTitle("Brano accodato")
                .setAuthor("Autore B")
                .setDuration(180)
                .build();
        engine.addTrackToQueue(track2);

        // verifica che la traccia corrente NON sia cambiata
        assertEquals("Brano in esecuzione", engine.getCurrentTrack().getTitle(),
                "L'accodamento non deve interrompere la traccia in riproduzione");
        assertTrue(engine.getState() instanceof PlayingState,
                "Il motore deve rimanere in PlayingState dopo l'accodamento");

        // skip manuale → deve passare al brano accodato
        engine.next();
        assertEquals("Brano accodato", engine.getCurrentTrack().getTitle(),
                "Dopo next() si deve passare alla traccia accodata");
    }
}