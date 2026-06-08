/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group10.controller.playlist;


import com.group10.controller.common.AbstractUIDetailsController;
import com.group10.controller.factory.TrackUIComponentFactory;
import com.group10.controller.track.TrackUIComponentItem;
import com.group10.controller.UIComponentFactory;
import com.group10.model.common.Playable;
import com.group10.model.PlaylistComponent;
import com.group10.model.builder.TrackBuilder;
import com.group10.model.TrackComponent;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author group10
 * 
 * product del pattern FACTORY
 */
public class PlaylistUIDetailsController extends AbstractUIDetailsController {
    
    @FXML
    private AnchorPane root;
    @FXML
    private ImageView playlistImageView;
    @FXML
    private Label playlistNameLabel;
    @FXML
    private Label playlistTracksCountLabel;
    @FXML
    private VBox tracksContainer;
    
    private TrackUIComponentFactory factory;
    private PlaylistComponent playlist;
    
    public PlaylistUIDetailsController(Playable playlist) {
        if (!(playlist instanceof PlaylistComponent)) {
            throw new RuntimeException("Impossibile creare DetailView per questa playlist");
        }
        else {
            this.playlist = (PlaylistComponent) playlist;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //uso la factory per creare gli elementi item per ogni traccia della playlist
        TrackUIComponentFactory factory = new TrackUIComponentFactory();
        TrackUIComponentItem item;
        
        //DA SOSTITUIRE SE USIAMO ITERATOR PER PLAYLIST
        for(TrackComponent t: playlist.getTracks()) {
            item = (TrackUIComponentItem) factory.createUIComponentItem(t);
            tracksContainer.getChildren().add(item.getRoot());
        }
        //playlistImageView;
        playlistNameLabel.setText(playlist.getName());
        Duration playlistDuration = Duration.ofSeconds(playlist.getDurationInSeconds());
        String formattedDuration = String.format("(%02d:%02d:%02d)", playlistDuration.toHoursPart(), playlistDuration.toMinutesPart(), playlistDuration.toSecondsPart());
        playlistTracksCountLabel.setText(String.valueOf(playlist.getSize())+" tracce "+ formattedDuration);
    }
    
    @Override
    public Parent getRoot() {
        return root;
    }    
}
