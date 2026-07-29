/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.group10.model.common;

/**
 *
 * @author group10
 * PATTERN: Observer (il Publisher).
 * Chi implementa questa interfaccia (MusicCatalogue, PlaybackEngine) tiene una lista di
 * Subscriber e li avvisa chiamando notifySubscribers() ogni volta che il proprio stato
 * cambia. Cosi' il model non deve conoscere le classi concrete delle viste o del gestore
 * di persistenza.
 */
public interface Publisher {

    public void notifySubscribers();

}
