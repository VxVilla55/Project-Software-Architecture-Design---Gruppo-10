package com.group10.controller.track;

import com.group10.controller.MainViewController;
import com.group10.controller.factory.PlaylistUIComponentFactory;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AddToPlaylistController implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private Label trackTitleLabel;
    @FXML
    private ListView<String> playlistListView;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;
    
    private TrackComponent selectedTrack;


    private Map<String, BooleanProperty> itemStates = new HashMap<>();
    
    public AddToPlaylistController(TrackComponent track) {
        this.selectedTrack = track;
    }

    public Parent getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        trackTitleLabel.setText("Aggiungi \"" + selectedTrack.getTitle() + "\" a:");

        
        playlistListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            return itemStates.get(item);
        }));

        if (MusicCatalogue.getInstance().getPlaylists() != null) {
            for (PlaylistComponent playlist : MusicCatalogue.getInstance().getPlaylists().values()) {
                String playlistName = playlist.getName();
                
                itemStates.put(playlistName, new SimpleBooleanProperty(playlist.contains(selectedTrack)));
                
                playlistListView.getItems().add(playlistName);
            }
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        MainViewController.getInstance().closePopup();
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        boolean alMenoUnaAggiunta = false;

        for (Map.Entry<String, BooleanProperty> entry : itemStates.entrySet()) {
            String playlistName = entry.getKey();
            boolean isChecked = entry.getValue().get();
                      
            if (isChecked) {
                MusicCatalogue.getInstance().addTrackToPlaylist(playlistName, selectedTrack);
            } else {
                MusicCatalogue.getInstance().removeTrackFromPlaylist(playlistName, selectedTrack);
            }
        }
        
        MusicCatalogue.getInstance().notifySubscribers();
        MainViewController.getInstance().closePopup();
        
    }
}