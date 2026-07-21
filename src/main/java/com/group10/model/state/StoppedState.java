package com.group10.model.state;

/**
 *
 * @author group10
 *
 * Stato di lettore fermo: la riproduzione riparte dall'inizio della traccia.
 *
 * PATTERN: ConcreteState del pattern State, PlaybackEngine è il Context
 */
public class StoppedState implements PlayerState {

    private PlaybackEngine context;

    @Override
    public void setContext(PlaybackEngine context) {
        this.context = context;
    }

    // avvia la riproduzione della traccia corrente
    @Override
    public void play() {
        context.changeState(new PlayingState());
        context.startSimulation();
    }

    // lettore fermo: la pausa non ha effetto
    @Override
    public void pause() {
    }

    // lettore già fermo: lo stop non ha effetto
    @Override
    public void stop() {
    }
}
