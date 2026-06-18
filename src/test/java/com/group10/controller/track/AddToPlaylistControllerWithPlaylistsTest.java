package com.group10.controller.track;

import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//teest per il popup di aggiunta traccia a una playlist (catalogo con playlist)
class AddToPlaylistControllerWithPlaylistsTest extends ApplicationTest {

    private FxRobot robot = new FxRobot();

    @Override
    public void start(Stage stage) throws Exception {
        MusicCatalogue catalogue = MusicCatalogue.getInstance();
        catalogue.getPlaylists().clear();

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
                getClass().getResource("/com/group10/view/AddToPlaylistView.fxml")
        );
        loader.setController(controller);
        Parent root = loader.load();
        stage.setScene(new Scene(root, 300, 320));
        stage.show();
    }

    @Test
    void playlistListHasOneRowPerPlaylist() {
        //tree playlist nel catalogo -> tre voci nella lista
        ListView<?> list = robot.lookup("#playlistListView").queryAs(ListView.class);
        assertEquals(3, list.getItems().size());
    }

    @Test
    void playlistListContainsExpectedNames() {
        //u nomi delle playlist devono comparire tutti nella lista
        ListView<?> list = robot.lookup("#playlistListView").queryAs(ListView.class);
        assertTrue(list.getItems().contains("Rock Classics"));
        assertTrue(list.getItems().contains("Chill Vibes"));
        assertTrue(list.getItems().contains("Workout Mix"));
    }
}
