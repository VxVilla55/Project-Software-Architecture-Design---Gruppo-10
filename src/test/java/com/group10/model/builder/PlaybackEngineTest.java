package com.group10.model.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.group10.model.TrackComponent;
import com.group10.model.state.PausedState;
import com.group10.model.state.PlaybackEngine;
import com.group10.model.state.PlayingState;

public class PlaybackEngineTest {

    @Test
    public void testTransizioneInRiproduzione() {
        PlaybackEngine engine = PlaybackEngine.getInstance();
        engine.stop(); // Setup: assicura che il motore sia fermo
        
        TrackComponent track = new TrackBuilder()
                .setTitle("Brano Test")
                .setAuthor("Autore Test")
                .setDuration(100)
                .build();
        
        engine.addTrackToQueue(track);
        engine.play(); // Azione: avvia riproduzione
        
        // Verifica transizione a PlayingState
        assertTrue(engine.getState() instanceof PlayingState, "Il motore deve passare in PlayingState dopo il play.");
        engine.stop(); // Pulisce il timer
    }

    @Test
    public void testTransizioneInPausa() {
        PlaybackEngine engine = PlaybackEngine.getInstance();
        engine.stop(); 
        
        TrackComponent track = new TrackBuilder()
                .setTitle("Brano Test")
                .setAuthor("Autore Test")
                .setDuration(100)
                .build();
        
        engine.addTrackToQueue(track);
        engine.play();
        engine.pause(); // Azione: mette in pausa
        
        // Verifica transizione a PausedState
        assertTrue(engine.getState() instanceof PausedState, "Il motore deve passare in PausedState dopo la pausa.");
        engine.stop(); 
    }

    @Test
    public void testRipresaDaPausaResume() {
        PlaybackEngine engine = PlaybackEngine.getInstance();
        engine.stop();
        
        TrackComponent track = new TrackBuilder()
                .setTitle("Brano Test")
                .setAuthor("Autore Test")
                .setDuration(100)
                .build();
        
        engine.addTrackToQueue(track);
        engine.play();
        engine.pause();
        
        engine.play(); // Azione: Resume (riprende dalla pausa)
        
        // Verifica ritorno a PlayingState
        assertTrue(engine.getState() instanceof PlayingState, "Il motore deve tornare in PlayingState dopo il resume.");
        engine.stop(); 
    }

    @Test
    public void testComportamentoSkipNext() {
        PlaybackEngine engine = PlaybackEngine.getInstance();
        engine.stop();
        
        TrackComponent trackA = new TrackBuilder().setTitle("Traccia A").setAuthor("A").setDuration(10).build();
        TrackComponent trackB = new TrackBuilder().setTitle("Traccia B").setAuthor("B").setDuration(10).build();
        
        engine.addTrackToQueue(trackA);
        engine.addTrackToQueue(trackB);
        
        engine.play();
        engine.next(); // Azione: Skip in avanti
        
        // Verifica che dopo lo skip il motore stia ancora suonando e il tempo sia azzerato
        assertTrue(engine.getState() instanceof PlayingState, "Dopo lo skip il motore deve restare in riproduzione.");
        assertEquals(0, engine.getCurrentTime(), "Il tempo deve essere azzerato dopo lo skip in avanti.");
        
        engine.stop(); 
    }

    @Test
    public void testComportamentoSkipPrevious() {
        PlaybackEngine engine = PlaybackEngine.getInstance();
        engine.stop();
        
        TrackComponent trackA = new TrackBuilder().setTitle("Traccia A").setAuthor("A").setDuration(10).build();
        
        engine.addTrackToQueue(trackA);
        
        engine.play();
        engine.previous(); // Azione: Skip all'indietro (torna a inizio brano)
        
        // Verifica
        assertTrue(engine.getState() instanceof PlayingState, "Saltando all'indietro, deve restare in riproduzione.");
        assertEquals(0, engine.getCurrentTime(), "Il tempo deve essere azzerato saltando all'inizio della traccia.");
        
        engine.stop(); 
    }
}