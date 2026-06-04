package com.group10.model.state;

public class PausedState implements PlayerState {
    @Override
    public void play(PlaybackEngine context) {
        System.out.println("▶️ Riprendo la riproduzione esattamente da dove l'ho lasciata.");
        context.setState(new PlayingState());
        context.startSimulation(); 
    }

    @Override
    public void pause(PlaybackEngine context) {
        System.out.println("⚠️ Il lettore è già in pausa.");
    }

    @Override
    public void stop(PlaybackEngine context) {
        System.out.println("⏹️ Riproduzione interrotta dalla pausa.");
        context.stopSimulation();
        context.resetTime();
        context.setState(new StoppedState());
    }
}