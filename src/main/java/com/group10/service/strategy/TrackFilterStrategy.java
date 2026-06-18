/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.service.strategy;

import com.group10.model.TrackComponent;

/**
 *
 * @author group10
 */
public interface TrackFilterStrategy {
    boolean matches(TrackComponent track);
}
