/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.controller;

import com.group10.model.Playable;
import com.group10.model.PlaylistComponent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author group10
 */
public class PlaylistUIComponentItem extends AbstractUIComponentItem {
    
    @FXML
    private AnchorPane root;
    @FXML
    private Label itemPlace1;
    @FXML
    private Label itemPlace2;
    @FXML
    private Label itemPlace3;
    
    private PlaylistComponent playlist;

    public PlaylistUIComponentItem(Playable model) {
        if (!(playlist instanceof PlaylistComponent)) {
            throw new RuntimeException("Impossibile crearne l'item");
        }
        else {
            this.playlist = (PlaylistComponent) playlist;
        }
    }
    
    public PlaylistUIComponentItem (PlaylistComponent playlist) {
        this.playlist = (PlaylistComponent) playlist;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //imageView.setImage(track.getImagePath());
        itemPlace1.setText(playlist.getName());
        itemPlace2.setText(String.valueOf(playlist.getDurationInSeconds()));
    }
    
    @Override
    public Parent getRoot() {
        return root;
    }
    
    @FXML
    private void handleOptions(ActionEvent event) {
        //Istanzio il controllore che carica la view
        //PlaylistUIOptionsController c = (PlaylistUIOptionsController) new PlaylistUIComponentFactory().createUIComponentOptions(playlist);
        //prendo dalla view il nodo Parent da collocare
        //Parent trackDetailsView = c.getRoot();
        //mostra la view
    }
}
