package com.group10.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
        
        // 1. Leggiamo cosa ha scritto l'utente
        String nomePlaylist = playlistNameInput.getText();

        // 2. Controlliamo che non sia vuoto
        if (nomePlaylist == null || nomePlaylist.trim().isEmpty()) {
            System.out.println("❌ ERRORE: Hai provato a creare una playlist senza nome!");
            return; // Blocchiamo l'esecuzione qui
        }

        // 3. Se il nome è valido, mostriamo l'effetto!
        System.out.println("✅ SUCCESSO: Hai appena richiesto di creare la playlist chiamata: '" + nomePlaylist + "'");
        
        // Qui in futuro aggiungerai il codice per salvare davvero la playlist nel tuo model.
        // Esempio fittizio: playlistManager.addPlaylist(new Playlist(nomePlaylist));

        // 4. (Opzionale) Svuotiamo la casella di testo per il prossimo inserimento
        playlistNameInput.clear();
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}