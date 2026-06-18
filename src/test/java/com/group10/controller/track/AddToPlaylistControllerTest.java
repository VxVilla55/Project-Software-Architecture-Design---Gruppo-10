package com.group10.controller.track;

import com.group10.model.MusicCatalogue;
import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

// test per il popup di aggiunta traccia a una playlist (catalogo vuoto)
class AddToPlaylistControllerTest extends ApplicationTest {

    private FxRobot robot = new FxRobot();

    @Override
    public void start(Stage stage) throws Exception {
        //pulizia del catalogo per isolare il test
        MusicCatalogue.getInstance().getPlaylists().clear();

        TrackComponent track = new TrackBuilder()
                .setTitle("Bohemian Rhapsody")
                .setAuthor("Queen")
                .setDuration(354)
                .build();

        AddToPlaylistController controller = new AddToPlaylistController(track);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group10/view/AddToPlaylistView.fxml")
        );
        loader.setController(controller);
        Parent root = loader.load();
        stage.setScene(new Scene(root, 300, 320));
        stage.show();
    }

    @Test
    void titleLabelShowsTrackName() {
        //etichetta deve mostrare il nome della traccia tra virgolette
        Label label = robot.lookup("#trackTitleLabel").queryAs(Label.class);
        assertEquals("Aggiungi \"Bohemian Rhapsody\" a:", label.getText());
    }

    @Test
    void playlistListIsEmptyWithNoCatalogue() {
        //nessuna playlist nel catalogo → lista vuota
        ListView<?> list = robot.lookup("#playlistListView").queryAs(ListView.class);
        assertTrue(list.getItems().isEmpty());
    }

    @Test
    void cancelButtonIsVisible() {
        Button cancelButton = robot.lookup("#cancelButton").queryAs(Button.class);
        assertTrue(cancelButton.isVisible());
        assertEquals("Annulla", cancelButton.getText());
    }

    @Test
    void confirmButtonIsVisible() {
        Button confirmButton = robot.lookup("#confirmButton").queryAs(Button.class);
        assertTrue(confirmButton.isVisible());
        assertEquals("Conferma", confirmButton.getText());
    }
}
