package com.group10.controller;

import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.common.Subscriber;
import com.group10.model.playback.PlaybackMode;
import com.group10.model.playback.RepeatPlaylist;
import com.group10.model.playback.RepeatTrack;
import com.group10.model.playback.Sequential;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Parent;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import com.group10.model.state.PlaybackEngine;

/**
 *
 * @author group10
 * PATTERN: Strategy (il Client).
 * òl
 *
 * Possiede le istanze delle modalita' di ripetizione (playbackModes) e decide quale
 * passare al Context (PlaybackEngine) col setter, quando l'utente clicca il pulsante loop.
 * E' anche Subscriber del pattern Observer, per aggiornarsi quando cambia lo stato del player.
 * Controller della barra del player in basso (play/pausa, avanti/indietro, barra di
 * avanzamento, shuffle e repeat).
 */
public class PlayerViewController implements Initializable, Subscriber {

    @FXML private Button playPauseButton;
    @FXML private Slider trackSlider;
    @FXML private Label trackTitle;
    @FXML private Label trackAuthor;

    @FXML private Label currentTimeLabel;
    @FXML private Label totalTimeLabel;

    @FXML private ImageView loopButtonIcon;
    @FXML private ImageView shuffleButtonIcon;
    @FXML private ImageView playPauseIcon;
    
    private Parent root;

    // ciclo delle modalita' di ripetizione, gestito qui lato client (pattern strategy)
    // l'ordine dell'array e' l'ordine con cui il pulsante loop le cicla
    private final PlaybackMode[] playbackModes = { new Sequential(), new RepeatPlaylist(), new RepeatTrack() };
    private int playbackModeIndex = 0;

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

        /*  
        PERCHE' Platform.runLater
        Timer di PlaybackEngine gira su un thread diverso da quello di JavaFX
        La UI può essere aggiornata peròsolo dal thread di JavaFX
        Platform.runLater() quindi esegue il codice sul thread corretto
        */

        // si aggiorna solo l'icona play/pausa quando lo stato di riproduzione cambia
        engine.setOnPlayStateChanged(isPlaying -> Platform.runLater(() -> {
            if (playPauseButton != null) {
                if (isPlaying) {
                    playPauseIcon.setImage(new Image(getClass().getResourceAsStream("/com/group10/images/icons/pause-button.png")));
                } else {
                    playPauseIcon.setImage(new Image(getClass().getResourceAsStream("/com/group10/images/icons/play-button.png")));
                }
            }
        }));

        // chiamato circa ogni 100ms dal Timer del player (PlaybackEngine.startSimulation)
        // aggiorna la barra di avanzamento e il tempo trascorso mentre la traccia suona
        engine.setOnTick(time -> Platform.runLater(() -> {
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
        }));

        // scatta quando cambia la traccia in riproduzione
        // aggiorna titolo, autore e resetta la barra e i tempi mostrati
        engine.setOnTrackChanged(track -> Platform.runLater(() -> {
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
        }));

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
            
            if (engine.isPlaying()) {
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

        if (engine.isPlaying()) {
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
        // il client avanza nel ciclo, sceglie la strategia e la passa al Context (setter)
        playbackModeIndex = (playbackModeIndex + 1) % playbackModes.length;
        PlaybackMode mode = playbackModes[playbackModeIndex];
        PlaybackEngine.getInstance().setPlaybackMode(mode);

        // icona in base alla strategy
        String icon;
        if (mode.loopsTrack()) {
            icon = "/com/group10/images/loop-track.png";
        } else {
            icon = "/com/group10/images/loop-playlist.png";
        }
        loopButtonIcon.setImage(new Image(getClass().getResourceAsStream(icon)));

        if (mode.loopsQueue() || mode.loopsTrack()) {
            loopButtonIcon.setOpacity(0.7);
        } else {
            loopButtonIcon.setOpacity(0.2);
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

        // se c'è una playlist accodata in attesa parte quella
        PlaylistComponent pending = engine.getPendingPlaylist();
        if (pending != null) {
            engine.startPlaylist(pending);
            return;
        }

        // se c'è una playlist corrente, si avanza alla successiva della lista
        PlaylistComponent current = engine.getCurrentPlaylist();
        if (current != null) {
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
            engine.startPlaylist(playlists.get((idx + 1) % playlists.size()));
            return;
        }

        // altrimenti ferma e svuota
        engine.clearQueue();
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
        MainViewController.getInstance().showQueue();
    }
}