package com.group10.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

public class CreazionePlaylistController {

    // Questo si collega alla casella di testo creata in Scene Builder
    @FXML
    private TextField playlistNameInput;
    
    private final String viewPath  = "/com/group10/view/CreazionePlaylist.fxml";
    private Parent view = null;
    
    public CreazionePlaylistController() {
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
}