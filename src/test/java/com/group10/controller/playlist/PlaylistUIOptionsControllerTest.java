package com.group10.controller.playlist;

import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

// test per il menu opzioni di una playlist (accoda, rinomina, elimina)
class PlaylistUIOptionsControllerTest extends ApplicationTest {

        private FxRobot robot = new FxRobot();

        @Override
        public void start(Stage stage) throws Exception {
        PlaylistComponent playlist = new PlaylistComponent("Jazz Night");
        TrackComponent t1 = new TrackBuilder()
                .setTitle("Blue in Green").setAuthor("Miles Davis").setDuration(337).build();
        TrackComponent t2 = new TrackBuilder()
                .setTitle("So What").setAuthor("Miles Davis").setDuration(562).build();
        playlist.add(t1);
        playlist.add(t2);

        PlaylistUIOptionsController controller = new PlaylistUIOptionsController(playlist);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group10/view/PlaylistOptions.fxml")
        );
        loader.setController(controller);
        Parent root = loader.load();
        stage.setScene(new Scene(root, 232, 126));
        stage.show();
    }

    @Test
    void addToQueueButtonIsVisible() {
        // pulsante per accodare la playlist deve essere visibile con il testo corretto
        Button btn = robot.lookup("#addPlaylistToQueueButton").queryAs(Button.class);
        assertTrue(btn.isVisible());
        assertEquals("Aggiungi alla coda", btn.getText());
    }

    @Test
    void renameButtonIsVisible() {
        // pulsante di rinomina deve essere visibile con il testo corretto
        Button btn = robot.lookup("#renamePlaylistButton").queryAs(Button.class);
        assertTrue(btn.isVisible());
        assertEquals("Rinomina playlist", btn.getText());
    }

    @Test
    void deleteButtonIsVisible() {
        //pulsante di eliminazione deve essere visibile con il testo corretto
        Button btn = robot.lookup("#removePlaylistButton").queryAs(Button.class);
        assertTrue(btn.isVisible());
        assertEquals("Elimina playlist", btn.getText());
    }

    @Test
    void menuHasAllThreeButtons() {
        //mmenu deve contenere esattamente i tre pulsanti previsti
        assertNotNull(robot.lookup("#addPlaylistToQueueButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#renamePlaylistButton").queryAs(Button.class));
        assertNotNull(robot.lookup("#removePlaylistButton").queryAs(Button.class));
    }
}
