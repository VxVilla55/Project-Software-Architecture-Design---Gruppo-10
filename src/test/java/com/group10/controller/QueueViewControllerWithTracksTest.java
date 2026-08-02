package com.group10.controller;

import com.group10.model.builder.TrackBuilder;
import com.group10.model.state.PlaybackEngine;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

//test per la vista coda con tracce già presenti
class QueueViewControllerWithTracksTest extends ApplicationTest {

    private FxRobot robot = new FxRobot();

    @Override
    public void start(Stage stage) throws Exception {
        PlaybackEngine engine = PlaybackEngine.getInstance();
        engine.clearQueue();

        // Aggiunge tre tracce alla coda prima di caricare la vista
        engine.addTrackToQueue(new TrackBuilder()
                .setTitle("Track A").setAuthor("Artist A").setDuration(200).build());
        engine.addTrackToQueue(new TrackBuilder()
                .setTitle("Track B").setAuthor("Artist B").setDuration(180).build());
        engine.addTrackToQueue(new TrackBuilder()
                .setTitle("Track C").setAuthor("Artist C").setDuration(210).build());

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group10/view/QueueView.fxml")
        );
        Parent root = loader.load();
        stage.setScene(new Scene(root, 324, 587));
        stage.show();
    }

    @Test
    void nonEmptyQueueHasOneItemPerTrack() {
        //trre tracce in coda → tre elementi nel container
        VBox container = robot.lookup("#container").queryAs(VBox.class);
        assertEquals(3, container.getChildren().size());
    }

    @Test
    void nonEmptyQueueNoEmptyLabel() {
        //con tracce presenti non deve comparire il messaggio "La coda è vuota"
        VBox container = robot.lookup("#container").queryAs(VBox.class);
        boolean hasEmptyLabel = false;

        for (Node n : container.getChildren()) {
            if (n instanceof Label) {
                Label label = (Label) n;

                if (label.getText().equals("La coda è vuota")) {
                    hasEmptyLabel = true;
                    break;
                }
            }
        }
    }
}
