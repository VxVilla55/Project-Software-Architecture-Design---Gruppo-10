/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.group10.controller;

/**
 * FXML Controller class
 *
 * @author group10
 * 
 * è il ConcreteProduct, rappresenta il Controller dell'Item.fxml che mostra i dettagli della traccia
 */
 
import com.group10.model.Playable;
import com.group10.model.TrackBuilder;
import com.group10.model.TrackComponent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class TrackUIComponentItem extends AbstractUIComponentItem{
    
    @FXML
    private AnchorPane root;
    @FXML
    private Label itemPlace1;
    @FXML
    private Label itemPlace2;
    @FXML
    private Label itemPlace3;
    
    private TrackComponent track;
    
    public TrackUIComponentItem(TrackComponent track) {
        this.track = track;
    }
    
    public TrackUIComponentItem(Playable t) {
        if (!(t instanceof TrackComponent)) {
            throw new RuntimeException("Impossibile crearne card.");
        }
        else {
            track = (TrackComponent) t;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        itemPlace1.setText(track.getTitle());
        itemPlace2.setText(track.getGenre());
        itemPlace3.setText(String.valueOf(track.getYear()));
    }
    
    @Override
    public Parent getRoot() {
        return root;
    }
    
    @FXML
    private void handleOptions(ActionEvent event) {
        //Istanzio il controllore che carica la view
        TrackUIOptionsController c = (TrackUIOptionsController) new TrackUIComponentFactory().createUIComponentOptions(track);
        //prendo dalla view il nodo Parent da collocare
        Parent trackDetailsView = c.getRoot();
    }
}
