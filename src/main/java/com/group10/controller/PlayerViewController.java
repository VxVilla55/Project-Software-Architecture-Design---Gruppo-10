package com.group10.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import com.group10.model.state.PlaybackEngine;

public class PlayerViewController implements Initializable {

    @FXML private Button playPauseButton;
    @FXML private Slider trackSlider;
    @FXML private Label trackTitle;
    @FXML private Label trackAuthor;
    @FXML private Pane progressFill; // La scia bianca a sinistra
    
    private Parent root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    if (progressFill == null) {
        System.err.println("ERRORE: progressFill non è stato collegato! Controlla l'ID nell'FXML.");
    }

    var engine = PlaybackEngine.getInstance();
    engine.setOnTick(seconds -> {
        var track = engine.getCurrentTrack();
        if (track != null && track.getDurationInSeconds() > 0) {
            double progress = (double) seconds / track.getDurationInSeconds();
            
            // CONTROLLO DI SICUREZZA
            if (trackSlider != null && !trackSlider.isValueChanging()) {
                trackSlider.setValue(progress * 100);
            }
            
            // CONTROLLO DI SICUREZZA
            if (progressFill != null) {
                progressFill.setMaxWidth(progress * 440);
            }
        }
    });

        // 2. Logica cambio traccia (Aggiorna etichette)
        engine.setOnTrackChanged(track -> {
            trackTitle.setText(track.getTitle());
            trackAuthor.setText(track.getAuthor());
            progressFill.setMaxWidth(0); // Reset scia
            trackSlider.setValue(0);     // Reset slider
        });

        // 3. Logica Slider Interattivo
        trackSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (trackSlider.isValueChanging()) {
                var track = engine.getCurrentTrack();
                if (track != null) {
                    double percent = newVal.doubleValue() / 100.0;
                    progressFill.setMaxWidth(percent * 440);
                    engine.seek((int) (track.getDurationInSeconds() * percent));
                }
            }
        });
    }

    // --- AZIONI BOTTONI ---

    @FXML
    public void handlePlayPause(ActionEvent event) {
        var engine = PlaybackEngine.getInstance();
        if (engine.getCurrentTrack() == null) return;

        if (engine.getState() instanceof com.group10.model.state.PlayingState) {
            engine.pause();
            playPauseButton.setText("▶");
        } else {
            engine.play();
            playPauseButton.setText("⏸");
            // Aggiornamento forzato
            trackTitle.setText(engine.getCurrentTrack().getTitle());
        }
    }

    @FXML
    public void handleNext(ActionEvent event) {
        PlaybackEngine.getInstance().next();
    }

    @FXML
    public void handlePrevious(ActionEvent event) {
        PlaybackEngine.getInstance().previous();
    }

    // --- ALTRI METODI ---
    public Parent getRoot() { return this.root; }
    
    @FXML public void handleFavorite(ActionEvent event) { System.out.println("Preferiti!"); }
    @FXML public void handleRepeat(ActionEvent event) { System.out.println("Repeat!"); }
    @FXML public void handleShuffle(ActionEvent event) { System.out.println("Shuffle!"); }
    @FXML public void handleNextPlaylist(ActionEvent event) { System.out.println("Next Playlist!"); }
}