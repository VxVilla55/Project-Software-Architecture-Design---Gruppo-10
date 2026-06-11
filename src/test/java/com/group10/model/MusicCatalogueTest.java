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
        //registro un subscriber mock e verifico che update() venga invocato da addTrack
        MusicCatalogue cat = MusicCatalogue.getInstance();
        Subscriber sub = mock(Subscriber.class);
        cat.addSubscriber(sub);
        cat.addTrack(makeTrack("T", "A"));
        verify(sub, atLeastOnce()).update();
    }

    @Test
    void addSubscriber_dopoAddPlaylist_updateVieneChamato() {
        //ogni mutazione al catalogo deve notificare i subscriber
        MusicCatalogue cat = MusicCatalogue.getInstance();
        Subscriber sub = mock(Subscriber.class);
        cat.addSubscriber(sub);
        cat.addPlaylist(makePlaylist("PL"));
        verify(sub, atLeastOnce()).update();
    }

    @Test
    void removeTracks_subscriberRimosso_updateNonVienePiuChiamato() {
        //dopo la rimozione del subscriber, update non deve essere invocato
        MusicCatalogue cat = MusicCatalogue.getInstance();
        Subscriber sub = mock(Subscriber.class);
        cat.addSubscriber(sub);
        cat.removeTracks(sub); //rimozione subscriber
        cat.addTrack(makeTrack("T", "A"));
        verify(sub, never()).update();
    }

    @Test
    void addSubscriber_piuSubscriber_tuttiNotificati() {
        //con due subscriber, entrambi devono ricevere update dopo una mutazione
        MusicCatalogue cat = MusicCatalogue.getInstance();
        Subscriber sub1 = mock(Subscriber.class);
        Subscriber sub2 = mock(Subscriber.class);
        cat.addSubscriber(sub1);
        cat.addSubscriber(sub2);
        cat.addTrack(makeTrack("T", "A"));
        verify(sub1, atLeastOnce()).update();
        verify(sub2, atLeastOnce()).update();
    }
}