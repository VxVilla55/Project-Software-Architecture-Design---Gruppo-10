package com.group10.model.state;

/**
 *
 * @author group10
 *
 * Astrae il comportamento del lettore in base allo stato in cui si trova:
 * ogni stato concreto reagisce a play/pause/stop a modo proprio ed è
 * responsabile della transizione verso lo stato successivo.
 *
 * PATTERN: State del pattern omonimo, PlaybackEngine è il Context
 */
public interface PlayerState {

    void play(PlaybackEngine context);

    void pause(PlaybackEngine context);

    void stop(PlaybackEngine context);
}
