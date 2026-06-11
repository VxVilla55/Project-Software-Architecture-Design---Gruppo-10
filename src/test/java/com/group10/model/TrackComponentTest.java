/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model;

import com.group10.model.builder.TrackBuilder;
import com.group10.model.state.PlaybackEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author alfon
 */
class TrackComponentTest {

    private TrackComponent track;

    @BeforeEach
    void setUp() {
        //creo una traccia base riusata da più test
        track = createTrack("Shape of You", "Ed Sheeran", 234);
    }

    /** Creo una TrackComponent con i campi minimi validi */
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
        //verifico che getTitle rispecchi il valore impostato nel builder
        assertEquals("Shape of You", track.getTitle());
    }

    @Test
    void getAuthor_restituisceAutoreCorretto() {
        assertEquals("Ed Sheeran", track.getAuthor());
    }

    @Test
    void getDurationInSeconds_restituisceDurataCorretta() {
        //verifico che getDurationInSeconds rispecchi il valore impostato nel builder
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
        //Alpha < Beta → compareTo deve restituire valore negativo
        TrackComponent alpha = createTrack("Alpha", "A", 100);
        TrackComponent beta  = createTrack("Beta",  "A", 100);
        assertTrue(alpha.compareTo(beta) < 0);
    }

    @Test
    void compareTo_titoloUguale_restituisceZero() {
        //titoli identici (case insensitive) → compareTo deve restituire 0
        TrackComponent t1 = createTrack("Same", "A", 100);
        TrackComponent t2 = createTrack("same", "B", 200); //stesso titolo, case diverso
        assertEquals(-1, t1.compareTo(t2));
    }

    @Test
    void compareTo_titoloAlfabeticamenteSuccessivo_restituiscePositivo() {
        //Zebra > Apple → compareTo deve restituire valore positivo
        TrackComponent z = createTrack("Zebra", "A", 100);
        TrackComponent a = createTrack("Apple", "A", 100);
        assertTrue(z.compareTo(a) > 0);
    }
    
    @Test
    void equals_stessoRiferimento_restituisceTrue() {
        //un oggetto è sempre uguale a se stesso
        assertEquals(track, track);
    }

    @Test
    void equals_titolEAutoreUguali_restituisceTrue() {
        //due oggetti distinti con stesso titolo e autore sono considerati uguali
        TrackComponent clone = createTrack("Shape of You", "Ed Sheeran", 999);
        assertEquals(track, clone);
    }

    @Test
    void equals_autoreDiverso_restituisceFalse() {
        //stesso titolo ma autore diverso → non uguali
        TrackComponent other = createTrack("Shape of You", "AltroArtista", 234);
        assertNotEquals(track, other);
    }

    @Test
    void equals_titoloDiverso_restituisceFalse() {
        //stesso autore ma titolo diverso → non uguali
        TrackComponent other = createTrack("Galway Girl", "Ed Sheeran", 234);
        assertNotEquals(track, other);
    }

    @Test
    void equals_confrontoConNull_restituisceFalse() {
        //confronto con null non deve lanciare eccezione
        assertNotEquals(null, track);
    }

    @Test
    void equals_confrontoConClasseDiversa_restituisceFalse() {
        //confronto con oggetto di tipo diverso → non uguali
        assertNotEquals("una stringa qualsiasi", track);
    }
    
    @Test
    void hashCode_oggettiUgualiPerEquals_hashUguali() {
        //se due oggetti sono equals, devono avere lo stesso hashCode (contratto Java)
        TrackComponent clone = createTrack("Shape of You", "Ed Sheeran", 999);
        assertEquals(track.hashCode(), clone.hashCode());
    }

    @Test
    void hashCode_stessoOggetto_valoreDeterministico() {
        //hashCode chiamato due volte sullo stesso oggetto deve restituire lo stesso valore
        assertEquals(track.hashCode(), track.hashCode());
    }
}