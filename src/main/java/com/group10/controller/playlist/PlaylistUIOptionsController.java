package com.group10.controller.playlist;

import com.group10.controller.common.AbstractUIComponent;
import com.group10.controller.MainViewController;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.common.Playable;
import com.group10.model.state.PlaybackEngine;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;

import java.util.Optional;

/**
 *
 * @author group10
 *
 * Controller del menu opzioni di una playlist: accodamento, rinomina, eliminazione
 */

public class PlaylistUIOptionsController implements AbstractUIComponent {

    @FXML
    private VBox root;

    private PlaylistComponent playlist;

    public PlaylistUIOptionsController(Playable p) {
        this.playlist = (PlaylistComponent) p;
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
    }

    // accoda tutte le tracce della playlist alla coda di riproduzione
    @FXML
    private void handleAddPlaylistToQueue(ActionEvent event) {
        for (TrackComponent track : playlist.getTracks()) {
            PlaybackEngine.getInstance().addTrackToQueue(track);
        }
        MainViewController.getInstance().closePopup();
    }

    // rinomina la playlist chiedendo il nuovo nome, validando l'univocita'
    @FXML
    private void handleRenamePlaylist(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog(playlist.getName());
        dialog.setHeaderText(null);
        dialog.setTitle("Rinomina playlist");
        dialog.setContentText("Nuovo nome:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String newName = result.get().trim();
            if (newName.isEmpty() || MusicCatalogue.getInstance().isPlaylistNameTaken(newName)) {
                return;
            }
            //MusicCatalogue.getInstance().renamePlaylist(playlist, newName);
        }
        MainViewController.getInstance().closePopup();
    }

    // elimina la playlist dalla libreria
    @FXML
    private void handleRemovePlaylist(ActionEvent event) {
        MusicCatalogue.getInstance().removePlaylist(playlist);
        MainViewController.getInstance().closePopup();
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
