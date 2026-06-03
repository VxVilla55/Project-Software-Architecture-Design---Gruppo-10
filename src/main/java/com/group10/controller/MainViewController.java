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
import javafx.geometry.Point2D;
import javafx.scene.Node;
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
    private Popup activePopup = null;

    public static MainViewController getInstance() {
        if (singleton == null) {
            singleton = new MainViewController();
        }
        return singleton;
    }
    

    //metodi per controllare l'interfaccia
    //public Parent getRoot() { return root; }
    //public ObservableList<Node> getRootChildren() { return root.getChildren(); }
    
    public void showOnRightPane(Parent pane) {
        rightPane.getChildren().removeIf(child -> child != root.getChildren().get(0));
        rightPane.getChildren().get(0).setEffect(null);
    }
    public void showOnCenterPane(Parent pane) {
        System.out.println(centerPane.getChildren());
        //rimuovo tutti gli elementi
        centerPane.getChildren().removeAll();
        //aggiungo il pannello richiesto
        centerPane.getChildren().add(pane);
        System.out.println(centerPane.getChildren());
    }
    public void showOnLeftPane(Parent pane) {
        //rimuovo tutti gli elementi
        leftPane.getChildren().removeAll();
        //aggiungo il pannello richiesto
        leftPane.getChildren().add(pane);
    }
    
    public void showOnBottomPane(Parent pane) {
        //rimuovo tutti gli elementi
        bottomPane.getChildren().removeAll();
        //aggiungo il pannello richiesto
        bottomPane.getChildren().add(pane);
    }
    
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

    // metodo previsto dal pattern Observer
    @Override
    public void update() {
        // 1. Svuotiamo i pannelli per non raddoppiare le cose già presenti
        leftPane.getChildren().clear();
        centerPane.getChildren().clear();

        // 2. Ricarichiamo le Playlist (nel Left Pane) usando la TUA Factory!
        for(PlaylistComponent p: MusicCatalogue.getInstance().getPlaylists().values()) {
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
    // Aggiungi questo metodo sotto il handlePlaylistCreation
    @FXML
    public void handleAnnulla(ActionEvent event) {
        // Se c'è un popup (oltre allo sfondo principale), rimuovilo
        if (root.getChildren().size() > 1) {
            root.getChildren().remove(root.getChildren().size() - 1);
            root.getChildren().get(0).setEffect(null); // Togli l'effetto sfocatura
        }
    }

    @FXML
    private void handleUndo(ActionEvent event) {}

    /*FXML
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
    }*/

    public void closePopup() {
        root.getChildren().get(0).setEffect(new GaussianBlur(10));
        root.getChildren().removeIf(child -> child != root.getChildren().get(0));
        root.getChildren().get(0).setEffect(null);
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


    public StackPane getRoot() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoot'");
    }
}
