/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.group10.controller;

import com.group10.controller.factory.PlaylistUIComponentFactory;
import com.group10.controller.factory.TrackUIComponentFactory;
import com.group10.controller.playlist.PlaylistUIComponentCard;
import com.group10.controller.track.TrackUIComponentCard;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author group10
 */
public class HomepageController implements Initializable {

    @FXML
    private HBox playlistsCardsContainer;
    @FXML
    private HBox tracksCardsContainer;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //uso la factory per creare gli elementi item per ogni traccia della playlist
        TrackUIComponentCard trackCard;
        
        //DA SOSTITUIRE SE USIAMO ITERATOR PER PLAYLIST
        for(TrackComponent t: MusicCatalogue.getInstance().getTracks()) {
            trackCard = (TrackUIComponentCard) new TrackUIComponentFactory().createUIComponentCard(t);
            tracksCardsContainer.getChildren().add(trackCard.getRoot());
        }
        //uso la factory per creare gli elementi item per ogni traccia della playlist
        PlaylistUIComponentCard playlistCard;
        
        //DA SOSTITUIRE SE USIAMO ITERATOR PER PLAYLIST
        for(PlaylistComponent t: MusicCatalogue.getInstance().getPlaylists().values() ) {
            playlistCard = (PlaylistUIComponentCard) new PlaylistUIComponentFactory().createUIComponentCard(t);
            playlistsCardsContainer.getChildren().add(playlistCard.getRoot());
        }
    }    
    
}
