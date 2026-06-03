package com.group10.controller.playlist;

import com.group10.controller.common.AbstractUIAdderController;
import com.group10.controller.MainViewController; // Importiamo il MainViewController
import com.group10.model.MusicCatalogue;
import com.group10.model.builder.PlaylistBuilder;
import com.group10.model.PlaylistComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class PlaylistUIAdderController extends AbstractUIAdderController {

    @FXML
    private AnchorPane root;
    
    @FXML
    private TextField playlistNameInput;


    @FXML
    private void handleCreatePlaylist(ActionEvent event) {
        String playlistName = playlistNameInput.getText();
        try {
            PlaylistComponent playlist = new PlaylistBuilder()
            .setName(playlistName)
            .build();
            
            MusicCatalogue.getInstance().addPlaylist(playlist);
            
            playlistNameInput.clear();
            
            //chiudi il popup
            MainViewController.getInstance().closePopup();
            
        } catch (IllegalArgumentException ex ) {
            System.out.println("Errore: " + ex.getMessage());
            return;
        }
    }


@FXML
    private void handleAnnulla(ActionEvent event) {
        //pulizia dei campi di input
        if (playlistNameInput != null) {
            playlistNameInput.clear();
        }
        //chiudi il popup
        MainViewController.getInstance().closePopup();

    }

    @Override
    public Parent getRoot() {
        return root;
    }
}