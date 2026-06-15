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

    // --- NUOVO: Etichette per il tempo ---
    @FXML private Label currentTimeLabel;
    @FXML private Label totalTimeLabel;

    @FXML private ImageView loopButtonIcon;
    @FXML private ImageView shuffleButtonIcon;
    @FXML private ImageView playPauseIcon;
    
    private Parent root;

    // Metodo helper per formattare i secondi in mm:ss
    private String formatTime(double seconds) {
        int totalSeconds = (int) Math.max(0, seconds);
        int minutes = totalSeconds / 60;
        int secs = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    private void updateSliderFill(double percent) {
        javafx.scene.Node track = trackSlider.lookup(".track");
        if (track != null) {
            int p = (int) Math.round(Math.max(0, Math.min(100, percent)));
            track.setStyle("-fx-background-color: linear-gradient(to right, #00BFA5 " + p + "%, #EFE6CC " + p + "%);");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PlaybackEngine.getInstance().addSubscriber(this);

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
                    updateSliderFill(progress * 100);
                }

                if (currentTimeLabel != null) {
                    currentTimeLabel.setText(formatTime(time));
                }
            }
        });

        engine.setOnTrackChanged(track -> {
            if (track != null) {
                trackTitle.setText(track.getTitle());
                trackAuthor.setText(track.getAuthor());

                if (totalTimeLabel != null) totalTimeLabel.setText(formatTime(track.getDurationInSeconds()));
                if (currentTimeLabel != null) currentTimeLabel.setText("00:00");

            } else {
                trackTitle.setText("");
                trackAuthor.setText("");
                if (totalTimeLabel != null) totalTimeLabel.setText("00:00");
                if (currentTimeLabel != null) currentTimeLabel.setText("00:00");
            }
            if (trackSlider != null) trackSlider.setValue(0);
            updateSliderFill(0);
        });

        trackSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (trackSlider.isPressed()) {
                var track = engine.getCurrentTrack();
                if (track != null) {
                    double percent = newVal.doubleValue() / 100.0;
                    updateSliderFill(newVal.doubleValue());
                    double seekTime = track.getDurationInSeconds() * percent;
                    engine.seek(seekTime);

                    if (currentTimeLabel != null) currentTimeLabel.setText(formatTime(seekTime));
                }
            }
        });

        // Sincronizzazione iniziale al caricamento
        var current = engine.getCurrentTrack();
        if (current != null) {
            trackTitle.setText(current.getTitle());
            trackAuthor.setText(current.getAuthor());
            if (totalTimeLabel != null) totalTimeLabel.setText(formatTime(current.getDurationInSeconds()));
            if (currentTimeLabel != null) currentTimeLabel.setText(formatTime(engine.getCurrentTime()));
            
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

        if (current == null) {
            System.out.println("Nessuna playlist in riproduzione.");
            engine.stop();
            return;
        }

        List<PlaylistComponent> playlists = new ArrayList<>(MusicCatalogue.getInstance().getPlaylists().values());
        if (playlists.size() < 2) return;

        int idx = -1;
        for (int i = 0; i < playlists.size(); i++) {
            if (playlists.get(i).getName().equals(current.getName())) {
                idx = i;
                break;
            }
        }
        if (idx == -1) return;

        int nextIdx = (idx + 1) % playlists.size(); 
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

    @FXML
    private void handleShowQueue(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group10/view/QueueView.fxml"));
            Parent queueRoot = loader.load();
            MainViewController.getInstance().showOnRightPane(queueRoot);
            MainViewController.getInstance().setSelectedTrack(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}