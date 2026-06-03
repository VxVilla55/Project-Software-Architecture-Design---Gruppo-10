package com.group10.controller;

import com.group10.controller.factory.TrackUIComponentFactory;
import com.group10.controller.factory.PlaylistUIComponentFactory;
import com.group10.controller.track.TrackUIAdderController;
import com.group10.controller.playlist.PlaylistUIAdderController;
import com.group10.controller.playlist.PlaylistUIComponentItem;
import com.group10.controller.playlist.PlaylistUIDetailsController;
import com.group10.controller.track.TrackUIDetailsController;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.common.Subscriber;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

/**
 * FXML Controller class
 *
 * @author group10
 * * Singleton
 */
public class MainViewController implements Initializable, Subscriber { 

    @FXML
    private TextField searchField;
    
    @FXML
    private VBox leftPane;
    
    @FXML
    private HBox bottomPane;
    
    @FXML
    private VBox centerPane;
    
    @FXML
    private VBox rightPane;
    
    @FXML
    private Button playlistCreationButton;
    
    @FXML
    private Button addTrackButton;
    
    @FXML
    private StackPane root;
    
    @FXML
    private Button playPauseButton;
    
    @FXML
    private Button homeButton;
    
    
    private static MainViewController singleton;
    private Popup activePopup = null;
    private PlaylistComponent selectedPlaylist;
    private TrackComponent selectedTrack;

    public static MainViewController getInstance() {
        if (singleton == null) {
            singleton = new MainViewController();
        }
        return singleton;
    }
    
    public void showOnRightPane(Parent pane) {
        rightPane.getChildren().clear();
        rightPane.getChildren().add(pane);
    }
    public void showOnCenterPane(Parent pane) {
        centerPane.getChildren().clear();
        centerPane.getChildren().add(pane);
    }
    public void showOnLeftPane(Parent pane) {
        leftPane.getChildren().clear();
        leftPane.getChildren().add(pane);
    }
    
    public void showOnBottomPane(Parent pane) {
        bottomPane.getChildren().clear();
        bottomPane.getChildren().add(pane);
    }
    
    public void setSelectedPlaylist(PlaylistComponent playlist) {
        selectedPlaylist = playlist;
    }
    public void setSelectedTrack(TrackComponent track) {
        selectedTrack = track;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Diciamo al Catalogo: "Avvisami quando crei una nuova playlist o traccia!"
        MusicCatalogue.getInstance().addSubscriber(this);
        //Carichiamo per una prima volta i componenti della schermata
        update();
    }

    @Override
    public void update() {
        FXMLLoader loader;
        //RELOAD DEL CONTENUTO DEL LEFT PANE
        //azzero il contenuto
        leftPane.getChildren().clear();
        //carico
        for(PlaylistComponent p: MusicCatalogue.getInstance().getPlaylists().values()) {
            PlaylistUIComponentItem item = (PlaylistUIComponentItem) new PlaylistUIComponentFactory().createUIComponentItem(p);
            try {
                leftPane.getChildren().add(item.getRoot());
            } catch (Exception ex) {
                System.out.println(ex.getCause());
            }
        }
        
        //RELOAD DEL CONTENUTO DEL RIGHT PANE
        //azzero il contenuto
        //rightPane.getChildren().clear();
        //carico
        if(selectedTrack != null) {
            //MOSTRO IL BRANO SELEZIONATO
            try {
                TrackUIDetailsController c = (TrackUIDetailsController) new TrackUIComponentFactory().createUIComponentDetails(selectedTrack);
                Parent trackView = c.getRoot();
                showOnRightPane(trackView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //MOSTRO IL PANNELLO DI DESTRA VUOTO
            rightPane.getChildren().clear();
        }

        //RELOAD NEL CENTER PANE (Playlist selezionata altrimenti Home)
        //azzero il contenuto
        //centerPane.getChildren().clear();
        //carico
        if(selectedPlaylist == null) {
            //MOSTRO LA HOME
            loader = new FXMLLoader(getClass().getResource("/com/group10/view/HomepageView.fxml"));
            try {
                Parent homeView = loader.load();
                showOnCenterPane(homeView); //contiene una clear() del center pane
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            //MOSTRO LA PLAYLIST
            try {
                PlaylistUIDetailsController c = (PlaylistUIDetailsController) new PlaylistUIComponentFactory().createUIComponentDetails(selectedPlaylist);
                Parent playlistView = c.getRoot();
                showOnCenterPane(playlistView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        //RELOAD NEL BOTTOM PANE (Player)
        //azzero il contenuto
        bottomPane.getChildren().clear();
        //carico
        loader = new FXMLLoader(getClass().getResource("/com/group10/view/PlayerView.fxml"));
        PlayerViewController controller = new PlayerViewController();
        loader.setController(controller);
        try {
            Parent playerView = loader.load();
            showOnBottomPane(playerView);
        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento della PlayerView", e);
        }
    }
    
    public void showPopup(Parent popup) {
        //chiude il popup se già presente
        closePopup();
        
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
    public void closePopup() {
        root.getChildren().get(0).setEffect(new GaussianBlur(10));
        root.getChildren().removeIf(child -> child != root.getChildren().get(0));
        root.getChildren().get(0).setEffect(null);
    }
    
    public void showMenuPopup(Button source, Parent content) {
        // Se esiste già un popup attivo, chiudiamolo prima di aprirne uno nuovo
        if (activePopup != null && activePopup.isShowing()) {
            activePopup.hide();
            activePopup = null;
        }
        //creo quello che deve contenere il 'content'
        Popup popup = new Popup();
        popup.getContent().add(content);
        popup.setAutoHide(true);

        activePopup = popup;
        
        //ottieni coordinate dello schermo
        Point2D screenPoint = source.localToScreen(0, source.getHeight());
        popup.show(source, screenPoint.getX(), screenPoint.getY());
    }
    public void hideMenuPopup() {
        if (activePopup != null && activePopup.isShowing()) {
            activePopup.hide();
            activePopup = null;
        }
    }
    
    //gestione degli eventi sugli elementi della view    
    @FXML
    private void handleAddTrack(ActionEvent event) {
        try {
            TrackUIAdderController c = (TrackUIAdderController) new TrackUIComponentFactory().createUIComponentAdder();
            Parent trackAdderView = c.getRoot();
            MainViewController.getInstance().showPopup(trackAdderView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePlaylistCreation(ActionEvent event) {
        try {
            PlaylistUIAdderController c = (PlaylistUIAdderController) new PlaylistUIComponentFactory().createUIComponentAdder();
            Parent playlistAdderView = c.getRoot();
            MainViewController.getInstance().showPopup(playlistAdderView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    @FXML
    private void handleUndo(ActionEvent event) {
        System.out.println("Ancora da implementare");
    }
    
    @FXML
    private void handleHome(ActionEvent event) {
        selectedPlaylist = null;
        update();
    }
}
