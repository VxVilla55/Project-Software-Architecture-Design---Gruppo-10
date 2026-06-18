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
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

// Test per la scheda dettaglio/modifica di una traccia
class TrackUIDetailsControllerTest extends ApplicationTest {

    private FxRobot robot = new FxRobot();
    private TrackComponent track;

    @Override
    public void start(Stage stage) throws Exception {
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
    void title_isPopulated() {
        TextField titleField = robot.lookup("#titleField").queryAs(TextField.class);
        assertEquals("Stairway to Heaven", titleField.getText());
    }

    @Test
    void author_isPopulated() {
        TextField artistField = robot.lookup("#artistField").queryAs(TextField.class);
        assertEquals("Led Zeppelin", artistField.getText());
    }

    @Test
    void genre_isPopulated() {
        TextField genreField = robot.lookup("#genreField").queryAs(TextField.class);
        assertEquals("Rock", genreField.getText());
    }

    @Test
    void year_isPopulated() {
        TextField yearField = robot.lookup("#yearField").queryAs(TextField.class);
        assertEquals("1971", yearField.getText());
    }

    @Test
    void duration_isFormatted() {
        // 482 secondi devono essere mostrati come 00:08:02
        TextField durationField = robot.lookup("#durationField").queryAs(TextField.class);
        assertEquals("00:08:02", durationField.getText());
    }

    @Test
    void coverImage_isLoadedOnOpen() {
        // L'immagine di copertina deve essere presente all'apertura
        ImageView trackImageView = robot.lookup("#trackImageView").queryAs(ImageView.class);
        assertNotNull(trackImageView.getImage());
    }

    @Test
    void sectionTitle_showsDetailView() {
        Label sectionTitle = robot.lookup("#sectionTitle").queryAs(Label.class);
        assertEquals("Dettaglio brano", sectionTitle.getText());
    }

    @Test
    void leftButton_showsModificaOnOpen() {
        Button btnLeft = robot.lookup("#btnLeft").queryAs(Button.class);
        assertEquals("Modifica", btnLeft.getText());
    }

    @Test
    void changeCoverButton_isHiddenInReadMode() {
        // Il pulsante per cambiare copertina è visibile solo in modalità modifica
        Button changeCoverButton = robot.lookup("#changeCoverButton").queryAs(Button.class);
        assertFalse(changeCoverButton.isVisible());
    }

    @Test
    void fields_areNotEditableInReadMode() {
        TextField titleField = robot.lookup("#titleField").queryAs(TextField.class);
        assertFalse(titleField.isEditable());
    }

    // --- Dopo aver premuto "Modifica" (modalità modifica) ---

    @Test
    void afterEdit_sectionTitleChanges() {
        robot.clickOn("#btnLeft");
        Label sectionTitle = robot.lookup("#sectionTitle").queryAs(Label.class);
        assertEquals("Modifica traccia", sectionTitle.getText());
    }

    @Test
    void afterEdit_leftButtonShowsSave() {
        robot.clickOn("#btnLeft");
        Button btnLeft = robot.lookup("#btnLeft").queryAs(Button.class);
        assertEquals("Salva", btnLeft.getText());
    }

    @Test
    void afterEdit_rightButtonShowsCancel() {
        robot.clickOn("#btnLeft");
        Button btnRight = robot.lookup("#btnRight").queryAs(Button.class);
        assertEquals("Annulla", btnRight.getText());
    }

    @Test
    void afterEdit_changeCoverButtonIsVisible() {
        robot.clickOn("#btnLeft");
        Button changeCoverButton = robot.lookup("#changeCoverButton").queryAs(Button.class);
        assertTrue(changeCoverButton.isVisible());
    }

    @Test
    void afterEdit_fieldsAreEditable() {
        robot.clickOn("#btnLeft");
        TextField titleField = robot.lookup("#titleField").queryAs(TextField.class);
        assertTrue(titleField.isEditable());
    }

    // --- Dopo aver premuto "Annulla" ---

    @Test
    void afterCancel_returnsToReadMode() {
        robot.clickOn("#btnLeft");
        robot.clickOn("#btnRight");
        Label sectionTitle = robot.lookup("#sectionTitle").queryAs(Label.class);
        assertEquals("Dettaglio brano", sectionTitle.getText());
    }

    @Test
    void afterCancel_originalValuesRestored() {
        // Modificare il titolo e poi annullare deve riportare il valore originale
        robot.clickOn("#btnLeft");
        robot.doubleClickOn("#titleField").write("Valore modificato");
        robot.clickOn("#btnRight");
        TextField titleField = robot.lookup("#titleField").queryAs(TextField.class);
        assertEquals("Stairway to Heaven", titleField.getText());
    }
}
