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

    // Questa mappa associa il nome di ogni playlist a un valore booleano (selezionata o meno)
    private Map<String, BooleanProperty> itemStates = new HashMap<>();
    
    public AddToPlaylistController(TrackComponent track) {
        this.selectedTrack = track;
    }

    public Parent getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Mostriamo dinamicamente il titolo del brano che si sta salvando
        trackTitleLabel.setText("Aggiungi \"" + selectedTrack.getTitle() + "\" a:");

        // 1. Diciamo alla ListView di iniettare una Checkbox accanto al nome di ogni playlist
        playlistListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            return itemStates.get(item);
        }));

        // 2. Popoliamo la ListView e la mappa degli stati leggendo dal MusicCatalogue
        if (MusicCatalogue.getInstance().getPlaylists() != null) {
            for (PlaylistComponent playlist : MusicCatalogue.getInstance().getPlaylists().values()) {
                String playlistName = playlist.getName();
                
                // Di base, le caselle partono tutte deselezionate (false)
                itemStates.put(playlistName, new SimpleBooleanProperty(playlist.contains(selectedTrack)));
                
                // Aggiungiamo il nome della playlist nella lista visibile
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

        // Cicliamo sulla mappa per verificare quali playlist l'utente ha spuntato
        for (Map.Entry<String, BooleanProperty> entry : itemStates.entrySet()) {
            String playlistName = entry.getKey();
            boolean isChecked = entry.getValue().get();
            
            if (isChecked) {
                MusicCatalogue.getInstance().getPlaylist(playlistName).add(selectedTrack);
            } else {
                MusicCatalogue.getInstance().getPlaylist(playlistName).remove(selectedTrack);
            }
        }
        
        MusicCatalogue.getInstance().notifySubscribers();
        MainViewController.getInstance().closePopup();
        
    }
}