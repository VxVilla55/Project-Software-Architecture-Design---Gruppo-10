/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model.persistence;

import com.group10.model.TrackComponent;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author group10
 * 
 * Dati di una singola traccia nel formato di salvataggio
 */

public class TrackData {
    String title;
    String author;
    int duration;
    String genre;
    int year;
    int playCount;
    List<TrackComponent.Tag> tags = new ArrayList<>();
    String coverImagePath;
}
 