/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.controller;

import com.group10.model.Playable;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author gruppo
 * 
 * ConcreteFactory: 
 * per JavaFx forse è meglio usare 'fxmlLoader.setControllerFactory(param -> new PlaylistUIComponent(playlist);' 
 * invece di 'fxmlLoader.setControllerFactory(new PlaylistUIComponentFactory(playlistSelected));' 
 */


public class TrackUIComponentFactory implements UIComponentFactory{

    @Override
    public AnchorPane createUIComponentItem(Playable model) {
        try{
            System.out.println("TUTTO OK DURANTE LA CREAZIONE DELL'ITEM:");
            return new TrackUIComponentItem(model);
        }
        catch (Exception ex) {
            System.err.println("ERRORE NELLA FACTORY DURANTE LA CREAZIONE DELL'ITEM:");
            return new TrackUIComponentItem();
        }
    }
}