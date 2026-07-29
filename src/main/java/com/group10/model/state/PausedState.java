package com.group10.model.state;

/**
 *
 * @author group10
 * PATTERN: State. ConcreteState, rappresenta il player in pausa: il punto raggiunto
 * nella traccia resta salvato finche' non si riprende.
 */
public class PausedState implements PlayerState {

    private PlaybackEngine context;

    @Override
    public void setContext(PlaybackEngine context) {
        this.context = context;
    }

    // riprende la riproduzione dal punto in cui era stata sospesa
    @Override
    public void play() {
        context.changeState(new PlayingState());
        context.startSimulation();
    }

    // già in pausa: la pausa non ha effetto
    @Override
    public void pause() {
    }

    // interrompe la riproduzione e riporta la traccia all'inizio
    @Override
    public void stop() {
        context.stopSimulation();
        context.resetTime();
        context.changeState(new StoppedState());
    }
}
