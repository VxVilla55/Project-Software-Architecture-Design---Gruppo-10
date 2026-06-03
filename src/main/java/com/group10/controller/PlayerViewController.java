package com.group10.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable; // Per l'interfaccia di inizializzazione
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.Parent;

// Questi sono i due import che probabilmente ti mancano:
import java.net.URL;             // <-- Questo risolve il tuo errore
import java.util.ResourceBundle;  // <-- Necessario per Initializable
import com.group10.model.state.PlaybackEngine; //

public class PlayerViewController implements Initializable { // Implementa Initializable

    @FXML private Button playPauseButton;
    @FXML private Slider trackSlider;
    @FXML private Label trackTitle;
    @FXML private Label trackAuthor;
    @FXML private Parent root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Colleghiamo lo slider al motore di riproduzione
        PlaybackEngine.getInstance().setOnTick(seconds -> {
            var engine = PlaybackEngine.getInstance();
            var track = engine.getCurrentTrack();
            
            if (track != null && track.getDurationInSeconds() > 0) {
                // Calcolo percentuale: (tempo / durata) * 100
                double progress = (double) seconds / track.getDurationInSeconds() * 100;
                trackSlider.setValue(progress);
            }
        });
        trackSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
        if (trackSlider.isValueChanging()) {
            var engine = PlaybackEngine.getInstance();
            var track = engine.getCurrentTrack();
            
            if (track != null) {
                // Calcoliamo a quale secondo saltare in base alla percentuale
                double percent = newVal.doubleValue() / 100.0;
                int targetSecond = (int) (track.getDurationInSeconds() * percent);
                
                // Saltiamo al secondo scelto
                engine.seek(targetSecond);
            }
        }
    });
    }
    

    //metodo per restituire la radice al main---
    public Parent getRoot() {
        return this.root;
    }

    // --- AZIONI DEI BOTTONI ---

    @FXML
    public void handlePlayPause(javafx.event.ActionEvent event) {
        var engine = com.group10.model.state.PlaybackEngine.getInstance();
        
        // Controllo se c'è un brano
        if (engine.getCurrentTrack() == null) {
            System.out.println("⚠️ La coda è vuota, impossibile riprodurre!");
            return;
        }

        // Cambio stato e testo del bottone
        if (engine.getState() instanceof com.group10.model.state.PlayingState) {
            engine.pause();
            playPauseButton.setText("PLAY");
        } else {
            engine.play();
            playPauseButton.setText("PAUSA");
        }
    }

    @FXML
    public void handleNext(javafx.event.ActionEvent event) {
        com.group10.model.state.PlaybackEngine.getInstance().next();
    }

    @FXML
    public void handlePrevious(javafx.event.ActionEvent event) {
        com.group10.model.state.PlaybackEngine.getInstance().previous();
    }

    // Metodi vuoti per evitare crash se clicchi gli altri tasti
    @FXML 
    public void handleFavorite(javafx.event.ActionEvent event) {
        System.out.println("Aggiunto ai preferiti!");
    }

    @FXML 
    public void handleRepeat(javafx.event.ActionEvent event) {
        System.out.println("Ripetizione attivata!");
    }

    @FXML 
    public void handleShuffle(javafx.event.ActionEvent event) {
        System.out.println("Shuffle attivato!");
    }

    @FXML 
    public void handleNextPlaylist(javafx.event.ActionEvent event) {
        System.out.println("Passo alla prossima playlist!");
    }
}