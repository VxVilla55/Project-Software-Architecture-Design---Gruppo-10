package com.group10.controller.track;

/**
 * FXML Controller class
 *
 * @author group10
 * * è il ConcreteProduct, rappresenta il Controller dell'Item.fxml che mostra i dettagli della traccia
 */
 
import com.group10.controller.MainViewController;
import com.group10.controller.common.AbstractUIComponent;
import com.group10.service.factory.TrackUIComponentFactory;
import com.group10.model.common.Playable;
import com.group10.model.TrackComponent;

import com.group10.model.state.PlaybackEngine; 

import java.net.URL;
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

public class TrackUIComponentCard implements AbstractUIComponent, Initializable {
    
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
        MainViewController.getInstance().setSelectedTrack(track);
        MainViewController.getInstance().update();
        
        PlaybackEngine.getInstance().setCurrentTrack(track);
        PlaybackEngine.getInstance().play();
    }
    
    @FXML
    private void handleOptions(ActionEvent event) {
        //System.out.println("OPTIONS");
        TrackUIOptionsController c = (TrackUIOptionsController) new TrackUIComponentFactory().createUIComponentOptions(track);
        MainViewController.getInstance().showMenuPopup(menuButton, c.getRoot()); 
        
    }
}
