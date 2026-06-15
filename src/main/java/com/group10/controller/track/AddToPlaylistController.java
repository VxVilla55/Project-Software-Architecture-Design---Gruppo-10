package com.group10.controller.track;

import com.group10.controller.MainViewController;
import com.group10.controller.common.AbstractUIComponent;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.service.command.AddTrackToPlaylistCommand;
import com.group10.service.command.CommandManager;
import com.group10.service.command.RemoveTrackFromPlaylistCommand;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.VBox;

public class AddToPlaylistController implements AbstractUIComponent, Initializable {

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
    
    private final TrackComponent selectedTrack;
    private Map<String, BooleanProperty> itemStates;
    
    public AddToPlaylistController(TrackComponent track) {
        this.itemStates = new HashMap<>();
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
        for (Map.Entry<String, BooleanProperty> entry : itemStates.entrySet()) {
            String playlistName = entry.getKey();
            boolean isChecked = entry.getValue().get();
                      
            if (isChecked) {
                CommandManager.getInstance().executeCommand(new AddTrackToPlaylistCommand(selectedTrack, playlistName));
            } else {
                CommandManager.getInstance().executeCommand(new RemoveTrackFromPlaylistCommand(selectedTrack, playlistName));
            }
        }
        
        MusicCatalogue.getInstance().notifySubscribers();
        MainViewController.getInstance().closePopup();
        
    }
}