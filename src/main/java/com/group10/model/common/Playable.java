package com.group10.model.common;

import com.group10.model.state.PlaybackEngine;

/**
 *
 * @author group10
 * PATTERN: Composite (il Component).
 * Astrae tutto cio' che e' "riproducibile": sia una singola TrackComponent (la Leaf)
 * sia una PlaylistComponent (il Composite, che internamente ha piu' tracce). Grazie a
 * questa interfaccia il resto del codice (PlaybackEngine, i controller) puo' trattare
 * una traccia singola e una playlist intera esattamente allo stesso modo, chiamando
 * playOnEngine() senza doversi preoccupare di quale dei due sia davvero: sara' la
 * classe concreta a decidere come riprodursi (una traccia si accoda da sola, una
 * playlist accoda in blocco tutte le sue tracce).
 */
public interface Playable {

    //durata totale in secondi
    int getDurationInSeconds();

    void playOnEngine(PlaybackEngine engine);

}