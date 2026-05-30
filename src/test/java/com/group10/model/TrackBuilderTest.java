package com.group10.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrackBuilderTest {

    @Test
    public void testCostruzioneCorrettaCampiOpzionali() {
        TrackComponent track = new TrackBuilder()
                .title("Bohemian Rhapsody")
                .author("Queen")
                .duration(354)
                .genre("Rock")
                .year(1975)
                .build();

        assertEquals("Bohemian Rhapsody", track.getTitle());
        assertEquals("Queen", track.getAuthor());
        assertEquals(354, track.getDurationInSeconds());
        assertEquals("Rock", track.getGenre());
        assertEquals(1975, track.getYear());
    }

    @Test
    public void testFallimentoSenzaTitolo() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            new TrackBuilder()
                    .author("Autore Test")
                    .duration(200)
                    .build();
        });

        assertTrue(exception.getMessage().contains("titolo") || exception.getMessage().contains("Titolo"));
    }

    @Test
    public void testFallimentoSenzaAutore() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            new TrackBuilder()
                    .title("Titolo Test")
                    .duration(200)
                    .build();
        });

        assertTrue(exception.getMessage().contains("autore") || exception.getMessage().contains("Autore"));
    }
}