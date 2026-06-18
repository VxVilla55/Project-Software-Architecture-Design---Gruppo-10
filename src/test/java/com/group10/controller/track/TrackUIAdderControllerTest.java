package com.group10.controller.track;

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

//test per il form di aggiunta di una nuova traccia
class TrackUIAdderControllerTest extends ApplicationTest {

    private FxRobot robot = new FxRobot();

    @Override
    public void start(Stage stage) throws Exception {
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
    void coverPreviewHasDefaultImage() {
        //all'apertura deve essere visibile la copertina di default
        ImageView coverPreview = robot.lookup("#coverPreview").queryAs(ImageView.class);
        assertNotNull(coverPreview.getImage());
        assertTrue(coverPreview.getImage().getWidth() > 0);
    }

    @Test
    void errorLabelIsHidden() {
        //ll'etichetta di errore non deve essere visibile prima di interagire
        Label errorLabel = robot.lookup("#errorLabel").queryAs(Label.class);
        assertFalse(errorLabel.isVisible());
    }

    @Test
    void coverNameLabelShowsDefaultTextOnOpen() {
        //il nome della copertina deve indicare che si usa quella di default
        Label coverNameLabel = robot.lookup("#coverNameLabel").queryAs(Label.class);
        assertEquals("Immagine di default", coverNameLabel.getText());
    }

    @Test
    void saveWithoutTitleShowsError() {
        //premere Salva senza titolo deve mostrare un messaggio di errore
        robot.clickOn("#authorField").write("Queen");
        robot.clickOn("#durationField").write("354");
        robot.clickOn("#saveButton");

        Label errorLabel = robot.lookup("#errorLabel").queryAs(Label.class);
        assertTrue(errorLabel.isVisible());
        assertFalse(errorLabel.getText().isBlank());
    }

    @Test
    void saveWithoutDurationShowsError() {
        // il builder rifiuta e deve apparire l'errore
        robot.clickOn("#titleField").write("Test Track");
        robot.clickOn("#authorField").write("Test Author");
        robot.clickOn("#saveButton");

        Label errorLabel = robot.lookup("#errorLabel").queryAs(Label.class);
        assertTrue(errorLabel.isVisible());
    }

    @Test
    void saveWithTextDurationShowsError() {
        //  errore specifico sui numeri interi
        robot.clickOn("#titleField").write("Test Track");
        robot.clickOn("#authorField").write("Test Author");
        robot.clickOn("#durationField").write("abc");
        robot.clickOn("#saveButton");

        Label errorLabel = robot.lookup("#errorLabel").queryAs(Label.class);
        assertTrue(errorLabel.isVisible());
        assertTrue(errorLabel.getText().contains("numeri interi"));
    }
}
