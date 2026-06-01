/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group10.controller;


import com.group10.model.Playable;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackBuilder;
import com.group10.model.TrackComponent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
    private VBox vbox;
    
    private UIComponentFactory factory;
    
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
        factory = new TrackUIComponentFactory();
        TrackUIComponentItem item;
        
        //DA SOSTITUIRE SE USIAMO ITERATOR PER PLAYLIST
        for(TrackComponent t: playlist.getTracks()) {
            item = (TrackUIComponentItem) factory.createUIComponentItem(t);
            vbox.getChildren().add(item.getRoot());
        }
    }
    
    @Override
    public Parent getRoot() {
        return root;
    }    
}
