package com.group10.controller.track;

/**
 * FXML Controller class
 *
 * @author group10
 * * è il ConcreteProduct, rappresenta il Controller dell'Item.fxml che mostra i dettagli della traccia
 */
 
import com.group10.controller.MainViewController;
import com.group10.controller.common.AbstractUIComponentCard;
import com.group10.model.common.Playable;
import com.group10.model.TrackComponent;

// AGGIUNTO: L'import per il motore di riproduzione
import com.group10.model.state.PlaybackEngine; 

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class TrackUIComponentCard extends AbstractUIComponentCard{
    
    @FXML
    private AnchorPane root;
    @FXML
    private Label itemPlace1;
    @FXML
    private Label itemPlace2;
    @FXML
    private ImageView imageView;
    private TrackComponent track;
        
    
    public TrackUIComponentCard(TrackComponent track) {
        this.track = track;
    }
    
    public TrackUIComponentCard(Playable t) {
        if (!(t instanceof TrackComponent)) {
            throw new RuntimeException("Impossibile crearne card.");
        }
        else {
            track = (TrackComponent) t;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //imageView.setImage(track.getImagePath());
        itemPlace1.setText(track.getTitle());
        itemPlace2.setText(track.getAuthor());
    }
    
    @Override
    public Parent getRoot() {
        return root;
    }
    
    @FXML
    private void handleSelection(MouseEvent event) {
        // 1. Mostra i dettagli a destra (la tua logica originale)
        MainViewController.getInstance().setSelectedTrack(track);
        MainViewController.getInstance().update();
        
        // 2. NUOVA LOGICA: Imposta la traccia nel motore e fai partire il Play!
        PlaybackEngine.getInstance().setCurrentTrack(track);
        PlaybackEngine.getInstance().play();
    }    
}
