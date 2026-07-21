package com.group10.model.state;

/**
 *
 * @author group10
 *
 * Stato di riproduzione sospesa: il punto raggiunto viene conservato.
 *
 * PATTERN: ConcreteState del pattern State, PlaybackEngine è il Context
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
