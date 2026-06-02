/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.group10.controller;

import com.group10.controller.factory.TrackUIComponentFactory;
import com.group10.controller.factory.PlaylistUIComponentFactory;
import com.group10.controller.track.TrackUIAdderController;
import com.group10.controller.playlist.PlaylistUIAdderController;
import com.group10.controller.playlist.PlaylistUIComponentItem;
import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.state.PlaybackEngine;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
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
 * 
 * Singleton
 */
public class MainViewController implements Initializable {

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
    private javafx.scene.control.Button playPauseButton;

    
    
    private static MainViewController singleton;
    //metodo previsto dal pattern Singleton
    public static MainViewController getInstance() {
        if (singleton == null) {
            singleton = new MainViewController();
        }
        return singleton;
    }

    public Parent getRoot() {
        return root;
    }
    public Parent getLeftPane() {
        return leftPane;
    }
    public Parent getRightPane() {
        return rightPane;
    }
    public Parent getCenterPane() {
        return centerPane;
    }
    public Parent getBottomPane() {
        return bottomPane;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for(PlaylistComponent p: MusicCatalogue.getInstance().getPlaylists()) {
            PlaylistUIComponentItem item = (PlaylistUIComponentItem) new PlaylistUIComponentFactory().createUIComponentItem(p);
            try {
                leftPane.getChildren().add(item.getRoot());
            } catch (Exception ex) {
                System.out.println(ex.getCause());
            }
        }
    }

    @FXML
    private void handleUndo(ActionEvent event) {
    }

    @FXML
    private void handleAddTrack(ActionEvent event) {
        try {
            //Istanzio il controllore che carica la view
            TrackUIAdderController c = (TrackUIAdderController) new TrackUIComponentFactory().createUIComponentAdder();
            //prendo dalla view il nodo Parent da collocare
            Parent trackAdderView = c.getRoot();
            
            showCustomPopup(trackAdderView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePlaylistCreation(ActionEvent event) {
        try {
            //Istanzio il controllore che carica la view
            PlaylistUIAdderController c = (PlaylistUIAdderController) new PlaylistUIComponentFactory().createUIComponentAdder();
            //prendo dalla view il nodo Parent da collocare
            Parent playlistAdderView = c.getRoot();
            
            showCustomPopup(playlistAdderView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showCustomPopup(Parent popup) {
        //sfoco la schermata
        root.getChildren().get(0).setEffect(new GaussianBlur(10));
        
        //rinuovo altripopup
        root.getChildren().removeIf( child -> child != root.getChildren().get(0));
        
        //carico il popup
        StackPane layer = new StackPane();
        //layer.setEffect(new GaussianBlur(10));
        
        Pane pane = new Pane();
        pane.setEffect(new GaussianBlur(10));
        
        pane.setOnMouseClicked(e -> {
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
    public void handlePlayPause(javafx.event.ActionEvent event) {
        PlaybackEngine engine = PlaybackEngine.getInstance();
        
        // 1. CONTROLLO: Se non c'è nulla in coda, non fare nulla (o stampa un avviso)
        // (Nota: dovresti aggiungere un metodo getQueueSize() nel PlaybackEngine se non c'è già)
        if (engine.getCurrentTrack() == null) {
            System.out.println("⚠️ La coda è vuota, aggiungi prima un brano!");
            return; // Esce dal metodo e non cambia l'icona
        }

        // 2. Se c'è musica, gestiamo lo stato
        if (engine.getState() instanceof com.group10.model.state.PlayingState) {
            engine.pause();
            playPauseButton.setText("▶️"); // Torna su Play
        } else {
            engine.play();
            playPauseButton.setText("⏸️"); // Passa su Pausa
        }
    }

    @FXML
    public void handleNext(javafx.event.ActionEvent event) {
        com.group10.model.state.PlaybackEngine.getInstance().next();
    }

    @FXML
    public void handlePrevious(javafx.event.ActionEvent event) {
        com.group10.model.state.PlaybackEngine.getInstance().previous();
    }}
