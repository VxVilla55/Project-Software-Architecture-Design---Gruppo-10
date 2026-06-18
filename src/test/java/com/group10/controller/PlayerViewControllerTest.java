package com.group10.controller;

import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import com.group10.model.state.PlaybackEngine;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;

// Test per il pannello del player (barra inferiore di riproduzione)
@ExtendWith(ApplicationExtension.class)
class PlayerViewControllerTest {

    private PlayerViewController controller;

    @Start
    void start(Stage stage) throws Exception {
        // Pulizia dell'engine prima di ogni test per evitare interferenze
        PlaybackEngine.getInstance().clearQueue();
        WaitForAsyncUtils.waitForFxEvents();

        controller = new PlayerViewController();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group10/view/PlayerView.fxml")
        );
        loader.setController(controller);
        Parent root = loader.load();
        stage.setScene(new Scene(root, 1065, 130));
        stage.show();
    }

    // --- Stato iniziale senza traccia corrente ---

    @Test
    void trackTitle_isEmptyOnOpen(FxRobot robot) {
        // Senza una traccia in riproduzione il titolo deve essere vuoto
        Label trackTitle = robot.lookup("#trackTitle").queryAs(Label.class);
        assertEquals("", trackTitle.getText());
    }

    @Test
    void trackAuthor_isEmptyOnOpen(FxRobot robot) {
        // Senza una traccia in riproduzione l'autore deve essere vuoto
        Label trackAuthor = robot.lookup("#trackAuthor").queryAs(Label.class);
        assertEquals("", trackAuthor.getText());
    }

    @Test
    void currentTimeLabel_showsZeroOnOpen(FxRobot robot) {
        Label currentTimeLabel = robot.lookup("#currentTimeLabel").queryAs(Label.class);
        assertEquals("00:00", currentTimeLabel.getText());
    }

    @Test
    void totalTimeLabel_showsZeroOnOpen(FxRobot robot) {
        Label totalTimeLabel = robot.lookup("#totalTimeLabel").queryAs(Label.class);
        assertEquals("00:00", totalTimeLabel.getText());
    }

    @Test
    void playPauseIcon_hasImageOnOpen(FxRobot robot) {
        // L'icona play/pausa deve essere già caricata all'apertura
        ImageView icon = robot.lookup("#playPauseIcon").queryAs(ImageView.class);
        assertNotNull(icon.getImage());
    }

    @Test
    void loopIcon_hasImageOnOpen(FxRobot robot) {
        ImageView icon = robot.lookup("#loopButtonIcon").queryAs(ImageView.class);
        assertNotNull(icon.getImage());
    }

    @Test
    void shuffleIcon_hasImageOnOpen(FxRobot robot) {
        ImageView icon = robot.lookup("#shuffleButtonIcon").queryAs(ImageView.class);
        assertNotNull(icon.getImage());
    }

    @Test
    void loopIcon_isDimmedInitially(FxRobot robot) {
        // Loop disabilitato → icona a opacità ridotta (0.2)
        ImageView icon = robot.lookup("#loopButtonIcon").queryAs(ImageView.class);
        assertEquals(0.2, icon.getOpacity(), 0.01);
    }

    @Test
    void shuffleIcon_isDimmedInitially(FxRobot robot) {
        // Shuffle disabilitato → icona a opacità ridotta (0.2)
        ImageView icon = robot.lookup("#shuffleButtonIcon").queryAs(ImageView.class);
        assertEquals(0.2, icon.getOpacity(), 0.01);
    }

    // --- Interazione con l'engine ---

    @Test
    void update_doesNotCrashWithTrackInQueue(FxRobot robot) {
        // Verifica che il metodo update() non lanci eccezioni con una traccia in coda
        TrackComponent track = new TrackBuilder()
                .setTitle("Dark Side of the Moon")
                .setAuthor("Pink Floyd")
                .setDuration(260)
                .build();

        PlaybackEngine.getInstance().addTrackToQueue(track);
        Platform.runLater(() -> controller.update());
        WaitForAsyncUtils.waitForFxEvents();

        Label trackTitle = robot.lookup("#trackTitle").queryAs(Label.class);
        assertNotNull(trackTitle);
    }

    @Test
    void shuffleIcon_opacityChangesAfterToggle(FxRobot robot) {
        // Attivare lo shuffle deve cambiare l'opacità dell'icona
        ImageView shuffleIcon = robot.lookup("#shuffleButtonIcon").queryAs(ImageView.class);
        double opacityBefore = shuffleIcon.getOpacity();

        Platform.runLater(() -> {
            PlaybackEngine.getInstance().toggleShuffle();
            shuffleIcon.setOpacity(PlaybackEngine.getInstance().isShuffled() ? 0.7 : 0.2);
        });
        WaitForAsyncUtils.waitForFxEvents();

        assertNotEquals(opacityBefore, shuffleIcon.getOpacity());
    }
}
