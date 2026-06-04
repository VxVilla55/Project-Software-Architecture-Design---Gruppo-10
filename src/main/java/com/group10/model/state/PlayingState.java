package com.group10.model.state;

public class PlayingState implements PlayerState {
    @Override
    public void play(PlaybackEngine context) {
        System.out.println("⚠️ La traccia è già in riproduzione.");
    }

    @Override
    public void pause(PlaybackEngine context) {
        System.out.println("⏸️ Riproduzione messa in pausa al secondo " + context.getCurrentTime() + ".");
        context.stopSimulation(); 
        context.setState(new PausedState());
    }

    @Override
    public void stop(PlaybackEngine context) {
        System.out.println("⏹️ Riproduzione interrotta. Torno all'inizio.");
        context.stopSimulation(); 
        context.resetTime();      
        context.setState(new StoppedState());
    }
}