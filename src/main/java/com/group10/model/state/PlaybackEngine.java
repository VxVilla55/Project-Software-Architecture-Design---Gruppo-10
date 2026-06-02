package com.group10.model.state;

// ... qui inizia public class PlaybackEngine...
public class PlaybackEngine {
    
    private static PlaybackEngine instance;
    private PlayerState currentState;

    private PlaybackEngine() {
        // All'avvio, il player è fermo
        this.currentState = new StoppedState();
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

    // --- AZIONI ---
    public void play() {
        currentState.play(this);
    }

    public void pause() {
        currentState.pause(this);
    }

    public void stop() {
        currentState.stop(this);
    }
}