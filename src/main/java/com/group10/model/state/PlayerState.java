package com.group10.model.state;

/**
 *
 * @author group10
 * PATTERN: State (l'interfaccia State).
 * Rappresenta lo stato in cui si trova il player in un dato momento: ogni stato concreto
 * implementa play/pause/stop a modo suo e decide qual e' lo stato successivo. Il Context e'
 * PlaybackEngine.
 */
public interface PlayerState {

    void setContext(PlaybackEngine context);

    void play();

    void pause();

    void stop();
}
