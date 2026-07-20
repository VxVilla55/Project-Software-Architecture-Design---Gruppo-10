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

    // avvia la riproduzione della traccia corrente
    @Override
    public void play(PlaybackEngine context) {
        context.setState(new PlayingState());
        context.startSimulation();
    }

    // lettore fermo: la pausa non ha effetto
    @Override
    public void pause(PlaybackEngine context) {
    }

    // lettore già fermo: lo stop non ha effetto
    @Override
    public void stop(PlaybackEngine context) {
    }
}
