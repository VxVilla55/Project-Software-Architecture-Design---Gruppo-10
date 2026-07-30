package com.group10.controller.track;

import com.group10.controller.MainViewController;
import com.group10.controller.common.AbstractUIComponent;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.common.Playable;
import com.group10.model.TrackComponent;
import com.group10.model.state.PlaybackEngine;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 *
 * @author group10
 * PATTERN: Abstract Factory. Prodotto specifico della famiglia Track, la famiglia Playlist non ne ha bisogno.
 * Controller della riga di una traccia dentro la vista Coda.
 */
public class TrackUIQueueComponentItem implements AbstractUIComponent, Initializable {
    @FXML private HBox root;
    @FXML private Label indexLabel;
    @FXML private Label titleLabel;
    @FXML private Label artistLabel;
    @FXML private ImageView coverImage;
    
    
    private TrackComponent track;
    private PlaylistComponent contextPlaylist = null;
    
    public void setContextPlaylist(PlaylistComponent playlist) {
        this.contextPlaylist = playlist;
    }

    //mostra la posizione della traccia nella coda al posto del vecchio trattino
    public void setIndex(int position) {
        indexLabel.setText(String.valueOf(position));
    }
    
    public TrackUIQueueComponentItem(TrackComponent track) {
        this.track = track;
    }
    
    public TrackUIQueueComponentItem(Playable t) {
        if (!(t instanceof TrackComponent)) {
            throw new RuntimeException("Impossibile crearne l'item.");
        } else {
            track = (TrackComponent) t;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleLabel.setText(track.getTitle());
        artistLabel.setText(track.getAuthor());
        loadCoverImage(track.getCoverImagePath());
        root.setFocusTraversable(false);
    }

    private void loadCoverImage(String coverImagePath) {
        try {
            if (coverImagePath != null && !coverImagePath.isEmpty()) {
                File file = new File(coverImagePath);
                if (file.exists()) {
                    coverImage.setImage(new Image(file.toURI().toString()));
                    return;
                }
            }
            coverImage.setImage(
                new Image(getClass().getResourceAsStream("/com/group10/images/covers/default-cover.png"))
            );
        } catch (Exception e) {
            System.err.println("Errore nel caricamento della cover: " + e.getMessage());
        }
    }
    
    @Override
    public Parent getRoot() {
        return root;
    }
    
    @FXML
    private void handleSelection(MouseEvent event) {
        MainViewController.getInstance().setSelectedTrack(track);
        MainViewController.getInstance().update();

        PlaybackEngine engine = PlaybackEngine.getInstance();

        if (contextPlaylist != null) {
            engine.addListToQueue(new ArrayList<>(contextPlaylist.getTracks()));
            engine.setCurrentPlaylist(contextPlaylist);
        } else {
            engine.addListToQueue(new ArrayList<>(MusicCatalogue.getInstance().getTracks()));
            engine.setCurrentPlaylist(null);
        }

        engine.setCurrentTrack(track);
    }
}