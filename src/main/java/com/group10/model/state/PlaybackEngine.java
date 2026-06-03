package com.group10.model.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import com.group10.model.TrackComponent;
/**
 *
 * @author group10
 * 
 * Singleton: classe che modella lo stato del player
 */
public class PlaybackEngine {
    
    private static PlaybackEngine instance;
    private PlayerState currentState;
    
    // --- VARIABILI T8.4: Coda e Tempo ---
    private List<TrackComponent> queue;
    private int currentIndex;
    private TrackComponent currentTrack;
    private double currentTime;
    private Timer timer;
    private Consumer<TrackComponent> onTrackChanged;
private Consumer<Integer> onTick;
    private PlaybackEngine() {
        this.currentState = new StoppedState();
        this.queue = new ArrayList<>();
        this.currentIndex = -1;
        this.currentTime = 0;
    }

    public static PlaybackEngine getInstance() {
        if (instance == null) {
            instance = new PlaybackEngine();
        }
        return instance;
    }

    public void setState(PlayerState state) {
        this.currentState = state;
    }

    public PlayerState getState() {
        return currentState;
    }
    
    public double getCurrentTime() {
        return currentTime;
    }
    // --- METODI PER L'INTERFACCIA GRAFICA (VIEW) ---
    
    public void setCurrentTrack(TrackComponent track) {
        if (track == null) return;

        // 1. Se la traccia non è già presente nella coda, la aggiungiamo in fondo
        if (!queue.contains(track)) {
            queue.add(track);
        }
        
        // 2. Aggiorniamo l'indice per mantenere coerenti i tasti Next e Previous
        this.currentIndex = queue.indexOf(track);
        
        // 3. Sfruttiamo il tuo metodo interno che è già perfetto per fare il cambio!
        cambiaTraccia(track);
    }

    // Ti consiglio caldamente di aggiungere anche questo metodo.
    // Quando dalla UI clicchi "Play" su una nuova Playlist, prima di accodare 
    // i nuovi brani vorrai sicuramente svuotare la coda precedente!
    public void clearQueue() {
        this.queue.clear();
        this.currentIndex = -1;
        this.currentTrack = null;
        this.currentTime = 0;
        stopSimulation();
        setState(new StoppedState());
        System.out.println("🧹 Coda svuotata.");
    }

    // --- LOGICA DELLA CODA E SKIP (T8.4) ---
    
    public void addTrackToQueue(TrackComponent track) {
        queue.add(track);
        if (currentTrack == null) {
            currentIndex = 0;
            currentTrack = queue.get(currentIndex);
        }
        System.out.println("✅ Accodato: " + track.getTitle());
    }
    
    public void addTrackAsNext(TrackComponent track) {
        System.out.println("NON ANCORA IMPLEMENTATA");
    }
    
    public TrackComponent getCurrentTrack() {
        return this.currentTrack;
    }

    public void next() {
        if (queue.isEmpty() || currentTrack == null) return;
        
        if (currentIndex < queue.size() - 1) {
            currentIndex++;
            cambiaTraccia(queue.get(currentIndex));
        } else {
            System.out.println("⏹️ Coda terminata.");
            stop();
        }
    }

    public void previous() {
        if (queue.isEmpty() || currentTrack == null) return;
        
        if (currentIndex > 0) {
            currentIndex--;
            cambiaTraccia(queue.get(currentIndex));
        } else {
            System.out.println("⏮️ Sei già alla prima traccia. Ricomincio.");
            cambiaTraccia(queue.get(0));
        }
    }

// Modifica cambiaTraccia per notificare la UI
private void cambiaTraccia(TrackComponent newTrack) {
    this.currentTrack = newTrack;
    this.currentTime = 0;
    
    // Notifica la UI che la traccia è cambiata (per aggiornare titolo e autore)
    if (onTrackChanged != null) {
        javafx.application.Platform.runLater(() -> onTrackChanged.accept(newTrack));
    }
    
    // Notifica per resettare lo slider a 0
    if (onTick != null) {
        javafx.application.Platform.runLater(() -> onTick.accept(0));
    }
    
    if (currentState instanceof PlayingState) {
        stopSimulation();
        startSimulation();
    }
}
    

    // --- SIMULAZIONE DEL TEMPO CHE PASSA (T8.4) ---
       public void setOnTick(java.util.function.Consumer<Integer> onTick) {
    this.onTick = onTick;
}
public void setOnTrackChanged(Consumer<TrackComponent> listener) {
    this.onTrackChanged = listener;
}
    public void startSimulation() {
        if (currentTrack == null) {
            System.out.println("⚠️ Nessuna traccia da riprodurre! Aggiungi un brano prima.");
            setState(new StoppedState());
            return;
        }
        System.out.println("▶️ IN RIPRODUZIONE: " + currentTrack.getTitle());
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentTime += 0.1;
                if (onTick != null) {
javafx.application.Platform.runLater(() -> onTick.accept((int)currentTime));
    }
                System.out.println("⏳ [" + currentTrack.getTitle() + "] " + currentTime + "s / " + currentTrack.getDurationInSeconds() + "s");
                
                // Se la canzone finisce, passa in automatico alla prossima
                if (currentTime >= currentTrack.getDurationInSeconds()) {
                    next();
                }
            }
        }, 100, 100); // Scatta ogni 1 secondo (1000 millisecondi)
    }
public void seek(int seconds) {
    // Impostiamo il tempo corrente al punto scelto dall'utente
    this.currentTime = seconds;
    
    // Opzionale: se il timer deve ripartire da qui, lo facciamo così:
    System.out.println("⏭️ Saltato al secondo: " + seconds);
}
    public void stopSimulation() {
        if (timer != null) {
            timer.cancel(); // Ferma il timer, ma NON azzera i secondi!
            timer = null;
        }
    }

    public void resetTime() {
        this.currentTime = 0; // Questo si chiama solo quando si preme STOP
    }

    // --- DELEGAZIONE AGLI STATI (T8.2) ---
    public void play() { currentState.play(this); }
    public void pause() { currentState.pause(this); }
    public void stop() { currentState.stop(this); }

}