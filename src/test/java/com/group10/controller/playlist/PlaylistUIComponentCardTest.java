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
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

// test per la card della playlist nella griglia
class PlaylistUIComponentCardTest extends ApplicationTest {

    private FxRobot robot = new FxRobot();

    @Override
    public void start(Stage stage) throws Exception {
        PlaylistComponent playlist = new PlaylistComponent("My Favourites");
        TrackComponent t1 = new TrackBuilder()
                .setTitle("Track A").setAuthor("Artist A").setDuration(200).build();
        TrackComponent t2 = new TrackBuilder()
                .setTitle("Track B").setAuthor("Artist B").setDuration(180).build();
        playlist.add(t1);
        playlist.add(t2);

        PlaylistUIComponentCard controller = new PlaylistUIComponentCard(playlist);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group10/view/Card.fxml")
        );
        loader.setController(controller);
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void nameIsDisplayed() {
        // il nome della playlist deve comparire nel primo slot
        Label nameLabel = robot.lookup("#itemPlace1").queryAs(Label.class);
        assertEquals("My Favourites", nameLabel.getText());
    }

    @Test
    void trackCountIsInSubtitle() {
        // il sottotitolo deve contenere il numero di tracce
        Label subtitleLabel = robot.lookup("#itemPlace2").queryAs(Label.class);
        assertTrue(subtitleLabel.getText().contains("2 tracce"));
    }

    @Test
    void coverImageIsLoaded() {
        // L'immagine di copertina della playlist deve essere caricata
        ImageView imageView = robot.lookup("#imageView").queryAs(ImageView.class);
        assertNotNull(imageView.getImage());
        assertTrue(imageView.getImage().getWidth() > 0);
    }
}
