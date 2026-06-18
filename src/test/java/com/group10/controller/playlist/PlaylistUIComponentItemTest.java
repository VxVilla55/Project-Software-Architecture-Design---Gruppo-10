package com.group10.controller.playlist;

import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
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
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

// test per la riga della playlist nella lista della libreria
@ExtendWith(ApplicationExtension.class)
class PlaylistUIComponentItemTest extends ApplicationTest {

        private FxRobot robot = new FxRobot();

        @Override
        public void start(Stage stage) throws Exception {
        PlaylistComponent playlist = new PlaylistComponent("Rock Classics");
        TrackComponent t1 = new TrackBuilder()
                .setTitle("Track A").setAuthor("Artist A").setDuration(210).build();
        TrackComponent t2 = new TrackBuilder()
                .setTitle("Track B").setAuthor("Artist B").setDuration(195).build();
        TrackComponent t3 = new TrackBuilder()
                .setTitle("Track C").setAuthor("Artist C").setDuration(230).build();
        playlist.add(t1);
        playlist.add(t2);
        playlist.add(t3);

        PlaylistUIComponentItem controller = new PlaylistUIComponentItem(playlist);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group10/view/PlaylistItem.fxml")
        );
        loader.setController(controller);
        Parent root = loader.load();
        stage.setScene(new Scene(root, 600, 60));
        stage.show();
    }

    @Test
    void nameIsDisplayed() {
        Label nameLabel = robot.lookup("#nameLabel").queryAs(Label.class);
        assertEquals("Rock Classics", nameLabel.getText());
    }

    @Test
    void trackCountPlural() {
        // deve mostrare il plurale "brani"
        Label trackCountLabel = robot.lookup("#trackCountLabel").queryAs(Label.class);
        assertEquals("3 brani", trackCountLabel.getText());
        assertFalse(trackCountLabel.getText().equals("3 brano"));
    }

    @Test
    void coverImageIsLoaded() {
        // la copertina di default per le playlist deve essere caricata
        ImageView coverImage = robot.lookup("#coverImage").queryAs(ImageView.class);
        assertNotNull(coverImage.getImage());
        assertTrue(coverImage.getImage().getWidth() > 0);
    }
}
