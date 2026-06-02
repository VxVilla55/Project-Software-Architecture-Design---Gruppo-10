package com.group10.model.state;

public interface PlayerState {
    void play(PlaybackEngine context);
    void pause(PlaybackEngine context);
    void stop(PlaybackEngine context);
}