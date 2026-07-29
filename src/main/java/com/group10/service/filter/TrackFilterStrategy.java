/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.service.filter;

import com.group10.model.TrackComponent;

/**
 *
 * @author group10
 * PATTERN: Strategy (l'interfaccia Strategy).
 * Un solo metodo, matches(track), che decide se una traccia rispetta un certo criterio.
 * Il Context e' PlaylistComponent: una playlist "automatica" tiene una lista di
 * TrackFilterStrategy e considera una traccia valida solo se TUTTE le strategie
 * restituiscono true (vedi PlaylistComponent.getTracks()).
 */
public interface TrackFilterStrategy {
    boolean matches(TrackComponent track);
}
