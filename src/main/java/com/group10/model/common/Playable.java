package com.group10.model.common;

/**
 * Component del pattern Composite.
 * Astrae tutto ciò che è "riproducibile": sia una singola TrackComponent
 * sia una PlaylistComponent (Composite). 
 */
public interface Playable {

    //durata totale in secondi
    int getDurationInSeconds();

}