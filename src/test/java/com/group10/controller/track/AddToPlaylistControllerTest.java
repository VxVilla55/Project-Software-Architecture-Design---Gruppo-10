package com.group10.controller.track;

import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

// Test per il popup di aggiunta traccia a una playlist (catalogo vuoto)
@ExtendWith(ApplicationExtension.class)
class AddToPlaylistControllerTest {

    private TrackComponent track;

    @Start
    void start(Stage stage) throws Exception {
        // Pulizia del catalogo per isolare il test
        MusicCatalogue.getInstance().getPlaylists().clear();

        track = new TrackBuilder()
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
    void titleLabel_showsTrackName(FxRobot robot) {
        // L'etichetta deve mostrare il nome della traccia tra virgolette
        Label label = robot.lookup("#trackTitleLabel").queryAs(Label.class);
        assertEquals("Aggiungi \"Bohemian Rhapsody\" a:", label.getText());
    }

    @Test
    void playlistList_isEmptyWithNoCatalogue(FxRobot robot) {
        // Nessuna playlist nel catalogo → lista vuota
        ListView<?> list = robot.lookup("#playlistListView").queryAs(ListView.class);
        assertTrue(list.getItems().isEmpty());
    }

    @Test
    void cancelButton_isVisible(FxRobot robot) {
        Button cancelButton = robot.lookup("#cancelButton").queryAs(Button.class);
        assertTrue(cancelButton.isVisible());
        assertEquals("Annulla", cancelButton.getText());
    }

    @Test
    void confirmButton_isVisible(FxRobot robot) {
        Button confirmButton = robot.lookup("#confirmButton").queryAs(Button.class);
        assertTrue(confirmButton.isVisible());
        assertEquals("Conferma", confirmButton.getText());
    }
}

// Test per il popup di aggiunta traccia a una playlist (catalogo con playlist)
@ExtendWith(ApplicationExtension.class)
class AddToPlaylistControllerWithPlaylistsTest {

    @Start
    void start(Stage stage) throws Exception {
        MusicCatalogue catalogue = MusicCatalogue.getInstance();
        catalogue.getPlaylists().clear();

        // Popola il catalogo con tre playlist
        catalogue.addPlaylist(new PlaylistComponent("Rock Classics"));
        catalogue.addPlaylist(new PlaylistComponent("Chill Vibes"));
        catalogue.addPlaylist(new PlaylistComponent("Workout Mix"));

        TrackComponent track = new TrackBuilder()
                .setTitle("Stairway to Heaven")
                .setAuthor("Led Zeppelin")
                .setDuration(482)
                .build();

        AddToPlaylistController controller = new AddToPlaylistController(track);
        FXMLLoader loader = new FXMLLoader(
                AddToPlaylistControllerWithPlaylistsTest.class
                        .getResource("/com/group10/view/AddToPlaylistView.fxml")
        );
        loader.setController(controller);
        Parent root = loader.load();
        stage.setScene(new Scene(root, 300, 320));
        stage.show();
    }

    @Test
    void playlistList_hasOneRowPerPlaylist(FxRobot robot) {
        // Tre playlist nel catalogo → tre voci nella lista
        ListView<?> list = robot.lookup("#playlistListView").queryAs(ListView.class);
        assertEquals(3, list.getItems().size());
    }

    @Test
    void playlistList_containsExpectedNames(FxRobot robot) {
        // I nomi delle playlist devono comparire tutti nella lista
        ListView<?> list = robot.lookup("#playlistListView").queryAs(ListView.class);
        assertTrue(list.getItems().contains("Rock Classics"));
        assertTrue(list.getItems().contains("Chill Vibes"));
        assertTrue(list.getItems().contains("Workout Mix"));
    }
}
