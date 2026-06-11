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
 * @author group10
 */

class PlaylistComponentTest {

    private PlaylistComponent playlist;

    @BeforeEach
    void setUp() {
        //creo una playlist base riusata da più test
        playlist = new PlaylistComponent("My Playlist");
    }
    private TrackComponent makeTrack(String title, String author, int duration) {
        TrackBuilder b = new TrackBuilder();
        b.setTitle(title);
        b.setAuthor(author);
        b.setDuration(duration);
        b.setGenre("Pop");
        b.setYear(2020);
        return new TrackComponent(b);
    }
    
    @Test
    void costruttore_nomeValido_inizializzaCorrettamente() {
        //verifico nome, dimensione iniziale e stato vuoto
        assertEquals("My Playlist", playlist.getName());
        assertEquals(0, playlist.getSize());
        assertTrue(playlist.isEmpty());
    }

    @Test
    void costruttore_nomeConSpaziEsterni_vieneTrimmato() {
        //gli spazi attorno al nome devono essere rimossi
        PlaylistComponent p = new PlaylistComponent("  Rock  ");
        assertEquals("Rock", p.getName());
    }

    @Test
    void costruttore_nomeNull_lanceIllegalArgumentException() {
        //nome null → eccezione attesa
        assertThrows(IllegalArgumentException.class,
            () -> new PlaylistComponent((String) null));
    }

    @Test
    void costruttore_nomeBlank_lanceIllegalArgumentException() {
        //nome composto solo da spazi → eccezione attesa
        assertThrows(IllegalArgumentException.class,
            () -> new PlaylistComponent("   "));
    }

    @Test
    void costruttoreDefault_nomeSegnaposto() {
        //il costruttore no-arg deve assegnare il nome di default
        PlaylistComponent p = new PlaylistComponent();
        assertEquals("Nuova Playlist", p.getName());
        assertTrue(p.isEmpty());
    }
    
    @Test
    void setName_nomeValido_aggiornaNome() {
        //cambio il nome con uno valido → deve essere aggiornato
        playlist.setName("Updated Name");
        assertEquals("Updated Name", playlist.getName());
    }

    @Test
    void setName_nomeNull_lanceIllegalArgumentException() {
        //setName(null) deve lanciare eccezione senza modificare lo stato
        assertThrows(IllegalArgumentException.class,
            () -> playlist.setName(null));
    }

    @Test
    void setName_nomeBlank_lanceIllegalArgumentException() {
        //setName con stringa di soli spazi deve lanciare eccezione
        assertThrows(IllegalArgumentException.class,
            () -> playlist.setName("   "));
    }

    @Test
    void add_tracciaNuova_aggiuntaEDimensioneCresceDiUno() {
        //aggiungo una traccia non presente → size deve diventare 1
        playlist.add(makeTrack("T1", "A", 100));
        assertEquals(1, playlist.getSize());
    }

    @Test
    void add_tracciaGiaPresente_nessunDuplicato() {
        //aggiungo la stessa traccia due volte → size deve rimanere 1
        TrackComponent t = makeTrack("T1", "A", 100);
        playlist.add(t);
        playlist.add(t);
        assertEquals(1, playlist.getSize());
    }
    
    @Test
    void remove_tracciaPresente_rimossaERestituisceTrue() {
        //rimuovo una traccia presente → true e size torna a 0
        TrackComponent t = makeTrack("T1", "A", 100);
        playlist.add(t);
        assertTrue(playlist.remove(t));
        assertEquals(0, playlist.getSize());
    }

    @Test
    void remove_tracciaAssente_restituisceFalse() {
        //rimuovo una traccia mai aggiunta → false, stato invariato
        assertFalse(playlist.remove(makeTrack("T1", "A", 100)));
    }

    @Test
    void contains_tracciaPresente_restituisceTrue() {
        TrackComponent t = makeTrack("T1", "A", 100);
        playlist.add(t);
        assertTrue(playlist.contains(t));
    }

    @Test
    void contains_tracciaAssente_restituisceFalse() {
        //verifico che una traccia mai aggiunta non risulti contenuta
        assertFalse(playlist.contains(makeTrack("T1", "A", 100)));
    }

    @Test
    void isEmpty_playlistVuota_restituisceTrue() {
        assertTrue(playlist.isEmpty());
    }

    @Test
    void isEmpty_playlistConTracce_restituisceFalse() {
        playlist.add(makeTrack("T1", "A", 100));
        assertFalse(playlist.isEmpty());
    }

    @Test
    void getSize_playlistVuota_restituisceZero() {
        assertEquals(0, playlist.getSize());
    }

    @Test
    void getSize_dopoAggiunte_restituisceConteggioCorretto() {
        //aggiungo 3 tracce distinte → size deve essere 3
        playlist.add(makeTrack("T1", "A", 100));
        playlist.add(makeTrack("T2", "B", 100));
        playlist.add(makeTrack("T3", "C", 100));
        assertEquals(3, playlist.getSize());
    }

    @Test
    void getDurationInSeconds_playlistVuota_restituisceZero() {
        assertEquals(0, playlist.getDurationInSeconds());
    }

    @Test
    void getDurationInSeconds_piuTracce_restituisceSommaDurate() {
        //aggiungo due tracce da 120s e 80s → totale atteso 200s
        playlist.add(makeTrack("T1", "A", 120));
        playlist.add(makeTrack("T2", "B", 80));
        assertEquals(200, playlist.getDurationInSeconds());
    }

    @Test
    void updateTrack_tracciaPresente_vecchiaRimossaNuovaAggiunta() {
        //aggiorno una traccia presente → la vecchia non c'è più, la nuova sì
        TrackComponent old     = makeTrack("Old Song", "A", 100);
        TrackComponent updated = makeTrack("New Song", "A", 200);
        playlist.add(old);
        playlist.updateTrack(old, updated);
        assertFalse(playlist.contains(old));
        assertTrue(playlist.contains(updated));
    }

    @Test
    void updateTrack_tracciaAssente_sizeInvariata() {
        //updateTrack su traccia mai aggiunta non deve modificare la playlist
        TrackComponent existing = makeTrack("Existing", "A", 100);
        TrackComponent absent   = makeTrack("Absent",   "B", 100);
        TrackComponent updated  = makeTrack("Updated",  "B", 200);
        playlist.add(existing);
        playlist.updateTrack(absent, updated);
        assertEquals(1, playlist.getSize());
        assertTrue(playlist.contains(existing));
    }

@Test
    void playOnEngine_impostaCurrentPlaylistSuEngine() {
        // 1. Setup: prendiamo l'engine reale e resettiamo la playlist
        PlaybackEngine engine = PlaybackEngine.getInstance();
        engine.setCurrentPlaylist(null); 
        
        // 2. Azione: chiamiamo il metodo
        playlist.playOnEngine(engine);
        
        // 3. Asserzione: verifichiamo che l'engine abbia ora salvato questa playlist
        assertEquals(playlist, engine.getCurrentPlaylist(), 
            "La playlist corrente dell'engine deve essere aggiornata dopo aver chiamato playOnEngine.");
    }

    @Test
    void playOnEngine_aggiungeTutteLeTracceAllaQueueDelEngine() {
        // 1. Setup
        PlaybackEngine engine = PlaybackEngine.getInstance();
        engine.clearQueue(); // Svuotiamo la coda dell'engine
        
        TrackComponent track1 = makeTrack("T1", "A", 100);
        TrackComponent track2 = makeTrack("T2", "B", 200);
        playlist.add(track1);
        playlist.add(track2);
        
        // 2. Azione
        playlist.playOnEngine(engine);
        
        // 3. Asserzioni
        // Dal codice di PlaybackEngine, sappiamo che addListToQueue imposta 
        // automaticamente la prima traccia della lista come "currentTrack".
        assertEquals(track1, engine.getCurrentTrack(), 
            "La prima traccia della playlist deve diventare la traccia corrente dell'engine.");
            
        // Se vogliamo essere ancora più sicuri, testiamo che ci sia anche la successiva
        engine.next();
        assertEquals(track2, engine.getCurrentTrack(), 
            "La seconda traccia della playlist deve essere accodata correttamente.");
    }
}