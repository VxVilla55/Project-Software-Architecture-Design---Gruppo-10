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



    // ====================================================================================
    // TASK T13.8 - TEST SU ASSEGNAZIONE, RIMOZIONE E PERSISTENZA DEI TAG
    // ====================================================================================

    @Test
    void build_withOneTag_trackHasOnlyThatTag() {
        TrackComponent taggedTrack = new TrackBuilder()
                .setTitle("Titolo Test")
                .setAuthor("Autore Test")
                .setDuration(180)
                .addTag(TrackComponent.Tag.FAVORITE)
                .build();

        // Verifica che il tag sia stato assegnato (Persistenza nel modello)
        assertTrue(taggedTrack.hasTag(TrackComponent.Tag.FAVORITE), "La traccia dovrebbe avere il tag FAVORITE");
        
        // Verifica che NON abbia tag che non abbiamo assegnato
        assertFalse(taggedTrack.hasTag(TrackComponent.Tag.NEW_RELEASE), "La traccia NON dovrebbe avere il tag NEW_RELEASE");
    }

    @Test
    void build_withTwoTags_trackHasBothTags() {
        TrackComponent multiTagTrack = new TrackBuilder()
                .setTitle("Titolo Multiplo")
                .setAuthor("Autore Test")
                .setDuration(200)
                .addTag(TrackComponent.Tag.EXPLICIT)
                .addTag(TrackComponent.Tag.NEW_RELEASE)
                .build();

        assertTrue(multiTagTrack.hasTag(TrackComponent.Tag.EXPLICIT));
        assertTrue(multiTagTrack.hasTag(TrackComponent.Tag.NEW_RELEASE));
        
        // La lista dei tag deve avere esattamente dimensione 2
        assertEquals(2, multiTagTrack.getTags().size(), "La traccia deve contenere esattamente 2 tag");
    }

    @Test
    void rebuild_withoutTag_tagIsNotPresentAnymore() {
        // 1. Creiamo una traccia iniziale CON il tag
        TrackComponent originalTrack = new TrackBuilder()
                .setTitle("Brano")
                .setAuthor("Autore")
                .setDuration(150)
                .addTag(TrackComponent.Tag.FAVORITE)
                .build();
        
        assertTrue(originalTrack.hasTag(TrackComponent.Tag.FAVORITE));

        // 2. Simuliamo l'azione dell'utente che toglie la spunta e salva:
        // Viene usato un nuovo Builder senza chiamare addTag per quel tag
        TrackComponent rebuiltTrack = new TrackBuilder()
                .setTitle(originalTrack.getTitle())
                .setAuthor(originalTrack.getAuthor())
                .setDuration(originalTrack.getDurationInSeconds())
                // NON aggiungiamo FAVORITE di proposito
                .build();

        // 3. Verifichiamo che il tag non ci sia più
        assertFalse(rebuiltTrack.hasTag(TrackComponent.Tag.FAVORITE), "Il tag FAVORITE dovrebbe essere rimosso/assente");
        assertTrue(rebuiltTrack.getTags().isEmpty(), "La lista dei tag dovrebbe essere vuota");
    }

    @Test
    void addTag_null_isIgnored() {
        // Simuliamo il passaggio di un parametro null al builder per verificare la robustezza
        TrackComponent trackWithNullTag = new TrackBuilder()
                .setTitle("Titolo")
                .setAuthor("Autore")
                .setDuration(120)
                .addTag(null) // Passaggio di null
                .build();

        // Verifichiamo che la traccia venga creata regolarmente e la lista tag non esploda
        assertNotNull(trackWithNullTag.getTags(), "Il set dei tag non deve mai essere null");
        assertTrue(trackWithNullTag.getTags().isEmpty(), "L'aggiunta di un tag null deve essere ignorata");
    }

    
    @Test
    void getTitle_returnsTitleFromBuilder() {
        assertEquals("Shape of You", track.getTitle());
    }

    @Test
    void getAuthor_returnsAuthorFromBuilder() {
        assertEquals("Ed Sheeran", track.getAuthor());
    }

    @Test
    void getDurationInSeconds_returnsDurationFromBuilder() {
        assertEquals(234, track.getDurationInSeconds());
    }

    @Test
    void getGenre_returnsGenreFromBuilder() {
        assertEquals("Pop", track.getGenre());
    }

    @Test
    void getYear_returnsYearFromBuilder() {
        assertEquals(2017, track.getYear());
    }

    @Test
    void compareTo_differentTitles_sortsAlphabetically() {
        TrackComponent alpha = createTrack("Alpha", "A", 100);
        TrackComponent beta  = createTrack("Beta",  "A", 100);
        assertTrue(alpha.compareTo(beta) < 0);
    }

    @Test
    void compareTo_sameTitleIgnoringCase_sortsByAuthor() {
        TrackComponent t1 = createTrack("Same", "A", 100);
        TrackComponent t2 = createTrack("same", "B", 200);
        assertEquals(-1, t1.compareTo(t2));
    }

    @Test
    void compareTo_titleAfterOther_returnsPositive() {
        TrackComponent z = createTrack("Zebra", "A", 100);
        TrackComponent a = createTrack("Apple", "A", 100);
        assertTrue(z.compareTo(a) > 0);
    }
    
    @Test
    void equals_sameReference_returnsTrue() {
        assertEquals(track, track);
    }

    @Test
    void equals_sameTitleAndAuthor_returnsTrue() {
        TrackComponent clone = createTrack("Shape of You", "Ed Sheeran", 999);
        assertEquals(track, clone);
    }

    @Test
    void equals_differentAuthor_returnsFalse() {
        TrackComponent other = createTrack("Shape of You", "AltroArtista", 234);
        assertNotEquals(track, other);
    }

    @Test
    void equals_differentTitle_returnsFalse() {
        TrackComponent other = createTrack("Galway Girl", "Ed Sheeran", 234);
        assertNotEquals(track, other);
    }

    @Test
    void equals_null_returnsFalse() {
        assertNotEquals(null, track);
    }

    @Test
    void equals_differentClass_returnsFalse() {
        assertNotEquals("una stringa qualsiasi", track);
    }
    
    @Test
    void hashCode_equalObjects_haveSameHash() {
        TrackComponent clone = createTrack("Shape of You", "Ed Sheeran", 999);
        assertEquals(track.hashCode(), clone.hashCode());
    }

    @Test
    void hashCode_sameObject_isStable() {
        assertEquals(track.hashCode(), track.hashCode());
    }
}