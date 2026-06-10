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
 
import com.group10.controller.MainViewController;
import com.group10.controller.common.AbstractUIComponent;
import com.group10.model.common.Playable;
import com.group10.model.PlaylistComponent;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class PlaylistUIComponentCard implements AbstractUIComponent, Initializable {
    
    @FXML
    private AnchorPane root;
    @FXML
    private Label itemPlace1;
    @FXML
    private Label itemPlace2;
    @FXML
    private ImageView imageView;
    @FXML
    private Button menuButton;
    
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
        
        itemPlace1.setText(playlist.getName());
        
        Duration durataPlaylist = Duration.ofSeconds(playlist.getDurationInSeconds());
        String durataFormattata = String.format("(%02d:%02d:%02d)", durataPlaylist.toHoursPart(), durataPlaylist.toMinutesPart(), durataPlaylist.toSecondsPart());
        itemPlace2.setText(String.valueOf(playlist.getSize())+" tracce "+durataFormattata);
    }
    
    @Override
    public Parent getRoot() {
        return root;
    }
    
    @FXML
    private void handleSelection(MouseEvent event) {
        MainViewController.getInstance().setSelectedPlaylist(playlist);
        MainViewController.getInstance().update();
    }
    
    @FXML
    private void handleOptions(ActionEvent event) {
        System.out.println("Operazioni su playlist non ancora definite");
        
    }
}
