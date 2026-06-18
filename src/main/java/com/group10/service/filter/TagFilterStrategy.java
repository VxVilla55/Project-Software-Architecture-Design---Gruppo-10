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
public class TagFilterStrategy implements TrackFilterStrategy {
    private final boolean isFavouriteSelected;
    private final boolean isNewReleaseSelected;
    private final boolean isExplicitSelected;

    public TagFilterStrategy(boolean isFavouriteSelected, boolean isNewReleaseSelected, boolean isExplicitSelected) {
        this.isFavouriteSelected = isFavouriteSelected;
        this.isNewReleaseSelected = isNewReleaseSelected;
        this.isExplicitSelected = isExplicitSelected;
    }
    
    @Override
    public boolean matches(TrackComponent track) {
        if (isFavouriteSelected && !track.isFavourite())
            return false;
        if (isNewReleaseSelected && !track.isNewRelease())
            return false;
        if (isExplicitSelected && !track.isExplicit())
            return false;
        return true;
    }
}