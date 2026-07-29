/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.group10.controller;

import com.group10.controller.playlist.PlaylistUIComponentCard;
import com.group10.controller.track.TrackUIComponentCard;
import com.group10.service.factory.TrackUIComponentFactory;
import com.group10.controller.track.TrackUIComponentItem;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.service.factory.PlaylistUIComponentFactory;
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
 *
 * controller della schermata home contenente le tracce e playlist consigliate, e la libreria
 * musicale per intero.
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //uso la factory per creare gli elementi item per ogni traccia della playlist
        TrackUIComponentCard trackCard;

        for(TrackComponent t: MusicCatalogue.getInstance().getTopTracks(3)) {
            trackCard = (TrackUIComponentCard) new TrackUIComponentFactory().createUIComponentCard(t);
            trackCardsContainer.getChildren().add(trackCard.getRoot());
        }
        //uso la factory per creare gli elementi item per ogni traccia della playlist
        PlaylistUIComponentCard playlistCard;

        for(PlaylistComponent t: MusicCatalogue.getInstance().getTopPlaylists(3)) {
            playlistCard = (PlaylistUIComponentCard) new PlaylistUIComponentFactory().createUIComponentCard(t);
            playlistCardsContainer.getChildren().add(playlistCard.getRoot());
        }
        
        TrackUIComponentItem trackItem;

        for(TrackComponent t: MusicCatalogue.getInstance().getTracks() ) {
            trackItem = (TrackUIComponentItem) new TrackUIComponentFactory().createUIComponentItem(t);
            trackItem.setIndexInContainer(trackItemsContainer.getChildren().size());
            trackItemsContainer.getChildren().add(trackItem.getRoot());
        }
    }    
    
}
