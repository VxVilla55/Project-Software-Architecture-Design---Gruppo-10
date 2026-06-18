package com.group10.controller;

import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import com.group10.model.state.PlaybackEngine;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;

// Test per la vista coda con coda vuota
// QueueView.fxml ha fx:controller dichiarato: il loader istanzia il controller da solo
@ExtendWith(ApplicationExtension.class)
class QueueViewControllerTest {

    @Start
    void start(Stage stage) throws Exception {
        // Pulizia della coda prima del test
        PlaybackEngine.getInstance().clearQueue();
        WaitForAsyncUtils.waitForFxEvents();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group10/view/QueueView.fxml")
        );
        Parent root = loader.load();
        stage.setScene(new Scene(root, 324, 587));
        stage.show();
    }

    @Test
    void header_showsCorrectTitle(FxRobot robot) {
        // Il titolo della sezione deve essere "Coda Tracce"
        Label sectionTitle = robot.lookup("#sectionTitle").queryAs(Label.class);
        assertEquals("Coda Tracce", sectionTitle.getText());
    }

    @Test
    void emptyQueue_showsEmptyLabel(FxRobot robot) {
        // Con la coda vuota deve comparire il messaggio "La coda è vuota"
        VBox container = robot.lookup("#container").queryAs(VBox.class);
        assertEquals(1, container.getChildren().size());
        assertTrue(container.getChildren().get(0) instanceof Label);
        assertEquals("La coda è vuota", ((Label) container.getChildren().get(0)).getText());
    }

    @Test
    void emptyQueue_containerNotEmpty(FxRobot robot) {
        // Il container deve avere almeno il messaggio vuoto, non zero figli
        VBox container = robot.lookup("#container").queryAs(VBox.class);
        assertFalse(container.getChildren().isEmpty());
    }
}

// Test per la vista coda con tracce già presenti
@ExtendWith(ApplicationExtension.class)
class QueueViewControllerWithTracksTest {

    @Start
    void start(Stage stage) throws Exception {
        PlaybackEngine engine = PlaybackEngine.getInstance();
        engine.clearQueue();
        WaitForAsyncUtils.waitForFxEvents();

        // Aggiunge tre tracce alla coda prima di caricare la vista
        engine.addTrackToQueue(new TrackBuilder()
                .setTitle("Track A").setAuthor("Artist A").setDuration(200).build());
        engine.addTrackToQueue(new TrackBuilder()
                .setTitle("Track B").setAuthor("Artist B").setDuration(180).build());
        engine.addTrackToQueue(new TrackBuilder()
                .setTitle("Track C").setAuthor("Artist C").setDuration(210).build());

        FXMLLoader loader = new FXMLLoader(
                QueueViewControllerWithTracksTest.class.getResource("/com/group10/view/QueueView.fxml")
        );
        Parent root = loader.load();
        stage.setScene(new Scene(root, 324, 587));
        stage.show();
    }

    @Test
    void nonEmptyQueue_hasOneItemPerTrack(FxRobot robot) {
        // Tre tracce in coda → tre elementi nel container
        VBox container = robot.lookup("#container").queryAs(VBox.class);
        assertEquals(3, container.getChildren().size());
    }

    @Test
    void nonEmptyQueue_noEmptyLabel(FxRobot robot) {
        // Con tracce presenti non deve comparire il messaggio "La coda è vuota"
        VBox container = robot.lookup("#container").queryAs(VBox.class);
        boolean hasEmptyLabel = container.getChildren().stream()
                .anyMatch(n -> n instanceof Label
                        && ((Label) n).getText().equals("La coda è vuota"));
        assertFalse(hasEmptyLabel);
    }
}
