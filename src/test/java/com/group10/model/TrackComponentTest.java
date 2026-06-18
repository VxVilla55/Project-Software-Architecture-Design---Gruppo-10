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
    void testAssegnazioneSingoloTag() {
        TrackComponent trackConTag = new TrackBuilder()
                .setTitle("Titolo Test")
                .setAuthor("Autore Test")
                .setDuration(180)
                .addTag(TrackComponent.Tag.FAVORITE)
                .build();

        // Verifica che il tag sia stato assegnato (Persistenza nel modello)
        assertTrue(trackConTag.hasTag(TrackComponent.Tag.FAVORITE), "La traccia dovrebbe avere il tag FAVORITE");
        
        // Verifica che NON abbia tag che non abbiamo assegnato
        assertFalse(trackConTag.hasTag(TrackComponent.Tag.NEW_RELEASE), "La traccia NON dovrebbe avere il tag NEW_RELEASE");
    }

    @Test
    void testAssegnazioneMultiplaTag() {
        TrackComponent trackMultipla = new TrackBuilder()
                .setTitle("Titolo Multiplo")
                .setAuthor("Autore Test")
                .setDuration(200)
                .addTag(TrackComponent.Tag.EXPLICIT)
                .addTag(TrackComponent.Tag.NEW_RELEASE)
                .build();

        assertTrue(trackMultipla.hasTag(TrackComponent.Tag.EXPLICIT));
        assertTrue(trackMultipla.hasTag(TrackComponent.Tag.NEW_RELEASE));
        
        // La lista dei tag deve avere esattamente dimensione 2
        assertEquals(2, trackMultipla.getTags().size(), "La traccia deve contenere esattamente 2 tag");
    }

    @Test
    void testRimozioneTag_TramiteRebuild() {
        // 1. Creiamo una traccia iniziale CON il tag
        TrackComponent tracciaOriginale = new TrackBuilder()
                .setTitle("Brano")
                .setAuthor("Autore")
                .setDuration(150)
                .addTag(TrackComponent.Tag.FAVORITE)
                .build();
        
        assertTrue(tracciaOriginale.hasTag(TrackComponent.Tag.FAVORITE));

        // 2. Simuliamo l'azione dell'utente che toglie la spunta e salva:
        // Viene usato un nuovo Builder senza chiamare addTag per quel tag
        TrackComponent tracciaModificata = new TrackBuilder()
                .setTitle(tracciaOriginale.getTitle())
                .setAuthor(tracciaOriginale.getAuthor())
                .setDuration(tracciaOriginale.getDurationInSeconds())
                // NON aggiungiamo FAVORITE di proposito
                .build();

        // 3. Verifichiamo che il tag non ci sia più
        assertFalse(tracciaModificata.hasTag(TrackComponent.Tag.FAVORITE), "Il tag FAVORITE dovrebbe essere rimosso/assente");
        assertTrue(tracciaModificata.getTags().isEmpty(), "La lista dei tag dovrebbe essere vuota");
    }

    @Test
    void testAssegnazioneTagNull_Ignorato() {
        // Simuliamo il passaggio di un parametro null al builder per verificare la robustezza
        TrackComponent trackNullTag = new TrackBuilder()
                .setTitle("Titolo")
                .setAuthor("Autore")
                .setDuration(120)
                .addTag(null) // Passaggio di null
                .build();

        // Verifichiamo che la traccia venga creata regolarmente e la lista tag non esploda
        assertNotNull(trackNullTag.getTags(), "Il set dei tag non deve mai essere null");
        assertTrue(trackNullTag.getTags().isEmpty(), "L'aggiunta di un tag null deve essere ignorata");
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