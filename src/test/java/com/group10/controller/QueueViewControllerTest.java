package com.group10.controller;

import com.group10.model.state.PlaybackEngine;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

// test per la vista coda con coda vuota
class QueueViewControllerTest extends ApplicationTest {

    private FxRobot robot = new FxRobot();

    @Override
    public void start(Stage stage) throws Exception {
        PlaybackEngine.getInstance().clearQueue();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group10/view/QueueView.fxml")
        );
        Parent root = loader.load();
        stage.setScene(new Scene(root, 324, 587));
        stage.show();
    }

    @Test
    void headerShowsCorrectTitle() {
        // il titolo della sezione deve essere "Coda Tracce"
        Label sectionTitle = robot.lookup("#sectionTitle").queryAs(Label.class);
        assertEquals("Coda Tracce", sectionTitle.getText());
    }

    @Test
    void emptyQueueShowsEmptyLabel() {
        // con la coda vuota deve comparire il messaggio "La coda è vuota"
        VBox container = robot.lookup("#container").queryAs(VBox.class);
        assertEquals(1, container.getChildren().size());
        assertTrue(container.getChildren().get(0) instanceof Label);
        assertEquals("La coda è vuota", ((Label) container.getChildren().get(0)).getText());
    }

    @Test
    void emptyQueueContainerNotEmpty() {
        // il container deve avere almeno il messaggio vuoto, non zero figli
        VBox container = robot.lookup("#container").queryAs(VBox.class);
        assertFalse(container.getChildren().isEmpty());
    }
}

