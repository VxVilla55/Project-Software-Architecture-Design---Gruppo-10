package com.group10.controller;

import com.group10.model.strategy.PlaybackMode;
import com.group10.model.strategy.RepeatPlaylist;
import com.group10.model.strategy.Sequential;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import com.group10.model.state.PlaybackEngine;
import com.group10.model.state.PlayingState;

public class PlayerViewController implements Initializable {

    @FXML private Button playPauseButton;
    @FXML private Slider trackSlider;
    @FXML private Label trackTitle;
    @FXML private Label trackAuthor;
    @FXML private Pane progressFill;

    @FXML private ImageView loopButtonIcon;
    @FXML private ImageView shuffleButtonIcon;
    
    private Parent root;

@Override
    public void initialize(URL location, ResourceBundle resources) {
        if (progressFill == null) {
            System.err.println("ERRORE: progressFill non è stato collegato! Controlla l'ID nell'FXML.");
        }

        var engine = PlaybackEngine.getInstance();
        
        engine.setOnPlayStateChanged(isPlaying -> {
            if (playPauseButton != null) {
                if (isPlaying) {
                    playPauseButton.setText("⏸");
                } else {
                    playPauseButton.setText("▶️");
                }
            }
        });
        
        engine.setOnTick(time -> { 
            var track = engine.getCurrentTrack();
            if (track != null && track.getDurationInSeconds() > 0) {
                double progress = time / track.getDurationInSeconds();
                
                if (trackSlider != null && !trackSlider.isPressed()) {
                    trackSlider.setValue(progress * 100);
                    if (progressFill != null) {
                        progressFill.setMaxWidth(progress * 440);
                    }
                }
            }
        });

    engine.setOnTrackChanged(track -> {
            if (track != null) {
                trackTitle.setText(track.getTitle());
                trackAuthor.setText(track.getAuthor());
            } else {
                // PRIMO PUNTO DA SVUOTARE
                trackTitle.setText("");
                trackAuthor.setText("");
            }
            if (progressFill != null) progressFill.setMaxWidth(0); 
            if (trackSlider != null) trackSlider.setValue(0);     
        });

        trackSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (trackSlider.isPressed()) {
                var track = engine.getCurrentTrack();
                if (track != null) {
                    double percent = newVal.doubleValue() / 100.0;
                    if (progressFill != null) {
                        progressFill.setMaxWidth(percent * 440);
                    }
                    engine.seek(track.getDurationInSeconds() * percent);
                }
            }
        });

        // Sincronizzazione iniziale al caricamento
        var current = engine.getCurrentTrack();
        if (current != null) {
            trackTitle.setText(current.getTitle());
            trackAuthor.setText(current.getAuthor());
            if (engine.getState() instanceof PlayingState) {
                if (playPauseButton != null) playPauseButton.setText("⏸");
            } else {
                if (playPauseButton != null) playPauseButton.setText("▶️");
            }
        } else {
            trackTitle.setText("");
            trackAuthor.setText("");
        }

        loopButtonIcon.setImage(new Image(getClass().getResourceAsStream("/com/group10/images/loop-playlist.png")));
        loopButtonIcon.setOpacity(0.2);

        shuffleButtonIcon.setImage(new Image(getClass().getResourceAsStream("/com/group10/images/shuffle.png")));
        shuffleButtonIcon.setOpacity(0.2);
    }


    @FXML
    public void handlePlayPause(ActionEvent event) {
        var engine = PlaybackEngine.getInstance();
        if (engine.getCurrentTrack() == null) return;

        if (engine.getState() instanceof PlayingState) {
            engine.pause();
        } else {
            engine.play();
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

    public Parent getRoot() { return this.root; }
    
    @FXML public void handleFavorite(ActionEvent event) { System.out.println("Preferiti!"); }
    @FXML public void handleRepeat(ActionEvent event) {
        PlaybackEngine.getInstance().cycleRepeatMode();

        PlaybackMode playbackMode = PlaybackEngine.getInstance().getPlaybackMode();
        if (playbackMode instanceof Sequential) {
            loopButtonIcon.setImage(new Image(getClass().getResourceAsStream("/com/group10/images/loop-playlist.png")));
            loopButtonIcon.setOpacity(0.2);
        } else if (playbackMode instanceof RepeatPlaylist) {
            loopButtonIcon.setImage(new Image(getClass().getResourceAsStream("/com/group10/images/loop-playlist.png")));
            loopButtonIcon.setOpacity(0.7);
        } else {
            loopButtonIcon.setImage(new Image(getClass().getResourceAsStream("/com/group10/images/loop-track.png")));
            loopButtonIcon.setOpacity(0.7);
        }
    }

    @FXML public void handleShuffle(ActionEvent event) {
        PlaybackEngine.getInstance().toggleShuffle();
        if (PlaybackEngine.getInstance().isShuffled()) {
            shuffleButtonIcon.setOpacity(0.7);
        } else shuffleButtonIcon.setOpacity(0.2);
    }

    @FXML public void handleNextPlaylist(ActionEvent event) { System.out.println("Next Playlist!"); }
}