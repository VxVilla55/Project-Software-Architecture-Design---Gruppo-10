package com.group10.controller.playlist;

import com.group10.controller.MainViewController; 
import com.group10.controller.common.AbstractUIComponent;
import com.group10.model.MusicCatalogue;
import com.group10.model.builder.PlaylistBuilder;
import com.group10.model.PlaylistComponent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class PlaylistUIAdderController implements AbstractUIComponent, Initializable {

    @FXML
    private AnchorPane root;
    
    @FXML
    private TextField playlistNameInput;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    public Parent getRoot() {
        return root;
    }
    
    @FXML
    private void handleCreatePlaylist(ActionEvent event) {
        String playlistName = playlistNameInput.getText();
        try {
            PlaylistComponent playlist = new PlaylistBuilder()
            .setName(playlistName)
            .build();
            
            MusicCatalogue.getInstance().addPlaylist(playlist);
            
            playlistNameInput.clear();
            
            MainViewController.getInstance().closePopup();
            
        } catch (IllegalArgumentException ex ) {
            System.out.println("Errore: " + ex.getMessage());
            return;
        }
    }


@FXML
    private void handleAnnulla(ActionEvent event) {
        
        if (playlistNameInput != null) {
            playlistNameInput.clear();
        }
        
        MainViewController.getInstance().closePopup();

    }
}