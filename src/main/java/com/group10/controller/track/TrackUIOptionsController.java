/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.group10.controller.track;

import com.group10.controller.common.AbstractUIOptionsComponent;
import com.group10.controller.factory.TrackUIComponentFactory;
import com.group10.model.common.Playable;
import com.group10.model.TrackComponent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.layout.VBox;
/**
 * FXML Controller class
 *
 * @author group10
 */
public class TrackUIOptionsController extends AbstractUIOptionsComponent {

    @FXML
    private VBox root;
    
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
        // TODO
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
        
        //MainTableController.getInstance().getLefPane().getChildren().add(trackDetailsView);
    }

    @FXML
    private void handleAddToQueue(ActionEvent event) {
        //MusicCatalogue.getInstance().addToQueue(track);
    }

    @FXML
    private void handlePlayAsNext(ActionEvent event) {
        //MusicCatalogue.getInstance().addAsNext(track);
    }

    @FXML
    private void handleAddToPlaylist(ActionEvent event) {
        //dobbiamo fargli visualizzare le playlist da spuntare (con una checkbox)
        //Istanzio il controllore che carica la view
        //x c = (x) new PlyalistUIComponentFactory().createUICataloguePlaylist(track);
        //prendo dalla view il nodo Parent da collocare
        //Parent view = c.getRoot();

        //showCustomPopup(view);
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
}
