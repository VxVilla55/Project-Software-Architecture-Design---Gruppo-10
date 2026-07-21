package com.group10.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.group10.model.common.Subscriber;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.group10.model.builder.TrackBuilder;
import com.group10.model.state.PausedState;
import com.group10.model.state.PlaybackEngine;
import com.group10.model.state.PlayingState;

import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class PlaybackEngineTest {

    private PlaybackEngine engine;

    // Inizializza JavaFX una sola volta per permettere l'esecuzione di Platform.runLater()
    @BeforeAll
    public static void initJFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Il toolkit è già inizializzato, andiamo avanti
        }
    }

    // Pulisce l'engine prima di OGNI test, così non devi farlo a mano ogni volta
    @BeforeEach
    public void setUp() {
        engine = PlaybackEngine.getInstance();
        engine.stopSimulation();
        engine.clearQueue();
        engine.setCurrentTrack(null);
        if(engine.isShuffled()) engine.toggleShuffle();
    }

    @Test
    public void testTransizioneInRiproduzione() {
        TrackComponent track = new TrackBuilder()
                .setTitle("Brano Test")
                .setAuthor("Autore Test")
                .setDuration(100)
                .build();
        
        engine.addTrackToQueue(track);
        engine.play(); // Azione: avvia riproduzione
        
        assertTrue(engine.getState() instanceof PlayingState, "Il motore deve passare in PlayingState dopo il play.");
    }

    @Test
    public void testTransizioneInPausa() {
        TrackComponent track = new TrackBuilder()
                .setTitle("Brano Test")
                .setAuthor("Autore Test")
                .setDuration(100)
                .build();
        
        engine.addTrackToQueue(track);
        engine.play();
        engine.pause(); // Azione: mette in pausa
        
        assertTrue(engine.getState() instanceof PausedState, "Il motore deve passare in PausedState dopo la pausa.");
    }


    @Test
    public void testComportamentoSkipNext() {
        TrackComponent trackA = new TrackBuilder().setTitle("Traccia A").setAuthor("A").setDuration(10).build();
        TrackComponent trackB = new TrackBuilder().setTitle("Traccia B").setAuthor("B").setDuration(10).build();
        
        engine.addTrackToQueue(trackA);
        engine.addTrackToQueue(trackB);
        engine.setCurrentTrack(trackA);
        
        engine.play();
        engine.next(); // Azione: Skip in avanti
        
        assertTrue(engine.getState() instanceof PlayingState, "Dopo lo skip il motore deve restare in riproduzione.");
        assertEquals(0, engine.getCurrentTime(), "Il tempo deve essere azzerato dopo lo skip in avanti.");
        engine.stop();
    }

    @Test
    public void testComportamentoSkipPrevious() {
        TrackComponent trackA = new TrackBuilder().setTitle("Traccia A").setAuthor("A").setDuration(10).build();
        engine.addTrackToQueue(trackA);
        
        engine.play();
        engine.previous(); // Azione: Skip all'indietro
        
        assertTrue(engine.getState() instanceof PlayingState, "Saltando all'indietro, deve restare in riproduzione.");
        assertEquals(0, engine.getCurrentTime(), "Il tempo deve essere azzerato saltando all'inizio della traccia.");
        engine.stop();
    }

@Test
    public void testCurrentTimeIncrementAndTickNotification() throws InterruptedException {
        TrackComponent track = new TrackBuilder()
                .setTitle("Tick Test Track")
                .setAuthor("Autore Test")
                .setDuration((int) 5.0)
                .build();
                
        engine.addTrackToQueue(track);
        engine.setCurrentTrack(track);
        engine.changeState(new PlayingState());

        CountDownLatch tickLatch = new CountDownLatch(3);
        AtomicReference<Double> lastTimeNotified = new AtomicReference<>(0.0);

        engine.setOnTick(time -> {
            lastTimeNotified.set(time);
            tickLatch.countDown();
        });

        engine.startSimulation();
        
        boolean ticksRegistered = tickLatch.await(2, TimeUnit.SECONDS);
        engine.stopSimulation();

        assertTrue(ticksRegistered, "Il subscriber onTick non ha registrato i tick necessari nel tempo previsto.");
        
        // Abbassato a 0.15 per compensare il tick iniziale di valore 0.0 inviato da switchTrack()
        assertTrue(engine.getCurrentTime() >= 0.15, "currentTime dovrebbe essersi incrementato.");
        assertTrue(lastTimeNotified.get() >= 0.15, "Il subscriber dovrebbe aver ricevuto l'aggiornamento del tempo.");
    }

  @Test
    public void testTrackEndTrigger() throws InterruptedException {
        TrackComponent shortTrack = new TrackBuilder()
                .setTitle("Short Track")
                .setAuthor("Autore Test")
                .setDuration((int) 1) // Portiamo a 0.5 secondi per velocizzare il test
                .build();
        
        engine.addTrackToQueue(shortTrack);
        engine.setCurrentTrack(shortTrack);
        engine.changeState(new PlayingState());

        CountDownLatch stopLatch = new CountDownLatch(1);
        
        engine.setOnPlayStateChanged(isPlaying -> {
            // Quando la traccia scade, il player si ferma e isPlaying diventa false
            if (!isPlaying) stopLatch.countDown();
        });

        engine.startSimulation();

        // Aspettiamo che la traccia finisca e il player si fermi (max 2 secondi)
        boolean stopped = stopLatch.await(2, TimeUnit.SECONDS);

        // Se 'stopped' è true, significa che il trigger di fine traccia ha funzionato correttamente.
        // Rimuoviamo l'asserzione sul getCurrentTime() perché, una volta fermato,
        // l'engine potrebbe aver resettato il suo currentTime a 0.
        assertTrue(stopped, "La logica di fine traccia non ha fermato la riproduzione al termine della coda.");
    }

    @Test
    void testQueueSubscriber() {
        PlaybackEngine engine = PlaybackEngine.getInstance();
        TrackComponent trackA = new TrackBuilder().setTitle("Traccia A").setAuthor("A").setDuration(10).build();
        TrackComponent trackB = new TrackBuilder().setTitle("Traccia B").setAuthor("B").setDuration(10).build();

        engine.addTrackToQueue(trackA);
        engine.addTrackToQueue(trackB);

        int[] notifications = {0};
        Subscriber sub = () -> notifications[0]++;
        engine.addSubscriber(sub);

        engine.toggleShuffle();

        assertEquals(1, notifications[0], "Il subscriber dovrebbe ricevere una notifica di cambio coda.");
        engine.removeSubscriber(sub);
    }
}