/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.group10.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
 */
public class MainViewController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private ScrollPane leftPane;
    @FXML
    private HBox bottomPane;
    @FXML
    private VBox centerPane;
    @FXML
    private ScrollPane rightPane;
    @FXML
    private Button playlistCreationButton;
    @FXML
    private Button addTrackButton;
    @FXML
    private StackPane root;
    
    
    private final String viewPath  = "/com/group10/view/MainView.fxml";
    private Parent view = null;
    
    public MainViewController() {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(viewPath)
        );
        
        //loader.setRoot(this);
        loader.setController(this);
        
        try {
            //carica effettivamente la grafica FXML
            view = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Path della view errato: " + viewPath);
        }
    }

    public Parent getView() {
        return view;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
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
}
