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
        try {
            // 1. Istanziamo il controllore del popup passando la traccia corrente (this.track)
            AddToPlaylistController c = new AddToPlaylistController(this.track);
            
            // 2. Prendiamo dalla view il nodo Parent (la checklist) da collocare
            Parent checklistView = c.getRoot();

            // 3. Mostriamo il popup personalizzato sovrapposto
            showCustomPopup(checklistView);
            
        } catch (Exception e) {
            System.err.println("Errore nel caricamento del popup della playlist:");
            e.printStackTrace();
        }
    }
    
    /**
     * Mostra il popup sopra la vista principale sfruttando lo StackPane radice dell'applicazione
     */
    private void showCustomPopup(Parent popup) {
        try {
            if (root != null && root.getScene() != null) {
                // Recuperiamo lo StackPane principale cercando l'id "root" della MainView
                StackPane mainRoot = (StackPane) root.getScene().lookup("#root");
                
                if (mainRoot != null) {
                    // Sfoco la schermata principale (il primo figlio dello StackPane)
                    mainRoot.getChildren().get(0).setEffect(new GaussianBlur(10));
                    
                    // Rimuovo altri layer di popup precedentemente rimasti aperti (se ce ne sono)
                    mainRoot.getChildren().removeIf(child -> child != mainRoot.getChildren().get(0));
                    
                    // Carico il pannello di blocco e il popup
                    StackPane layer = new StackPane();
                    Pane pane = new Pane();
                    pane.setEffect(new GaussianBlur(10));
                    
                    // Al click fuori dal popup, chiudiamo la finestrella ripristinando lo sfondo nitido
                    pane.setOnMouseClicked(e -> {
                        if (mainRoot.getChildren().size() > 1) {
                            mainRoot.getChildren().remove(mainRoot.getChildren().size() - 1);
                            mainRoot.getChildren().get(0).setEffect(null);
                        }
                    });
                    
                    layer.getChildren().add(pane);
                    layer.getChildren().add(popup); // Iniettiamo la tua ListView con checklist

                    mainRoot.getChildren().add(layer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}