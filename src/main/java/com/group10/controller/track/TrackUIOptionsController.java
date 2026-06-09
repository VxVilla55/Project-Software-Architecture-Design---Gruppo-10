/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.group10.controller.track;

import com.group10.controller.MainViewController;
import com.group10.controller.common.AbstractUIDetailsController;
import com.group10.controller.common.AbstractUIOptionsComponent;
import com.group10.controller.factory.TrackUIComponentFactory;
import com.group10.model.common.Playable;
import com.group10.model.MusicCatalogue;
import com.group10.model.TrackComponent;
import com.group10.model.state.PlaybackEngine;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

/**
 * FXML Controller class
 *
 * @author group10
 */
public class TrackUIOptionsController extends AbstractUIOptionsComponent {
    @FXML
    private VBox root;
    // --- INIZIO DELLE TUE MODIFICHE ---
    @FXML
    private javafx.scene.control.Button removeTrackButton;
    private com.group10.model.PlaylistComponent contextPlaylist = null;
    // --- FINE DELLE TUE MODIFICHE ---
    
    private final TrackComponent track;
    
    public TrackUIOptionsController(TrackComponent track) {
        this.track = track;
    }    
    
    public TrackUIOptionsController(Playable t) {
        if (!(t instanceof TrackComponent)) {
            throw new RuntimeException("Impossibile crearne card.");
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
        
    }


    @Override
    public Parent getRoot() {
        return root;
    }
    
    @FXML
    private void handleViewDetails(ActionEvent event) {
        //Istanzio il controllore che carica la view
        TrackUIDetailsController c = (TrackUIDetailsController) new TrackUIComponentFactory().createUIComponentDetails(track);
        //prendo dalla view il nodo Parent da collocare
        Parent trackDetailsView = c.getRoot();
                
        //cancella popup
        MainViewController.getInstance().hideMenuPopup();
        
        //VBox rightPane = (VBox) MainViewController.getInstance().getRightPane();
        //rightPane.getChildren().add(trackDetailsView);
        MainViewController.getInstance().showOnRightPane(trackDetailsView);
    }

    @FXML
    private void handleAddToQueue(ActionEvent event) {
        PlaybackEngine.getInstance().addTrackToQueue(track);
        //cancella popup
        MainViewController.getInstance().hideMenuPopup();
    }


    @FXML
    private void handlePlayAsNext(ActionEvent event) {
        PlaybackEngine.getInstance().addTrackAsNext(track);
        //cancella popup
        MainViewController.getInstance().hideMenuPopup();
    }

    @FXML
    private void handleAddToPlaylist(ActionEvent event) {
        //istanzio il loader sulla view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group10/view/AddToPlaylistView.fxml"));
        //istanzio il controller
        AddToPlaylistController controller = new AddToPlaylistController(track);
        //associo il controller alla view
        loader.setController(controller);
        try {
            Parent addToPlaylistView = loader.load();
            MainViewController.getInstance().showPopup(addToPlaylistView);
        } catch (IOException e) {
            throw new RuntimeException("Impossibile caricare AddToPlaylistView.fxml", e);
        }
        //cancella popup
        MainViewController.getInstance().hideMenuPopup();
    }
    // NUOVO: Metodo per dire al menu in quale playlist si trova
    public void setContextPlaylist(com.group10.model.PlaylistComponent playlist) {
        this.contextPlaylist = playlist;
        
        // Se c'è una playlist, cambiamo il testo del bottone!
        if (this.contextPlaylist != null && removeTrackButton != null) {
            removeTrackButton.setText("Rimuovi dalla playlist");
        }
    }

@FXML
    private void handleRemoveTrack(ActionEvent event) {
        if (contextPlaylist != null) {
            // COMPORTAMENTO 1: Siamo dentro una playlist
            contextPlaylist.remove(track);
            System.out.println("✅ Traccia '" + track.getTitle() + "' rimossa SOLO dalla playlist: " + contextPlaylist.getName());
            
        } else {
            // COMPORTAMENTO 2: Siamo nella libreria generale
            MusicCatalogue.getInstance().removeTrack(track);
            System.out.println("✅ Traccia '" + track.getTitle() + "' rimossa dal CATALOGO GENERALE (e a cascata dalle playlist).");
        }
        
        // Chiudiamo sempre il menu a tendina
        MainViewController.getInstance().hideMenuPopup();
        
        // ---> LA RIGA MAGICA <---
        // Diciamo alla schermata principale di ricaricare la grafica immediatamente!
        MainViewController.getInstance().update();
    }
}