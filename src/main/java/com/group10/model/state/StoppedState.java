package com.group10.model.state;

/**
 *
 * @author group10
 * PATTERN: State. ConcreteState, rappresenta il player fermo: alla prossima play
 * si riparte dall'inizio della traccia (e' anche lo stato iniziale del player).
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
