/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model;

import com.group10.model.builder.TrackBuilder;
import com.group10.model.common.Subscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author group10
 */
public class MusicCatalogueTest {
    
    @BeforeEach
    void resetSingleton() throws Exception {
        //azzero il campo statico "singleton" via reflection per garantire isolamento tra test
        Field f = MusicCatalogue.class.getDeclaredField("singleton");
        f.setAccessible(true);
        f.set(null, null);
    }

    private TrackComponent makeTrack(String title, String author) {
        TrackBuilder b = new TrackBuilder();
        b.setTitle(title);
        b.setAuthor(author);
        b.setDuration(180);
        b.setGenre("Pop");
        b.setYear(2020);
        return new TrackComponent(b);
    }

    private PlaylistComponent makePlaylist(String name) {
        return new PlaylistComponent(name);
    }

    @Test
    void getInstance_primaChamata_restituisceIstanzaNonNulla() {
        assertNotNull(MusicCatalogue.getInstance());
    }

    @Test
    void getInstance_chiamateMultiple_restituisceStessaIstanza() {
        //due chiamate devono restituire esattamente lo stesso oggetto
        assertSame(MusicCatalogue.getInstance(), MusicCatalogue.getInstance());
    }

    @Test
    void addTrack_tracciaValida_presenteInLista() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        TrackComponent t = makeTrack("Song", "Artist");
        cat.addTrack(t);
        assertTrue(cat.getTracks().contains(t));
    }

    @Test
    void addTrack_piuTracce_tuttePresenteInLista() {
        //aggiungo due tracce distinte → entrambe devono essere recuperabili
        MusicCatalogue cat = MusicCatalogue.getInstance();
        TrackComponent t1 = makeTrack("Song1", "Artist1");
        TrackComponent t2 = makeTrack("Song2", "Artist2");
        cat.addTrack(t1);
        cat.addTrack(t2);
        assertTrue(cat.getTracks().contains(t1));
        assertTrue(cat.getTracks().contains(t2));
    }

    @Test
    void removeTrack_tracciaPresente_nonPiuInLista() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        TrackComponent t = makeTrack("Song", "Artist");
        cat.addTrack(t);
        cat.removeTrack(t);
        assertFalse(cat.getTracks().contains(t));
    }

    @Test
    void removeTrack_tracciaAssente_listaInvariata() {
        //rimuovo una traccia mai aggiunta → nessuna eccezione, size rimane 0
        MusicCatalogue cat = MusicCatalogue.getInstance();
        cat.removeTrack(makeTrack("NonEsiste", "X"));
        assertEquals(0, cat.getTracks().size());
    }

    @Test
    void addPlaylist_nomeUnico_aggiuntaCorrettamente() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        PlaylistComponent p = makePlaylist("Rock");
        cat.addPlaylist(p);
        assertSame(p, cat.getPlaylist("Rock"));
    }

    @Test
    void addPlaylist_nomeDuplicato_lanceIllegalArgumentException() {
        //aggiungo due playlist con lo stesso nome → la seconda deve lanciare eccezione
        MusicCatalogue cat = MusicCatalogue.getInstance();
        cat.addPlaylist(makePlaylist("Rock"));
        assertThrows(IllegalArgumentException.class,
            () -> cat.addPlaylist(makePlaylist("Rock")));
    }

    @Test
    void removePlaylist_playlistPresente_nonPiuRecuperabile() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        PlaylistComponent p = makePlaylist("Jazz");
        cat.addPlaylist(p);
        cat.removePlaylist(p);
        assertNull(cat.getPlaylist("Jazz"));
    }

    @Test
    void getPlaylist_nomeEsistente_restituiscePlaylistCorretta() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        PlaylistComponent p = makePlaylist("Pop");
        cat.addPlaylist(p);
        assertSame(p, cat.getPlaylist("Pop"));
    }

    @Test
    void getPlaylist_nomeInesistente_restituisceNull() {
        //chiedo una playlist con nome non registrato → null atteso
        assertNull(MusicCatalogue.getInstance().getPlaylist("NomeInesistente_XYZ"));
    }
    
    @Test
    void addTrackToPlaylist_tracciaAggiuntaCorrettamente() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        cat.addPlaylist(makePlaylist("PL"));
        TrackComponent t = makeTrack("T", "A");
        cat.addTrackToPlaylist("PL", t);
        assertTrue(cat.getPlaylist("PL").contains(t));
    }

    @Test
    void addTrackToPlaylist_tracciaGiaPresente_nessunDuplicato() {
        //aggiungo la stessa traccia due volte alla stessa playlist → size rimane 1
        MusicCatalogue cat = MusicCatalogue.getInstance();
        cat.addPlaylist(makePlaylist("PL"));
        TrackComponent t = makeTrack("T", "A");
        cat.addTrackToPlaylist("PL", t);
        cat.addTrackToPlaylist("PL", t);
        assertEquals(1, cat.getPlaylist("PL").getSize());
    }
    
    @Test
    void removeTrackFromPlaylist_tracciaPresente_rimossaCorrettamente() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        cat.addPlaylist(makePlaylist("PL"));
        TrackComponent t = makeTrack("T", "A");
        cat.addTrackToPlaylist("PL", t);
        cat.removeTrackFromPlaylist("PL", t);
        assertFalse(cat.getPlaylist("PL").contains(t));
    }

    @Test
    void replaceTrack_tracciaPresente_sostituitaInLibreria() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        TrackComponent old     = makeTrack("Old",     "A");
        TrackComponent updated = makeTrack("Updated", "A");
        cat.addTrack(old);
        cat.replaceTrack(old, updated);
        //la vecchia traccia non deve più essere presente
        assertFalse(cat.getTracks().contains(old));
        //la nuova traccia deve essere presente
        assertTrue(cat.getTracks().contains(updated));
    }

    @Test
    void replaceTrack_tracciaPresente_sostituitaAncheNellePlaylist() {
        //la sostituzione deve propagarsi a tutte le playlist che contenevano la traccia
        MusicCatalogue cat = MusicCatalogue.getInstance();
        TrackComponent old     = makeTrack("Old",     "A");
        TrackComponent updated = makeTrack("Updated", "A");
        cat.addTrack(old);
        cat.addPlaylist(makePlaylist("PL"));
        cat.addTrackToPlaylist("PL", old);
        cat.replaceTrack(old, updated);
        assertFalse(cat.getPlaylist("PL").contains(old));
        assertTrue(cat.getPlaylist("PL").contains(updated));
    }

    @Test
    void replaceTrack_oldTrackNull_nessunEccezione() {
        //oldTrack null → il metodo deve terminare silenziosamente
        assertDoesNotThrow(() ->
            MusicCatalogue.getInstance().replaceTrack(null, makeTrack("T", "A")));
    }

    @Test
    void replaceTrack_newTrackNull_nessunEccezione() {
        //newTrack null → il metodo deve terminare silenziosamente
        assertDoesNotThrow(() ->
            MusicCatalogue.getInstance().replaceTrack(makeTrack("T", "A"), null));
    }
@Test
    void addSubscriber_dopoAddTrack_updateVieneChamato() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        
        // Usiamo un flag atomico per tracciare la chiamata senza classi fake
        java.util.concurrent.atomic.AtomicBoolean chiamato = new java.util.concurrent.atomic.AtomicBoolean(false);
        
        // Lambda: implementiamo Subscriber al volo
        Subscriber sub = () -> chiamato.set(true);
        
        cat.addSubscriber(sub);
        cat.addTrack(makeTrack("T", "A"));
        
        assertTrue(chiamato.get(), "Il metodo update avrebbe dovuto essere chiamato!");
    }

    @Test
    void addSubscriber_dopoAddPlaylist_updateVieneChamato() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        java.util.concurrent.atomic.AtomicBoolean chiamato = new java.util.concurrent.atomic.AtomicBoolean(false);
        
        Subscriber sub = () -> chiamato.set(true);
        
        cat.addSubscriber(sub);
        cat.addPlaylist(makePlaylist("PL"));
        
        assertTrue(chiamato.get());
    }

    @Test
    void removeTracks_subscriberRimosso_updateNonVienePiuChiamato() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        java.util.concurrent.atomic.AtomicInteger contatoreChiamate = new java.util.concurrent.atomic.AtomicInteger(0);
        
        Subscriber sub = () -> contatoreChiamate.incrementAndGet();
        
        cat.addSubscriber(sub);
        cat.removeTracks(sub); // Nota: verifica se nel catalogo si chiama removeTracks o removeSubscriber
        
        cat.addTrack(makeTrack("T", "A"));
        
        assertEquals(0, contatoreChiamate.get(), "Il subscriber non doveva ricevere notifiche dopo la rimozione");
    }

    @Test
    void addSubscriber_piuSubscriber_tuttiNotificati() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        java.util.concurrent.atomic.AtomicBoolean chiamato1 = new java.util.concurrent.atomic.AtomicBoolean(false);
        java.util.concurrent.atomic.AtomicBoolean chiamato2 = new java.util.concurrent.atomic.AtomicBoolean(false);
        
        Subscriber sub1 = () -> chiamato1.set(true);
        Subscriber sub2 = () -> chiamato2.set(true);
        
        cat.addSubscriber(sub1);
        cat.addSubscriber(sub2);
        
        cat.addTrack(makeTrack("T", "A"));
        
        assertTrue(chiamato1.get());
        assertTrue(chiamato2.get());
    }
    // ====================================================================================
    // TASK T12.4 - TEST SU GET TOP TRACKS E GET TOP PLAYLISTS
    // ====================================================================================

    @Test
    void getTopTracks_ordinamentoCorretto() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        TrackComponent t1 = makeTrack("Canzone Poco Ascoltata", "A");
        TrackComponent t2 = makeTrack("Hit Estiva", "A");
        TrackComponent t3 = makeTrack("Canzone Media", "A");

        // Simuliamo gli ascolti
        for(int i = 0; i < 10; i++) t2.incrementPlayCount(); // 10 ascolti
        for(int i = 0; i < 5; i++) t3.incrementPlayCount();  // 5 ascolti
        // t1 rimane a 0 ascolti

        cat.addTrack(t1);
        cat.addTrack(t2);
        cat.addTrack(t3);

        // Chiediamo la top 2
        java.util.List<TrackComponent> top = cat.getTopTracks(2);

        assertEquals(2, top.size(), "La lista deve contenere esattamente 2 elementi");
        assertEquals(t2, top.get(0), "Al primo posto deve esserci la Hit Estiva (10 ascolti)");
        assertEquals(t3, top.get(1), "Al secondo posto deve esserci la Canzone Media (5 ascolti)");
    }

    @Test
    void getTopTracks_listaVuota() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        
        // Catalogo vuoto, chiediamo la top 5
        java.util.List<TrackComponent> top = cat.getTopTracks(5);
        
        assertNotNull(top, "La lista non deve essere null, ma vuota");
        assertTrue(top.isEmpty(), "La lista deve essere vuota se il catalogo non ha tracce");
    }

    @Test
    void getTopTracks_paritaDiContatore() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        TrackComponent t1 = makeTrack("Pareggio 1", "A");
        TrackComponent t2 = makeTrack("Pareggio 2", "B");

        // Stesso numero di ascolti
        t1.incrementPlayCount();
        t2.incrementPlayCount();

        cat.addTrack(t1);
        cat.addTrack(t2);

        java.util.List<TrackComponent> top = cat.getTopTracks(2);

        assertEquals(2, top.size());
        assertEquals(1, top.get(0).getPlayCount(), "Il primo elemento deve avere 1 ascolto");
        assertEquals(1, top.get(1).getPlayCount(), "Il secondo elemento deve avere 1 ascolto");
        assertTrue(top.contains(t1) && top.contains(t2), "Entrambe le tracce devono essere nella top 2");
    }

    @Test
    void getTopPlaylists_ordinamentoCorretto() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        
        PlaylistComponent p1 = makePlaylist("Playlist Flop"); // 0 ascolti
        PlaylistComponent p2 = makePlaylist("Playlist Top");  // 10 ascolti
        PlaylistComponent p3 = makePlaylist("Playlist Mid");  // 5 ascolti

        TrackComponent hit = makeTrack("Hit", "A");
        for(int i = 0; i < 10; i++) hit.incrementPlayCount();
        
        TrackComponent mid = makeTrack("Mid", "A");
        for(int i = 0; i < 5; i++) mid.incrementPlayCount();

        p2.add(hit); // p2 totalizza 10 ascolti
        p3.add(mid); // p3 totalizza 5 ascolti
        // p1 rimane vuota/a 0 ascolti

        cat.addPlaylist(p1);
        cat.addPlaylist(p2);
        cat.addPlaylist(p3);

        java.util.List<PlaylistComponent> top = cat.getTopPlaylists(2);

        assertEquals(2, top.size());
        assertEquals(p2, top.get(0), "Al primo posto deve esserci la Playlist Top");
        assertEquals(p3, top.get(1), "Al secondo posto deve esserci la Playlist Mid");
    }

    @Test
    void getTopPlaylists_listaVuota() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        java.util.List<PlaylistComponent> top = cat.getTopPlaylists(3);
        
        assertNotNull(top);
        assertTrue(top.isEmpty(), "La lista deve essere vuota se non ci sono playlist");
    }

    @Test
    void getTopPlaylists_paritaDiContatore() {
        MusicCatalogue cat = MusicCatalogue.getInstance();
        
        PlaylistComponent p1 = makePlaylist("Playlist Pari 1");
        PlaylistComponent p2 = makePlaylist("Playlist Pari 2");

        TrackComponent t1 = makeTrack("Track 1", "A");
        TrackComponent t2 = makeTrack("Track 2", "B");
        t1.incrementPlayCount(); // 1 ascolto
        t2.incrementPlayCount(); // 1 ascolto

        p1.add(t1);
        p2.add(t2);

        cat.addPlaylist(p1);
        cat.addPlaylist(p2);

        java.util.List<PlaylistComponent> top = cat.getTopPlaylists(2);

        assertEquals(2, top.size());
        assertEquals(1, top.get(0).getPlayCount());
        assertEquals(1, top.get(1).getPlayCount());
        assertTrue(top.contains(p1) && top.contains(p2), "Entrambe le playlist devono essere presenti");
    }
}