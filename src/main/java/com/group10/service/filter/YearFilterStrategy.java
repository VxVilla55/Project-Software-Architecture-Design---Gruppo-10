/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.service.filter;

import com.group10.model.TrackComponent;

/**
 *
 * @author group10
 */
public class YearFilterStrategy implements TrackFilterStrategy {
    private final int from;
    private final int to;

    public YearFilterStrategy(int from, int to) {
        this.from = from;
        this.to = to;
    }
    
    @Override
    public boolean matches(TrackComponent track) {
        return track.getYear() >= from && track.getYear() <= to;
    }
}