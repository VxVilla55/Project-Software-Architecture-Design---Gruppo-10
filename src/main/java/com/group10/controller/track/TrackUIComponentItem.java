/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.group10.controller.track;

/**
 * FXML Controller class
 *
 * @author group10
 * * è il ConcreteProduct, rappresenta il Controller dell'Item.fxml che mostra i dettagli della traccia
 */
 
import com.group10.controller.MainViewController;
import com.group10.controller.common.AbstractUIComponentItem;
import com.group10.controller.factory.TrackUIComponentFactory;
import com.group10.model.common.Playable;
import com.group10.model.TrackComponent;

// AGGIUNTO: Import del motore di riproduzione
import com.group10.model.state.PlaybackEngine;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class TrackUIComponentItem extends AbstractUIComponentItem{
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
        durationLabel.setText(String.valueOf(track.getDurationInSeconds()));
        root.setFocusTraversable(false);
    }
    
    @Override
    public Parent getRoot() {
        return root;
    }
    
    @FXML
    private void handleOptions(ActionEvent event) {
        System.out.println("OPTIONS");
        //Istanzio il controllore che carica la view
        TrackUIOptionsController c = (TrackUIOptionsController) new TrackUIComponentFactory().createUIComponentOptions(track);
        //prendo dalla view il nodo Parent da collocare
        MainViewController.getInstance().showMenuPopup(trackMenuButton, c.getRoot()); 
        
    }
    
    @FXML
    private void handleSelection(MouseEvent event) {
        // 1. Logica originale: mostra i dettagli a destra
        MainViewController.getInstance().setSelectedTrack(track);
        MainViewController.getInstance().update();
        
        // 2. NUOVA LOGICA: Passa la traccia al motore e avvia la riproduzione!
        PlaybackEngine.getInstance().setCurrentTrack(track);
        PlaybackEngine.getInstance().play();
    }
    
    private void showOptionPopup(Parent popup) {        
        //rimuovo altri popup
        root.getChildren().removeIf( child -> child != root.getChildren().get(0));
        
        //carico il popup
        StackPane layer = new StackPane();
        
        Pane background = new Pane();        
        background.setOnMouseClicked(e -> {
            if (root.getChildren().size()>1) {
                root.getChildren().remove(root.getChildren().size()-1);
                root.getChildren().get(0).setEffect(null);
            }
        });
        Bounds buttonBounds = trackMenuButton.getBoundsInLocal();
        //appena a destra del pulsante
        double x = buttonBounds.getMinX() + trackMenuButton.getWidth();
        // allineato in alto
        double y = buttonBounds.getMinY();
        
        layer.getChildren().addAll(popup, background);

        root.getChildren().add(layer);
    }
}