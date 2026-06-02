package com.group10.model.state;

public class PausedState implements PlayerState {
    @Override
    public void play(PlaybackEngine context) {
        System.out.println("▶️ Riprendo la riproduzione dalla pausa.");
        context.setState(new PlayingState());
    }

    @Override
    public void pause(PlaybackEngine context) {
        System.out.println("⚠️ Il lettore è già in pausa.");
    }

    @Override
    public void stop(PlaybackEngine context) {
        System.out.println("⏹️ Riproduzione interrotta dalla pausa.");
        context.setState(new StoppedState());
    }
}