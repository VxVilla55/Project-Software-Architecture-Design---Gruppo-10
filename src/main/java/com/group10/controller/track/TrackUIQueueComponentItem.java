package com.group10.controller.track;

import com.group10.controller.MainViewController;
import com.group10.controller.common.AbstractUIComponent;
import com.group10.service.factory.TrackUIComponentFactory;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.common.Playable;
import com.group10.model.TrackComponent;
import com.group10.model.state.PlaybackEngine;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class TrackUIQueueComponentItem implements AbstractUIComponent, Initializable {
    @FXML private HBox root;
    @FXML private Label indexLabel;
    @FXML private Label titleLabel;
    @FXML private Label artistLabel;
    @FXML private Button trackMenuButton;
    @FXML private ImageView coverImage;
    
    
    private TrackComponent track;
    private PlaylistComponent contextPlaylist = null;
    
    public void setContextPlaylist(PlaylistComponent playlist) {
        this.contextPlaylist = playlist;
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
        indexLabel.setText("-");
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
    private void handleOptions(ActionEvent event) {
        TrackUIOptionsController c = (TrackUIOptionsController) new TrackUIComponentFactory().createUIComponentOptions(track);
        c.setContextPlaylist(this.contextPlaylist);
        MainViewController.getInstance().showMenuPopup(trackMenuButton, c.getRoot()); 
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
    
    private void showOptionPopup(Parent popup) {        
        root.getChildren().removeIf( child -> child != root.getChildren().get(0));
        StackPane layer = new StackPane();
        Pane background = new Pane();        
        background.setOnMouseClicked(e -> {
            if (root.getChildren().size()>1) {
                root.getChildren().remove(root.getChildren().size()-1);
                root.getChildren().get(0).setEffect(null);
            }
        });
        Bounds buttonBounds = trackMenuButton.getBoundsInLocal();
        double x = buttonBounds.getMinX() + trackMenuButton.getWidth();
        double y = buttonBounds.getMinY();
        
        layer.getChildren().addAll(popup, background);
        root.getChildren().add(layer);
    }
}