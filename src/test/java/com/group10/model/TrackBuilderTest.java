package com.group10.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrackBuilderTest {

    @Test
    public void testCostruzioneCorrettaCampiOpzionali() {
        // Usiamo i metodi corretti: setTitle, setAuthor, ecc.
        TrackComponent track = new TrackBuilder()
                .setTitle("Bohemian Rhapsody")
                .setAuthor("Queen")
                .setDuration(354)
                .setGenre("Rock")
                .setYear(1975)
                .build();

        assertEquals("Bohemian Rhapsody", track.getTitle());
        assertEquals("Queen", track.getAuthor());
        
        // *Nota per te: se nella tua classe TrackComponent il metodo si chiama 
        // getDuration() invece di getDurationInSeconds(), aggiorna questa riga!
        assertEquals(354, track.getDurationInSeconds()); 
        
        assertEquals("Rock", track.getGenre());
        assertEquals(1975, track.getYear());
    }

    @Test
    public void testFallimentoSenzaTitolo() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            new TrackBuilder()
                    .setAuthor("Autore Test")
                    .setDuration(200)
                    .build();
        });

        assertTrue(exception.getMessage().contains("titolo") || exception.getMessage().contains("Titolo"));
    }

    @Test
    public void testFallimentoSenzaAutore() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            new TrackBuilder()
                    .setTitle("Titolo Test")
                    .setDuration(200)
                    .build();
        });

        assertTrue(exception.getMessage().contains("autore") || exception.getMessage().contains("Autore"));
    }
}