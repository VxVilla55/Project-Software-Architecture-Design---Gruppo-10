package com.group10.controller;

import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.common.Subscriber;
import com.group10.model.strategy.PlaybackMode;
import com.group10.model.strategy.RepeatPlaylist;
import com.group10.model.strategy.Sequential;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.Parent;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import com.group10.model.state.PlaybackEngine;
import com.group10.model.state.PlayingState;

public class PlayerViewController implements Initializable, Subscriber {

    @FXML private Button playPauseButton;
    @FXML private Slider trackSlider;
    @FXML private Label trackTitle;
    @FXML private Label trackAuthor;
    @FXML private Pane progressFill;

    @FXML private ImageView loopButtonIcon;
    @FXML private ImageView shuffleButtonIcon;
    @FXML private ImageView playPauseIcon;
    
    private Parent root;

@Override
    public void initialize(URL location, ResourceBundle resources) {
        PlaybackEngine.getInstance().addSubscriber(this);
        if (progressFill == null) {
            System.err.println("ERRORE: progressFill non è stato collegato! Controlla l'ID nell'FXML.");
        }

        var engine = PlaybackEngine.getInstance();

        engine.setOnPlayStateChanged(isPlaying -> {
            if (playPauseButton != null) {
                if (isPlaying) {
                    playPauseIcon.setImage(new Image(getClass().getResourceAsStream("/com/group10/images/icons/pause-button.png")));
                } else {
                    playPauseIcon.setImage(new Image(getClass().getResourceAsStream("/com/group10/images/icons/play-button.png")));
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
                playPauseIcon.setImage(new Image(getClass().getResourceAsStream("/com/group10/images/icons/pause-button.png")));

            } else {
                playPauseIcon.setImage(new Image(getClass().getResourceAsStream("/com/group10/images/icons/play-button.png")));
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

    @FXML public void handleNextPlaylist(ActionEvent event) {
        PlaybackEngine engine = PlaybackEngine.getInstance();
        PlaylistComponent current = engine.getCurrentPlaylist();

        // nessuna playlist in riproduzione
        if (current == null) {
            System.out.println("Nessuna playlist in riproduzione.");
            engine.stop();
            return;
        }

        List<PlaylistComponent> playlists = new ArrayList<>(MusicCatalogue.getInstance().getPlaylists().values());
        // se c'è una sola playlist in libreria
        if (playlists.size() < 2) return;

        // trovo la corrente per nome (i nomi sono univoci)
        int idx = -1;
        for (int i = 0; i < playlists.size(); i++) {
            if (playlists.get(i).getName().equals(current.getName())) {
                idx = i;
                break;
            }
        }
        if (idx == -1) return;

        int nextIdx = (idx + 1) % playlists.size(); // dopo l'ultima torna alla prima
        PlaylistComponent next = playlists.get(nextIdx);
        next.playOnEngine(engine);
    }

    @Override
    public void update() {
        TrackComponent current = PlaybackEngine.getInstance().getCurrentTrack();
        if (current != null) {
            trackTitle.setText(current.getTitle());
            trackAuthor.setText(current.getAuthor());
        }
    }

    // gestione bottone queue
    @FXML
    private void handleShowQueue(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group10/view/QueueView.fxml"));
            Parent queueRoot = loader.load();
            MainViewController.getInstance().showOnRightPane(queueRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}