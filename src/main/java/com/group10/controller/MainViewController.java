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
    private StackPane stackPane;
    
    
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
            AddTrackController p = new AddTrackController();
            //prendo dalla view il nodo Parent da collocare
            Parent addTrackView = p.getView();
            
            showCustomPopup(addTrackView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePlaylistCreation(ActionEvent event) {
        try {
            //Istanzio il controllore che carica la view
            CreazionePlaylistController p = new CreazionePlaylistController();
            //prendo dalla view il nodo Parent da collocare
            Parent creazionePlaylistView = p.getView();
            
            showCustomPopup(creazionePlaylistView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showCustomPopup(Parent popup) {
        //sfoco la schermata
        stackPane.getChildren().get(0).setEffect(new GaussianBlur(10));
        
        //popup        
        stackPane.getChildren().add(popup);
        
        //nell'initialize c'è il listener che al click di stackPane.getChildren().get(0)
        //si toglie il blur e chiuso il popup.
        
        /*StackPane overlayPane = new StackPane();
        
        //pannello opaco
        Pane blurredPane = new Pane();
        //overlayPane.setVisible(false);
        

        overlayPane.getChildren().add(blurredPane);
        
        blurredPane.setOnMouseClicked(e -> {
            System.out.println("Size: " + stackPane.getChildren().size());
            System.out.println("Elementi dello stack pane: " + stackPane.getChildren());
            if (stackPane.getChildren().size()>1) {
                stackPane.getChildren().remove(stackPane.getChildren().size()-1);
                stackPane.getChildren().get(0).setEffect(null);
            }
        });
        
        
        //popup        
        overlayPane.getChildren().add(popup);
        
        //aggiungo l'overlay alla mainview
        stackPane.getChildren().add(overlayPane);
        // blur su rootStack (ma attento: sfoca anche overlayPane? No perché overlayPane è sopra)
        // meglio applicare blur solo al primo figlio di rootStack (es. il contenuto principale)
        stackPane.getChildren().get(0).setEffect(new GaussianBlur(10));*/
    }    
}
