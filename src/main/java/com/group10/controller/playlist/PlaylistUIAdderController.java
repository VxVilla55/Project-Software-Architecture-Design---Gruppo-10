package com.group10.controller.playlist;

import com.group10.controller.common.AbstractUIAdderController;
import com.group10.controller.MainViewController; // Importiamo il MainViewController
import com.group10.model.MusicCatalogue;
import com.group10.model.builder.PlaylistBuilder;
import com.group10.model.PlaylistComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class PlaylistUIAdderController extends AbstractUIAdderController {

    @FXML
    private AnchorPane root;
    
    @FXML
    private TextField playlistNameInput;


    
    public PlaylistUIAdderController() {
    }

    @FXML
    private void handleCreatePlaylist(ActionEvent event) {
        String playlistName = playlistNameInput.getText();
        try {
            PlaylistComponent playlist = new PlaylistBuilder()
            .setName(playlistName)
            .build();
            
            MusicCatalogue.getInstance().addPlaylist(playlist);
            
            playlistNameInput.clear();
            
            // Chiudiamo il popup dopo la creazione corretta
            chiudiPopup();
            
        } catch (IllegalArgumentException ex ) {
            System.out.println("Errore: " + ex.getMessage());
            return;
        }
    }


@FXML
    private void handleAnnulla(ActionEvent event) {
        // 1. Pulisci l'input (opzionale, ma utile per la prossima volta)
        if (playlistNameInput != null) {
            playlistNameInput.clear();
        }

        // 2. Chiudi il popup usando la tua funzione di servizio
        chiudiPopup();
    }
    // Metodo di servizio per evitare di ripetere il codice
    private void chiudiPopup() {
        StackPane mainRoot = (StackPane) MainViewController.getInstance().getRoot();
        if (mainRoot.getChildren().size() > 1) {
            mainRoot.getChildren().remove(mainRoot.getChildren().size() - 1);
            mainRoot.getChildren().get(0).setEffect(null);
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}