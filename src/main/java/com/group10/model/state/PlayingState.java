package com.group10.model.state;

/**
 *
 * @author group10
 *
 * Stato di riproduzione in corso.
 *
 * PATTERN: ConcreteState del pattern State, PlaybackEngine è il Context
 */
public class PlayingState implements PlayerState {

    // già in riproduzione: il play non ha effetto
    @Override
    public void play(PlaybackEngine context) {
    }

    // sospende la simulazione mantenendo il punto raggiunto
    @Override
    public void pause(PlaybackEngine context) {
        context.stopSimulation();
        context.setState(new PausedState());
    }

    // interrompe la riproduzione e riporta la traccia all'inizio
    @Override
    public void stop(PlaybackEngine context) {
        context.stopSimulation();
        context.resetTime();
        context.setState(new StoppedState());
    }
}
