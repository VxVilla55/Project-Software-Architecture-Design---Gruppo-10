package com.group10.model.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import com.group10.model.TrackComponent;

/**
 * @author group10
 * Singleton: classe che modella lo stato del player
 */
public class PlaybackEngine {
    
    private static PlaybackEngine instance;
    private PlayerState currentState;
    
    private List<TrackComponent> queue;
    private int currentIndex;
    private TrackComponent currentTrack;
    private double currentTime;
    private Timer timer;
    private Consumer<TrackComponent> onTrackChanged;
    private Consumer<Double> onTick;
    
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
    
    public void setCurrentTrack(TrackComponent track) {
        if (track == null) return;
        if (!queue.contains(track)) {
            queue.add(track);
        }
        this.currentIndex = queue.indexOf(track);
        cambiaTraccia(track);
    }

    
    public void clearQueue() {
        this.queue.clear();
        this.currentIndex = -1;
        this.currentTrack = null;
        this.currentTime = 0;
        stopSimulation();
        setState(new StoppedState());
        System.out.println("🧹 Coda svuotata.");
    }
    
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

    private void cambiaTraccia(TrackComponent newTrack) {
        this.currentTrack = newTrack;
        this.currentTime = 0;
        
        if (onTrackChanged != null) {
            javafx.application.Platform.runLater(() -> onTrackChanged.accept(newTrack));
        }
        
        if (onTick != null) {
            // Qui passiamo correttamente 0.0 per azzerare lo slider
            javafx.application.Platform.runLater(() -> onTick.accept(0.0));
        }
        
        if (currentState instanceof PlayingState) {
            stopSimulation();
            startSimulation();
        }
    }
    
    public void setOnTick(Consumer<Double> onTick) {
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
                    // Qui inviamo alla UI il tempo effettivo che sta scorrendo
                    javafx.application.Platform.runLater(() -> onTick.accept(currentTime));
                }
                
                if (currentTime >= currentTrack.getDurationInSeconds()) {
                    next();
                }
            }
        }, 100, 100); 
    }

    public void seek(double seconds) {
        this.currentTime = seconds;
        System.out.println("⏭️ Saltato al secondo: " + seconds);
    }
    
    public void stopSimulation() {
        if (timer != null) {
            timer.cancel(); 
            timer = null;
        }
    }

    public void resetTime() {
        this.currentTime = 0; 
    }

    // --- DELEGAZIONE AGLI STATI (T8.2) ---
    public void play() { currentState.play(this); }
    public void pause() { currentState.pause(this); }
    public void stop() { currentState.stop(this); }
}
