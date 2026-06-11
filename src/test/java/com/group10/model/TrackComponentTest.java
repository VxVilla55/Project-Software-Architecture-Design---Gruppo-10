package com.group10.model;

import com.group10.model.builder.TrackBuilder;
import com.group10.model.state.PlaybackEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrackComponentTest {

    private TrackComponent track;

    @BeforeEach
    void setUp() {
        track = createTrack("Shape of You", "Ed Sheeran", 234);
    }

    private TrackComponent createTrack(String title, String author, int duration) {
        TrackBuilder b = new TrackBuilder();
        b.setTitle(title);
        b.setAuthor(author);
        b.setDuration(duration);
        b.setGenre("Pop");
        b.setYear(2017);
        return new TrackComponent(b);
    }

    @Test
    void getTitle_restituisceTitoloCorretto() {
        assertEquals("Shape of You", track.getTitle());
    }

    @Test
    void getAuthor_restituisceAutoreCorretto() {
        assertEquals("Ed Sheeran", track.getAuthor());
    }

    @Test
    void getDurationInSeconds_restituisceDurataCorretta() {
        assertEquals(234, track.getDurationInSeconds());
    }

    @Test
    void getGenre_restituisceGenereCorretto() {
        assertEquals("Pop", track.getGenre());
    }

    @Test
    void getYear_restituisceAnnoCorretto() {
        assertEquals(2017, track.getYear());
    }

    @Test
    void compareTo_OrdinaCorrettamente() {
        TrackComponent alpha = createTrack("Alpha", "A", 100);
        TrackComponent beta  = createTrack("Beta",  "A", 100);
        assertTrue(alpha.compareTo(beta) < 0);
    }

    @Test
    void compareTo_titoloUguale_restituisceZero() {
        TrackComponent t1 = createTrack("Same", "A", 100);
        TrackComponent t2 = createTrack("same", "B", 200);
        assertEquals(-1, t1.compareTo(t2));
    }

    @Test
    void compareTo_titoloAlfabeticamenteSuccessivo_restituiscePositivo() {
        TrackComponent z = createTrack("Zebra", "A", 100);
        TrackComponent a = createTrack("Apple", "A", 100);
        assertTrue(z.compareTo(a) > 0);
    }
    
    @Test
    void equals_stessoRiferimento_restituisceTrue() {
        assertEquals(track, track);
    }

    @Test
    void equals_titolEAutoreUguali_restituisceTrue() {
        TrackComponent clone = createTrack("Shape of You", "Ed Sheeran", 999);
        assertEquals(track, clone);
    }

    @Test
    void equals_autoreDiverso_restituisceFalse() {
        TrackComponent other = createTrack("Shape of You", "AltroArtista", 234);
        assertNotEquals(track, other);
    }

    @Test
    void equals_titoloDiverso_restituisceFalse() {
        TrackComponent other = createTrack("Galway Girl", "Ed Sheeran", 234);
        assertNotEquals(track, other);
    }

    @Test
    void equals_confrontoConNull_restituisceFalse() {
        assertNotEquals(null, track);
    }

    @Test
    void equals_confrontoConClasseDiversa_restituisceFalse() {
        assertNotEquals("una stringa qualsiasi", track);
    }
    
    @Test
    void hashCode_oggettiUgualiPerEquals_hashUguali() {
        TrackComponent clone = createTrack("Shape of You", "Ed Sheeran", 999);
        assertEquals(track.hashCode(), clone.hashCode());
    }

    @Test
    void hashCode_stessoOggetto_valoreDeterministico() {
        assertEquals(track.hashCode(), track.hashCode());
    }
}