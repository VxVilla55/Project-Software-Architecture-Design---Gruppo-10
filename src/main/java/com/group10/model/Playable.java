/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.group10.model;

/**
 *
 * @author group10
 * 
 * Component del pattern Composite.
 * Astrae tutto ciò che è "riproducibile": sia una singola TrackComponent
 * sia una PlaylistComponent (Composite). 
 */

public interface Playable {

    // durata totale in secondi
    // per una playlist è la somma delle durate delle tracce contenute. 
    int getDurationInSeconds();

}