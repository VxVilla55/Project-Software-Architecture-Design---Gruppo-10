package com.group10.model.state;

import java.util.*;
import java.util.function.Consumer;

import com.group10.model.TrackComponent;

import com.group10.model.strategy.Sequential;
import com.group10.model.strategy.PlaybackMode;
import com.group10.model.strategy.RepeatPlaylist;
import com.group10.model.strategy.RepeatTrack;

/**
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

    // Pattern STRATEGY
    private PlaybackMode playbackMode = new Sequential();
    private boolean shuffled = false;
    private final List<TrackComponent> originalOrder = new ArrayList<>();
    
    private Consumer<TrackComponent> onTrackChanged;
    private Consumer<Double> onTick;
    private Consumer<Boolean> onPlayStateChanged; 
    
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
        
        // Avvisiamo la grafica che il brano è diventato "null" (vuoto)
        if (onTrackChanged != null) {
            javafx.application.Platform.runLater(() -> onTrackChanged.accept(null));
        }
        if (onTick != null) {
            javafx.application.Platform.runLater(() -> onTick.accept(0.0));
        }
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
    
    
    public void setOnPlayStateChanged(Consumer<Boolean> listener) {
        this.onPlayStateChanged = listener;
    }
    
    
    
    public void startSimulation() {
        if (currentTrack == null) {
            System.out.println("⚠️ Nessuna traccia da riprodurre! Aggiungi un brano prima.");
            setState(new StoppedState());
            return;
        }
        
        
        if (onPlayStateChanged != null) {
            javafx.application.Platform.runLater(() -> onPlayStateChanged.accept(true));
        }
        
        System.out.println("▶️ IN RIPRODUZIONE: " + currentTrack.getTitle());
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentTime += 0.1;
                if (onTick != null) {
                    javafx.application.Platform.runLater(() -> onTick.accept(currentTime));
                }
                
                if (currentTime >= currentTrack.getDurationInSeconds()) {
                    //next();
                    playbackMode.onTrackEnd(PlaybackEngine.this);
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
        
        
        if (onPlayStateChanged != null) {
            javafx.application.Platform.runLater(() -> onPlayStateChanged.accept(false));
        }
    }

    public void resetTime() {
        this.currentTime = 0; 
    }



public void removeTrackFromQueue(TrackComponent track) {
        // 1. CASO CRITICO: La traccia da eliminare è quella che sta suonando ORA
        if (currentTrack != null && currentTrack.equals(track)) {
            System.out.println("⚠️ La traccia eliminata era in riproduzione. Fermo il player.");
            
            queue.remove(track); // La togliamo fisicamente dalla lista

            if (queue.isEmpty()) {
                clearQueue(); // Svuota tutto e pulisce la grafica
            } else {
                // Se ci sono altre canzoni, ci posizioniamo su quella precedente in pausa
                if (currentIndex >= queue.size()) {
                    currentIndex = queue.size() - 1;
                    cambiaTraccia(queue.get(currentIndex));
                    stopSimulation();
                    setState(new StoppedState());
                } else {
                    cambiaTraccia(queue.get(currentIndex));
                }
            }
        } 
        // 2. CASO NORMALE: La traccia non suonava, ma era comunque in coda
        else if (queue.contains(track)) {
            int removedIndex = queue.indexOf(track);
            queue.remove(track);
            System.out.println("🗑️ Traccia '" + track.getTitle() + "' rimossa silenziosamente dalla coda.");

            // Aggiustiamo l'indice in modo che non salti la canzone successiva
            if (removedIndex < currentIndex) {
                currentIndex--;
            }
        } else {
            System.out.println("ℹ️ La traccia non era presente nella coda di riproduzione.");
        }
    }

    public void play() { currentState.play(this); }
    public void pause() { currentState.pause(this); }
    public void stop() { currentState.stop(this); }

    public void cycleRepeatMode() {
        if (playbackMode instanceof Sequential) {
            // se sequenziale, imposta loop su playlist
            playbackMode = new RepeatPlaylist();
        } else if (playbackMode instanceof RepeatPlaylist) {
            // se loop su playlist, imposta loop su singola traccia
            playbackMode = new RepeatTrack();
        } else {
            // ritorna a sequenziale
            playbackMode = new Sequential();
        }
    }

    public PlaybackMode getRepeatMode() {
        return playbackMode;
    }

    // tratta lo shuffle come toggle e usa due queue, quella mischiata e quella originale
    public void toggleShuffle() {
        shuffled = !shuffled;
        if (shuffled) {
            // salva l'ordine originale
            originalOrder.clear();
            originalOrder.addAll(queue);
            // fa shuffle sulla coda attuale
            Collections.shuffle(queue);
            if (currentTrack != null) {
                queue.remove(currentTrack);
                queue.add(0, currentTrack);
            }
        } else {
            // resetta l'ordine originale
            queue.clear();
            queue.addAll(originalOrder);
        }
        currentIndex = queue.indexOf(currentTrack);
    }

    public boolean getShuffled() {
        return shuffled;
    }

    public boolean hasNext() {
        return currentIndex < queue.size() - 1;
    }

    public void playFromStart() {
        currentIndex = 0;
        cambiaTraccia(queue.get(0));
        play();
    }

    public void replayCurrent() {
        resetTime();
        play();
    }
}