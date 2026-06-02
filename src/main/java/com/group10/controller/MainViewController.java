package com.group10.controller;

import com.group10.controller.common.AbstractUIComponentCard;
import com.group10.controller.factory.TrackUIComponentFactory;
import com.group10.controller.factory.PlaylistUIComponentFactory;
import com.group10.controller.track.TrackUIAdderController;
import com.group10.controller.playlist.PlaylistUIAdderController;
import com.group10.controller.playlist.PlaylistUIComponentItem;
import com.group10.controller.track.TrackUIComponentCard;
import com.group10.controller.track.TrackUIComponentItem;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.state.PlaybackEngine;
import com.group10.model.common.Subscriber; // IMPORTANTE: Importiamo il Subscriber!

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author group10
 * * Singleton
 */
// AGGIUNTO: "implements Subscriber" per poter ascoltare il Catalogo
public class MainViewController implements Initializable, Subscriber { 

    @FXML private TextField searchField;
    @FXML private VBox leftPane;
    @FXML private HBox bottomPane;
    @FXML private VBox centerPane;
    @FXML private VBox rightPane;
    @FXML private Button playlistCreationButton;
    @FXML private Button addTrackButton;
    @FXML private StackPane root;
    @FXML private javafx.scene.control.Button playPauseButton;
    
    private static MainViewController singleton;

    public static MainViewController getInstance() {
        if (singleton == null) {
            singleton = new MainViewController();
        }
        return singleton;
    }

    public Parent getRoot() { return root; }
    public ObservableList<Node> getRootChildren() { return root.getChildren(); }
    public Parent getLeftPane() { return leftPane; }
    public Parent getRightPane() { return rightPane; }
    public Parent getCenterPane() { return centerPane; }
    public Parent getBottomPane() { return bottomPane; }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Diciamo al Catalogo: "Avvisami quando crei una nuova playlist o traccia!"
        MusicCatalogue.getInstance().addSubscriber(this);
        leftPane.setSpacing(1);
        // 2. Carichiamo la grafica la primissima volta
        update(); 
        
        // CARICAMENTO NEL BOTTOM PANE (Player)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group10/view/PlayerView.fxml"));
        PlayerViewController controller = new PlayerViewController();
        loader.setController(controller);
        try {
            Parent playerView = loader.load();
            bottomPane.getChildren().add(playerView);
        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento della PlayerView", e);
        }
    }

    // ==========================================================
    // IL CUORE DELLA MAGIA (PATTERN OBSERVER)
    // ==========================================================
    @Override
    public void update() {
        // 1. Svuotiamo i pannelli per non raddoppiare le cose già presenti
        leftPane.getChildren().clear();
        centerPane.getChildren().clear();

        // 2. Ricarichiamo le Playlist (nel Left Pane) usando la TUA Factory!
        for(PlaylistComponent p: MusicCatalogue.getInstance().getPlaylists()) {
            PlaylistUIComponentItem item = (PlaylistUIComponentItem) new PlaylistUIComponentFactory().createUIComponentItem(p);
            try {
                leftPane.getChildren().add(item.getRoot());
            } catch (Exception ex) {
                System.out.println(ex.getCause());
            }
        }

        // 3. Ricarichiamo le Track (nel Center Pane) usando la TUA Factory!
        for(TrackComponent p: MusicCatalogue.getInstance().getTracks()) {
            TrackUIComponentItem item = (TrackUIComponentItem) new TrackUIComponentFactory().createUIComponentItem(p);
            try {
                centerPane.getChildren().add(item.getRoot());
            } catch (Exception ex) {
                System.out.println(ex.getCause());
            }
        }
    }

    // ==========================================================
    // GESTIONE DEI POPUP E DEI TASTI
    // ==========================================================
    
    @FXML
    private void handleAddTrack(ActionEvent event) {
        try {
            TrackUIAdderController c = (TrackUIAdderController) new TrackUIComponentFactory().createUIComponentAdder();
            Parent trackAdderView = c.getRoot();
            showCustomPopup(trackAdderView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePlaylistCreation(ActionEvent event) {
        try {
            PlaylistUIAdderController c = (PlaylistUIAdderController) new PlaylistUIComponentFactory().createUIComponentAdder();
            Parent playlistAdderView = c.getRoot();
            showCustomPopup(playlistAdderView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showCustomPopup(Parent popup) {
        root.getChildren().get(0).setEffect(new GaussianBlur(10));
        
        root.getChildren().removeIf(child -> child != root.getChildren().get(0));
        
        StackPane layer = new StackPane();
        Pane pane = new Pane();
        pane.setEffect(new GaussianBlur(10));
        
        pane.setOnMouseClicked(e -> {
            // Chiude il popup cliccando fuori
            if (root.getChildren().size()>1) {
                root.getChildren().remove(root.getChildren().size()-1);
                root.getChildren().get(0).setEffect(null);
            }
        });
        
        layer.getChildren().add(pane);
        layer.getChildren().add(popup);
        root.getChildren().add(layer);
    }

    @FXML
    private void handleUndo(ActionEvent event) {}

    @FXML
    public void handlePlayPause(javafx.event.ActionEvent event) {
        PlaybackEngine engine = PlaybackEngine.getInstance();
        if (engine.getCurrentTrack() == null) {
            System.out.println("⚠️ La coda è vuota, aggiungi prima un brano!");
            return; 
        }
        if (engine.getState() instanceof com.group10.model.state.PlayingState) {
            engine.pause();
            playPauseButton.setText("▶️"); 
        } else {
            engine.play();
            playPauseButton.setText("⏸️"); 
        }
    }

    @FXML
    public void handleNext(javafx.event.ActionEvent event) {
        PlaybackEngine.getInstance().next();
    }

    @FXML
    public void handlePrevious(javafx.event.ActionEvent event) {
        PlaybackEngine.getInstance().previous();
    }
}
