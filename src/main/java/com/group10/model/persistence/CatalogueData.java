/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model.persistence;

import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;

import java.util.List;

/**
 *
 * @author group10
 */

public class CatalogueData {

    private final List<TrackComponent> tracks;
    private final List<PlaylistComponent> playlists;

    public CatalogueData(List<TrackComponent> tracks, List<PlaylistComponent> playlists) {
        this.tracks = tracks;
        this.playlists = playlists;
    }

    public List<TrackComponent> getTracks() {
        return tracks;
    }

    public List<PlaylistComponent> getPlaylists() {
        return playlists;
    }
}