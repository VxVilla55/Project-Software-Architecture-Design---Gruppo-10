package com.group10.controller.track;

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

// Test per il form di aggiunta di una nuova traccia
@ExtendWith(ApplicationExtension.class)
class TrackUIAdderControllerTest {

    @Start
    void start(Stage stage) throws Exception {
        TrackUIAdderController controller = new TrackUIAdderController();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group10/view/TrackAdderView.fxml")
        );
        loader.setController(controller);
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void coverPreview_hasDefaultImageOnOpen(FxRobot robot) {
        // All'apertura deve essere visibile la copertina di default
        ImageView coverPreview = robot.lookup("#coverPreview").queryAs(ImageView.class);
        assertNotNull(coverPreview.getImage());
        assertTrue(coverPreview.getImage().getWidth() > 0);
    }

    @Test
    void errorLabel_isHiddenOnOpen(FxRobot robot) {
        // L'etichetta di errore non deve essere visibile prima di interagire
        Label errorLabel = robot.lookup("#errorLabel").queryAs(Label.class);
        assertFalse(errorLabel.isVisible());
    }

    @Test
    void coverNameLabel_showsDefaultTextOnOpen(FxRobot robot) {
        // Il nome della copertina deve indicare che si usa quella di default
        Label coverNameLabel = robot.lookup("#coverNameLabel").queryAs(Label.class);
        assertEquals("Immagine di default", coverNameLabel.getText());
    }

    @Test
    void save_withoutTitle_showsError(FxRobot robot) {
        // Premere Salva senza titolo deve mostrare un messaggio di errore
        robot.clickOn("#authorField").write("Queen");
        robot.clickOn("#durationField").write("354");
        robot.clickOn("#saveButton");

        Label errorLabel = robot.lookup("#errorLabel").queryAs(Label.class);
        assertTrue(errorLabel.isVisible());
        assertFalse(errorLabel.getText().isBlank());
    }

    @Test
    void save_withoutDuration_showsError(FxRobot robot) {
        // Durata vuota → il builder rifiuta e deve apparire l'errore
        robot.clickOn("#titleField").write("Test Track");
        robot.clickOn("#authorField").write("Test Author");
        robot.clickOn("#saveButton");

        Label errorLabel = robot.lookup("#errorLabel").queryAs(Label.class);
        assertTrue(errorLabel.isVisible());
    }

    @Test
    void save_withTextDuration_showsError(FxRobot robot) {
        // Durata non numerica → errore specifico sui numeri interi
        robot.clickOn("#titleField").write("Test Track");
        robot.clickOn("#authorField").write("Test Author");
        robot.clickOn("#durationField").write("abc");
        robot.clickOn("#saveButton");

        Label errorLabel = robot.lookup("#errorLabel").queryAs(Label.class);
        assertTrue(errorLabel.isVisible());
        assertTrue(errorLabel.getText().contains("numeri interi"));
    }
}
