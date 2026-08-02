package com.group10.controller;

import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import com.group10.model.state.PlaybackEngine;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

//teest per il pannello del player (barra inferiore di riproduzione)
class PlayerViewControllerTest extends ApplicationTest {

    private FxRobot robot = new FxRobot();
    private PlayerViewController controller;

    @Override
    public void start(Stage stage) throws Exception {
        PlaybackEngine.getInstance().clearQueue();

        controller = new PlayerViewController();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group10/view/PlayerView.fxml")
        );
        loader.setController(controller);
        Parent root = loader.load();
        stage.setScene(new Scene(root, 1065, 130));
        stage.show();
    }


    @Test
    void trackTitleIsEmpty() {
        //senza una traccia in riproduzione il titolo deve essere vuoto
        Label trackTitle = robot.lookup("#trackTitle").queryAs(Label.class);
        assertEquals("", trackTitle.getText());
    }

    @Test
    void trackAuthorIsEmpty() {
        // senza una traccia in riproduzione l'autore deve essere vuoto
        Label trackAuthor = robot.lookup("#trackAuthor").queryAs(Label.class);
        assertEquals("", trackAuthor.getText());
    }

    @Test
    void currentTimeLabelShowsZero() {
        Label currentTimeLabel = robot.lookup("#currentTimeLabel").queryAs(Label.class);
        assertEquals("00:00", currentTimeLabel.getText());
    }

    @Test
    void totalTimeLabelShowsZero() {
        Label totalTimeLabel = robot.lookup("#totalTimeLabel").queryAs(Label.class);
        assertEquals("00:00", totalTimeLabel.getText());
    }

    @Test
    void playPauseIconHasImage() {
        // k'icona play/pausa deve essere già caricata all'apertura
        ImageView icon = robot.lookup("#playPauseIcon").queryAs(ImageView.class);
        assertNotNull(icon.getImage());
    }

    @Test
    void loopIconHasImage() {
        ImageView icon = robot.lookup("#loopButtonIcon").queryAs(ImageView.class);
        assertNotNull(icon.getImage());
    }

    @Test
    void shuffleIconHasImage() {
        ImageView icon = robot.lookup("#shuffleButtonIcon").queryAs(ImageView.class);
        assertNotNull(icon.getImage());
    }

    @Test
    void loopIconIsDimmedInitially() {
        // loop disabilitato -> icona a opacità ridotta (0.2)
        ImageView icon = robot.lookup("#loopButtonIcon").queryAs(ImageView.class);
        assertEquals(0.2, icon.getOpacity(), 0.01);
    }

    @Test
    void shuffleIconIsDimmedInitially() {
        // shuffle disabilitato -> icona a opacità ridotta (0.2)
        ImageView icon = robot.lookup("#shuffleButtonIcon").queryAs(ImageView.class);
        assertEquals(0.2, icon.getOpacity(), 0.01);
    }
}
