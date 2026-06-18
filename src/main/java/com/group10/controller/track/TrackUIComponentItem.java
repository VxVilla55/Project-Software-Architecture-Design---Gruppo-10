package com.group10.controller.track;

import com.group10.controller.MainViewController;
import com.group10.controller.common.AbstractUIComponent;
import com.group10.service.command.CommandManager;
import com.group10.service.command.ReorderTrackCommand;
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
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class TrackUIComponentItem implements AbstractUIComponent, Initializable {
    @FXML private HBox root;
    @FXML private Label indexLabel;
    @FXML private Label titleLabel;
    @FXML private Label artistLabel;
    @FXML private Label genreLabel;
    @FXML private Label yearLabel;
    @FXML private Label durationLabel;
    @FXML private Button trackMenuButton;
    
    @FXML private Label favouriteLabel;
    @FXML private Label newReleaseLabel;
    @FXML private Label explicitLabel;
    @FXML private ImageView coverImage;
    
    private TrackComponent track;
    private PlaylistComponent contextPlaylist = null;
    private Integer position;
    
    public void setContextPlaylist(PlaylistComponent playlist) {
        this.contextPlaylist = playlist;
    }
    
    public TrackUIComponentItem(TrackComponent track) {
        this.track = track;
    }
    
    public TrackUIComponentItem(Playable t) {
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
        genreLabel.setText(track.getGenre());
        yearLabel.setText(String.valueOf(track.getYear()));
        if (position == null) {
            indexLabel.setText("-");
        }
        else {
            indexLabel.setText(String.valueOf(position+1));
        }
        loadCoverImage(track.getCoverImagePath());
        
        Duration trackDuration = Duration.ofSeconds(track.getDurationInSeconds());
        String formattedDuration = String.format("%02d:%02d:%02d", trackDuration.toHoursPart(), trackDuration.toMinutesPart(), trackDuration.toSecondsPart());
        durationLabel.setText(formattedDuration);
        root.setFocusTraversable(false);

        if (track.hasTag(TrackComponent.Tag.FAVORITE)) {
            favouriteLabel.setVisible(true);
            favouriteLabel.setManaged(true);
        }
        if (track.hasTag(TrackComponent.Tag.NEW_RELEASE)) {
            newReleaseLabel.setVisible(true);
            newReleaseLabel.setManaged(true);
        }
        if (track.hasTag(TrackComponent.Tag.EXPLICIT)) {
            explicitLabel.setVisible(true);
            explicitLabel.setManaged(true);
        }
    }
    
    @Override
    public Parent getRoot() {
        return root;
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

    @FXML
    private void handleOptions(ActionEvent event) {
        System.out.println("OPTIONS");
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

    public void setIndexInContainer(Integer position) {
        this.position = position;
        indexLabel.setText(String.valueOf(position+1));
        setupDragAndDrop();
    }

    private void setupDragAndDrop() {
        //QUANDO PARTE IL DRAG
        root.setOnDragDetected(event -> {
            Dragboard db = root.startDragAndDrop(TransferMode.MOVE);
            //degli appunti per salvare la posizione corrente all'inizio della trascinata
            ClipboardContent content = new ClipboardContent();
            content.putString(Integer.toString(position));
            db.setContent(content);
            event.consume();
        });

        //QUANDO QUALCOSA PASSA SOPRA
        root.setOnDragOver(event -> {
            if (event.getGestureSource() != root && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        //QUANDO IL MOUSE VIENE RILASCIATO QUI
        root.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            
            if (db.hasString() && contextPlaylist != null) {
                //recupera dalla Dragboard la posizione vecchia
                int fromIndex = Integer.parseInt(db.getString());
                //raccoglie il nuovo indice
                int toIndex = position; 
                //lancio comando di riordino
                CommandManager.getInstance().executeCommand(new ReorderTrackCommand(contextPlaylist, fromIndex, toIndex));
                
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
}