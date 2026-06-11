package com.group10.controller.track;

/**
 * FXML Controller class
 *
 * @author group10
 * * è il ConcreteProduct, rappresenta il Controller dell'Item.fxml che mostra i dettagli della traccia
 */
 
import com.group10.controller.MainViewController;
import com.group10.controller.common.AbstractUIComponent;
import com.group10.controller.factory.TrackUIComponentFactory;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.common.Playable;
import com.group10.model.TrackComponent;

import com.group10.model.state.PlaybackEngine;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class TrackUIComponentItem implements AbstractUIComponent, Initializable {
    @FXML
    private HBox root;
    @FXML
    private Label indexLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label artistLabel;
    @FXML
    private Label genreLabel;
    @FXML
    private Label yearLabel;
    @FXML
    private Label durationLabel;
    @FXML
    private Button trackMenuButton;
    
    private TrackComponent track;
    
    // --- NUOVO: Variabile per ricordarsi in che playlist siamo ---
    private PlaylistComponent contextPlaylist = null;
    
    // --- NUOVO: Metodo per ricevere la playlist dal PlaylistUIDetailsController ---
    public void setContextPlaylist(com.group10.model.PlaylistComponent playlist) {
        this.contextPlaylist = playlist;
    }
    
    public TrackUIComponentItem(TrackComponent track) {
        this.track = track;
    }
    
    public TrackUIComponentItem(Playable t) {
        if (!(t instanceof TrackComponent)) {
            throw new RuntimeException("Impossibile crearne l'item.");
        }
        else {
            track = (TrackComponent) t;
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleLabel.setText(track.getTitle());
        artistLabel.setText(track.getAuthor());
        genreLabel.setText(track.getGenre());
        yearLabel.setText(String.valueOf(track.getYear()));
        
        Duration trackDuration = Duration.ofSeconds(track.getDurationInSeconds());
        String formattedDuration = String.format("%02d:%02d:%02d", trackDuration.toHoursPart(), trackDuration.toMinutesPart(), trackDuration.toSecondsPart());
        durationLabel.setText(formattedDuration);
        root.setFocusTraversable(false);
    }
    
    @Override
    public Parent getRoot() {
        return root;
    }
    
    @FXML
    private void handleOptions(ActionEvent event) {
        System.out.println("OPTIONS");
        TrackUIOptionsController c = (TrackUIOptionsController) new TrackUIComponentFactory().createUIComponentOptions(track);
        
        // --- NUOVO: Passiamo la playlist al controller delle opzioni prima di mostrarlo! ---
        c.setContextPlaylist(this.contextPlaylist);
        
        MainViewController.getInstance().showMenuPopup(trackMenuButton, c.getRoot()); 
    }
    
    @FXML
    private void handleSelection(MouseEvent event) {
        MainViewController.getInstance().setSelectedTrack(track);
        MainViewController.getInstance().update();

        PlaybackEngine engine = PlaybackEngine.getInstance();

        if (contextPlaylist != null) {
            // playlist: metto in coda l'intera playlist
            engine.addListToQueue(new ArrayList<>(contextPlaylist.getTracks()));
            engine.setCurrentPlaylist(contextPlaylist);
        } else {
            // home: pulisco la coda e metto in coda l'intera libreria
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