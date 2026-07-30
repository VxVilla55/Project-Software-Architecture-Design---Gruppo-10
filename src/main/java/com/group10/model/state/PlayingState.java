package com.group10.model.state;

/**
 *
 * @author group10
 * PATTERN: State. ConcreteState, rappresenta il player mentre sta suonando una traccia.
 */
public class PlayingState implements PlayerState {

    private PlaybackEngine context;

    @Override
    public void setContext(PlaybackEngine context) {
        this.context = context;
    }

    // già in riproduzione: il play non ha effetto
    @Override
    public void play() {
    }

    // sospende la simulazione mantenendo il punto raggiunto
    @Override
    public void pause() {
        context.stopSimulation();
        context.changeState(new PausedState());
    }

    // interrompe la riproduzione e riporta la traccia all'inizio
    @Override
    public void stop() {
        context.stopSimulation();
        context.resetTime();
        context.changeState(new StoppedState());
    }
}
