/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.group10.controller.track;

import com.group10.controller.MainViewController;
import com.group10.service.command.CommandManager;
import com.group10.service.command.DeleteTrackCommand;
import com.group10.service.command.RemoveTrackFromPlaylistCommand;
import com.group10.controller.common.AbstractUIComponent;
import com.group10.service.factory.TrackUIComponentFactory;
import com.group10.model.common.Playable;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.state.PlaybackEngine;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author group10
 */
public class TrackUIOptionsController implements AbstractUIComponent, Initializable {
    @FXML
    private VBox root;
    @FXML
    private Button removeTrackButton;
    
    private PlaylistComponent contextPlaylist = null;    
    private TrackComponent track;
    
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
        
    }

    @Override
    public Parent getRoot() {
        return root;
    }
    
    public void setContextPlaylist(com.group10.model.PlaylistComponent playlist) {
        this.contextPlaylist = playlist;
        
        // Se c'è una playlist, cambiamo il testo del bottone!
        if (this.contextPlaylist != null && removeTrackButton != null) {
            removeTrackButton.setText("Rimuovi dalla playlist");
        }
    }
    
    @FXML
    private void handleViewDetails(ActionEvent event) {
        //Istanzio il controllore che carica la view
        TrackUIDetailsController c = (TrackUIDetailsController) new TrackUIComponentFactory().createUIComponentDetails(track);
        //prendo dalla view il nodo Parent da collocare
        Parent trackDetailsView = c.getRoot();
                
        //cancella popup
        MainViewController.getInstance().hideMenuPopup();
        
        //VBox rightPane = (VBox) MainViewController.getInstance().getRightPane();
        //rightPane.getChildren().add(trackDetailsView);
        MainViewController.getInstance().showOnRightPane(trackDetailsView);
    }

    @FXML
    private void handleAddToQueue(ActionEvent event) {
        PlaybackEngine.getInstance().addTrackToQueue(track);
        //cancella popup
        MainViewController.getInstance().hideMenuPopup();
    }


    @FXML
    private void handlePlayAsNext(ActionEvent event) {
        PlaybackEngine.getInstance().addTrackAsNext(track);
        //cancella popup
        MainViewController.getInstance().hideMenuPopup();
    }

    @FXML
    private void handleAddToPlaylist(ActionEvent event) {
        //istanzio il loader sulla view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group10/view/AddToPlaylistView.fxml"));
        //istanzio il controller
        AddToPlaylistController controller = new AddToPlaylistController(track);
        //associo il controller alla view
        loader.setController(controller);
        try {
            Parent addToPlaylistView = loader.load();
            MainViewController.getInstance().showPopup(addToPlaylistView);
        } catch (IOException e) {
            throw new RuntimeException("Impossibile caricare AddToPlaylistView.fxml", e);
        }
        //cancella popup
        MainViewController.getInstance().hideMenuPopup();
    }

    @FXML
    private void handleRemoveTrack(ActionEvent event) {
        if (contextPlaylist != null) {
            CommandManager.getInstance().executeCommand(new RemoveTrackFromPlaylistCommand(track, contextPlaylist.getName()));
        } else {
            String title = "Conferma eliminazione";
            String header = "Eliminare definitivamente la traccia?";
            String context = new StringBuilder()
                        .append("Stai per eliminare '" + track.getTitle() + "' di '" + track.getAuthor() + "'.\n")
                        .append("La traccia verrà rimossa da:\n")
                        .append("- Catalogo principale\n")
                        .append("- Tutte le playlist\n")
                        .append("- Coda di riproduzione (se presente)\n")
                        .append("AL MOMENTO è IRREVERSIBILE")
                        .toString();
            if (MainViewController.getInstance().showConfirmation(title, header, context)) {
                CommandManager.getInstance().executeCommand(new DeleteTrackCommand(track));

                //diciamo alla schermata principale di ricaricare la grafica immediatamente!
                MainViewController.getInstance().update();
            }
            //chiudiamo  il menu a tendina
            MainViewController.getInstance().hideMenuPopup();
        }
        
    }
}