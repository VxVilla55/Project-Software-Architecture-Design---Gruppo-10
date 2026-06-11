/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.group10.controller;

import com.group10.controller.factory.TrackUIComponentFactory;
import com.group10.controller.track.TrackUIComponentItem;
import com.group10.model.MusicCatalogue;
import com.group10.model.TrackComponent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author group10
 */
public class HomepageController implements Initializable {

    @FXML
    private VBox root;
    //prima sezione con la top delle playlist
    @FXML
    private VBox topPlaylistContainer;
    @FXML
    private Label topPlaylistLabel;
    @FXML
    private HBox playlistCardsContainer;
    //seconda sezione con la top delle tracce
    @FXML
    private VBox topTrackContainer;
    @FXML
    private Label topTracksLabel;
    @FXML
    private HBox trackCardsContainer;
    //terza sezione con tutte le tracce
    @FXML
    private VBox allTrackContainer;
    @FXML
    private Label allTracksLabel;
    @FXML
    private VBox trackItemsContainer;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //nascondi box per le stats perché non ancora implementata
        root.getChildren().remove(topPlaylistContainer);
        root.getChildren().remove(topTrackContainer);
        
        /*//uso la factory per creare gli elementi item per ogni traccia della playlist
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
        }*/
        
        TrackUIComponentItem trackItem;

        for(TrackComponent t: MusicCatalogue.getInstance().getTracks() ) {
            trackItem = (TrackUIComponentItem) new TrackUIComponentFactory().createUIComponentItem(t);
            trackItemsContainer.getChildren().add(trackItem.getRoot());
        }
    }    
    
}
