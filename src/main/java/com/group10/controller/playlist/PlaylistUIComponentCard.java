/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.group10.controller.playlist;

/**
 * FXML Controller class
 *
 * @author group10
 * 
 * è il ConcreteProduct, rappresenta il Controller dell'Item.fxml che mostra i dettagli della traccia
 */
 
import com.group10.controller.common.AbstractUIComponentCard;
import com.group10.model.common.Playable;
import com.group10.model.PlaylistComponent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class PlaylistUIComponentCard extends AbstractUIComponentCard{
    
    @FXML
    private AnchorPane root;
    @FXML
    private Label itemPlace1;
    @FXML
    private Label itemPlace2;
    @FXML
    private ImageView imageView;
    
    private PlaylistComponent playlist;
        
    public PlaylistUIComponentCard(Playable playlist) {
        if (!(playlist instanceof PlaylistComponent)) {
            throw new RuntimeException("Impossibile crearne card.");
        }
        else {
            this.playlist = (PlaylistComponent) playlist;
        }
    }
    
    public PlaylistUIComponentCard(PlaylistComponent playlist) {
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
}
