package com.group10.model.state;

public class StoppedState implements PlayerState {
    @Override
    public void play(PlaybackEngine context) {
        System.out.println("▶️ Riproduzione avviata.");
        context.setState(new PlayingState());
    }

    @Override
    public void pause(PlaybackEngine context) {
        System.out.println("⚠️ Impossibile mettere in pausa: il lettore è già fermo.");
    }

    @Override
    public void stop(PlaybackEngine context) {
        System.out.println("⏹️ Il lettore è già fermo.");
    }
}