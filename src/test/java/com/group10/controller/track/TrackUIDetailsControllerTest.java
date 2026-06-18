package com.group10.controller.track;

import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

// Test per la scheda dettaglio/modifica di una traccia
@ExtendWith(ApplicationExtension.class)
class TrackUIDetailsControllerTest {

    private TrackComponent track;

    @Start
    void start(Stage stage) throws Exception {
        track = new TrackBuilder()
                .setTitle("Stairway to Heaven")
                .setAuthor("Led Zeppelin")
                .setDuration(482)
                .setGenre("Rock")
                .setYear(1971)
                .build();

        TrackUIDetailsController controller = new TrackUIDetailsController(track);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group10/view/TrackDetailsView.fxml")
        );
        loader.setController(controller);
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setWidth(324);
        stage.setHeight(700);
        stage.show();
    }

    // --- Stato iniziale (modalità lettura) ---

    @Test
    void title_isPopulated(FxRobot robot) {
        TextField titleField = robot.lookup("#titleField").queryAs(TextField.class);
        assertEquals("Stairway to Heaven", titleField.getText());
    }

    @Test
    void author_isPopulated(FxRobot robot) {
        TextField artistField = robot.lookup("#artistField").queryAs(TextField.class);
        assertEquals("Led Zeppelin", artistField.getText());
    }

    @Test
    void genre_isPopulated(FxRobot robot) {
        TextField genreField = robot.lookup("#genreField").queryAs(TextField.class);
        assertEquals("Rock", genreField.getText());
    }

    @Test
    void year_isPopulated(FxRobot robot) {
        TextField yearField = robot.lookup("#yearField").queryAs(TextField.class);
        assertEquals("1971", yearField.getText());
    }

    @Test
    void duration_isFormatted(FxRobot robot) {
        // 482 secondi devono essere mostrati come 00:08:02
        TextField durationField = robot.lookup("#durationField").queryAs(TextField.class);
        assertEquals("00:08:02", durationField.getText());
    }

    @Test
    void coverImage_isLoadedOnOpen(FxRobot robot) {
        // L'immagine di copertina deve essere presente all'apertura
        ImageView trackImageView = robot.lookup("#trackImageView").queryAs(ImageView.class);
        assertNotNull(trackImageView.getImage());
    }

    @Test
    void sectionTitle_showsDetailView(FxRobot robot) {
        Label sectionTitle = robot.lookup("#sectionTitle").queryAs(Label.class);
        assertEquals("Dettaglio brano", sectionTitle.getText());
    }

    @Test
    void leftButton_showsModificaOnOpen(FxRobot robot) {
        Button btnLeft = robot.lookup("#btnLeft").queryAs(Button.class);
        assertEquals("Modifica", btnLeft.getText());
    }

    @Test
    void changeCoverButton_isHiddenInReadMode(FxRobot robot) {
        // Il pulsante per cambiare copertina è visibile solo in modalità modifica
        Button changeCoverButton = robot.lookup("#changeCoverButton").queryAs(Button.class);
        assertFalse(changeCoverButton.isVisible());
    }

    @Test
    void fields_areNotEditableInReadMode(FxRobot robot) {
        TextField titleField = robot.lookup("#titleField").queryAs(TextField.class);
        assertFalse(titleField.isEditable());
    }

    // --- Dopo aver premuto "Modifica" (modalità modifica) ---

    @Test
    void afterEdit_sectionTitleChanges(FxRobot robot) {
        robot.clickOn("#btnLeft");
        Label sectionTitle = robot.lookup("#sectionTitle").queryAs(Label.class);
        assertEquals("Modifica traccia", sectionTitle.getText());
    }

    @Test
    void afterEdit_leftButtonShowsSave(FxRobot robot) {
        robot.clickOn("#btnLeft");
        Button btnLeft = robot.lookup("#btnLeft").queryAs(Button.class);
        assertEquals("Salva", btnLeft.getText());
    }

    @Test
    void afterEdit_rightButtonShowsCancel(FxRobot robot) {
        robot.clickOn("#btnLeft");
        Button btnRight = robot.lookup("#btnRight").queryAs(Button.class);
        assertEquals("Annulla", btnRight.getText());
    }

    @Test
    void afterEdit_changeCoverButtonIsVisible(FxRobot robot) {
        robot.clickOn("#btnLeft");
        Button changeCoverButton = robot.lookup("#changeCoverButton").queryAs(Button.class);
        assertTrue(changeCoverButton.isVisible());
    }

    @Test
    void afterEdit_fieldsAreEditable(FxRobot robot) {
        robot.clickOn("#btnLeft");
        TextField titleField = robot.lookup("#titleField").queryAs(TextField.class);
        assertTrue(titleField.isEditable());
    }

    // --- Dopo aver premuto "Annulla" ---

    @Test
    void afterCancel_returnsToReadMode(FxRobot robot) {
        robot.clickOn("#btnLeft");
        robot.clickOn("#btnRight");
        Label sectionTitle = robot.lookup("#sectionTitle").queryAs(Label.class);
        assertEquals("Dettaglio brano", sectionTitle.getText());
    }

    @Test
    void afterCancel_originalValuesRestored(FxRobot robot) {
        // Modificare il titolo e poi annullare deve riportare il valore originale
        robot.clickOn("#btnLeft");
        robot.doubleClickOn("#titleField").write("Valore modificato");
        robot.clickOn("#btnRight");
        TextField titleField = robot.lookup("#titleField").queryAs(TextField.class);
        assertEquals("Stairway to Heaven", titleField.getText());
    }
}
