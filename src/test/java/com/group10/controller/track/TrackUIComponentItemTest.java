package com.group10.controller.track;

import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
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

import static org.junit.jupiter.api.Assertions.*;

// Test per la riga della traccia nella lista libreria/playlist
@ExtendWith(ApplicationExtension.class)
class TrackUIComponentItemTest {

    @Start
    void start(Stage stage) throws Exception {
        TrackComponent track = new TrackBuilder()
                .setTitle("Hotel California")
                .setAuthor("Eagles")
                .setDuration(391)
                .setGenre("Rock")
                .setYear(1977)
                .build();

        TrackUIComponentItem controller = new TrackUIComponentItem(track);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group10/view/TrackItem.fxml")
        );
        loader.setController(controller);
        Parent root = loader.load();
        stage.setScene(new Scene(root, 650, 55));
        stage.show();
    }

    @Test
    void title_isDisplayed(FxRobot robot) {
        Label titleLabel = robot.lookup("#titleLabel").queryAs(Label.class);
        assertEquals("Hotel California", titleLabel.getText());
    }

    @Test
    void artist_isDisplayed(FxRobot robot) {
        Label artistLabel = robot.lookup("#artistLabel").queryAs(Label.class);
        assertEquals("Eagles", artistLabel.getText());
    }

    @Test
    void genre_isDisplayed(FxRobot robot) {
        Label genreLabel = robot.lookup("#genreLabel").queryAs(Label.class);
        assertEquals("Rock", genreLabel.getText());
    }

    @Test
    void year_isDisplayed(FxRobot robot) {
        Label yearLabel = robot.lookup("#yearLabel").queryAs(Label.class);
        assertEquals("1977", yearLabel.getText());
    }

    @Test
    void duration_isFormatted(FxRobot robot) {
        // 391 secondi → 00:06:31
        Label durationLabel = robot.lookup("#durationLabel").queryAs(Label.class);
        assertEquals("00:06:31", durationLabel.getText());
    }

    @Test
    void coverImage_isLoaded(FxRobot robot) {
        // La miniatura della copertina deve essere caricata
        ImageView coverImage = robot.lookup("#coverImage").queryAs(ImageView.class);
        assertNotNull(coverImage.getImage());
        assertTrue(coverImage.getImage().getWidth() > 0);
    }

    @Test
    void tagLabels_areHiddenByDefault(FxRobot robot) {
        // I tag speciali devono essere nascosti se la traccia non li ha impostati
        Label favourite = robot.lookup("#favouriteLabel").queryAs(Label.class);
        Label newRelease = robot.lookup("#newReleaseLabel").queryAs(Label.class);
        Label explicit   = robot.lookup("#explicitLabel").queryAs(Label.class);
        assertFalse(favourite.isVisible());
        assertFalse(newRelease.isVisible());
        assertFalse(explicit.isVisible());
    }
}
