/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.model.persistence;
 
import java.util.ArrayList;
import java.util.List;
 
/**
 *
 * @author group10
 * 
 * Struttura dati che rappresenta il contenuto del file JSON:
 * l'elenco delle tracce e delle playlist salvate
 */
 
public class CatalogueFile {
 
    public List<TrackData> tracks = new ArrayList<>();
    public List<PlaylistData> playlists = new ArrayList<>();
 
}
 