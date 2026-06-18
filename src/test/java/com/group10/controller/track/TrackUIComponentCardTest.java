package com.group10.controller.track;

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
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

// Test per la card della traccia nella griglia
@ExtendWith(ApplicationExtension.class)
class TrackUIComponentCardTest {

    @Start
    void start(Stage stage) throws Exception {
        TrackComponent track = new TrackBuilder()
                .setTitle("Bohemian Rhapsody")
                .setAuthor("Queen")
                .setDuration(354)
                .setGenre("Rock")
                .setYear(1975)
                .build();

        TrackUIComponentCard controller = new TrackUIComponentCard(track);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/group10/view/Card.fxml")
        );
        loader.setController(controller);
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void title_isDisplayed(FxRobot robot) {
        // Il titolo deve comparire nel primo slot della card
        Label titleLabel = robot.lookup("#itemPlace1").queryAs(Label.class);
        assertEquals("Bohemian Rhapsody", titleLabel.getText());
    }

    @Test
    void author_isDisplayed(FxRobot robot) {
        // L'autore deve comparire nel secondo slot
        Label authorLabel = robot.lookup("#itemPlace2").queryAs(Label.class);
        assertEquals("Queen", authorLabel.getText());
    }

    @Test
    void coverImage_isLoaded(FxRobot robot) {
        // L'ImageView deve avere un'immagine caricata
        ImageView imageView = robot.lookup("#imageView").queryAs(ImageView.class);
        assertNotNull(imageView.getImage());
    }

    @Test
    void coverImage_usesDefaultWhenNoCoverPath(FxRobot robot) {
        // Traccia senza coverImagePath → deve usare l'immagine di default
        ImageView imageView = robot.lookup("#imageView").queryAs(ImageView.class);
        assertNotNull(imageView.getImage());
        assertTrue(imageView.getImage().getWidth() > 0);
    }
}
