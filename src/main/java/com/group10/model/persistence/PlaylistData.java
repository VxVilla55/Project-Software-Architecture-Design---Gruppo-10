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
 * Dati di una playlist nel formato di salvataggio: nome, playCount e i riferimenti
 * (titolo+autore) alle tracce che contiene, invece delle tracce intere.
 */
public class PlaylistData {
    String name;
    int playCount;
    List<String> trackKeys = new ArrayList<>();
}