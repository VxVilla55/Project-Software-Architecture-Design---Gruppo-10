package com.group10.controller.playlist;

import com.group10.controller.common.AbstractUIAdderController;
import com.group10.model.MusicCatalogue;
import com.group10.model.builder.PlaylistBuilder;
import com.group10.model.PlaylistComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class PlaylistUIAdderController extends AbstractUIAdderController {

    @FXML
    private AnchorPane root;
    
    @FXML
    private TextField playlistNameInput;
    
    public PlaylistUIAdderController() {
    }

    // Questo metodo scatterà quando l'utente cliccherà il bottone "Crea"
    @FXML
    private void handleCreatePlaylist(ActionEvent event) {
        
        //proviamoa creare la playlist con i dati inseriti
        String playlistName = playlistNameInput.getText();
        try {
            PlaylistComponent playlist = new PlaylistBuilder()
            .setName(playlistName)
            .build(); //farà la validazione
            
            //aggiunta della playlist al catalogo
            MusicCatalogue.getInstance().addPlaylist(playlist);
            
            playlistNameInput.clear();
        } catch (IllegalArgumentException ex ) {
            //invio di un alert
            System.out.println("Hai appena richiesto di creare la playlist chiamata: '" + playlistName + "'");
            return;
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}