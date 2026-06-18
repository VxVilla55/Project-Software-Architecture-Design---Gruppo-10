package com.group10.controller.track;

import com.group10.controller.MainViewController;
import com.group10.service.command.CommandManager;
import com.group10.service.command.DeleteTrackCommand;
import com.group10.service.command.UpdateTrackCommand;
import com.group10.controller.common.AbstractUIComponent;
import com.group10.model.common.Playable;
import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 * Controller della View di dettaglio del brano
 * @author group10
 */
public class TrackUIDetailsController implements AbstractUIComponent, Initializable {

    @FXML private AnchorPane root;
    @FXML private ImageView trackImageView;
    @FXML private Button changeCoverButton;
    @FXML private TextField titleField;
    @FXML private TextField artistField;
    @FXML private TextField genreField;
    @FXML private TextField yearField;
    @FXML private TextField durationField;
    @FXML private Button btnLeft;
    @FXML private Button btnRight;
    @FXML private Label sectionTitle;

    @FXML private ToggleButton favoriteButton;
    @FXML private ToggleButton newReleaseButton;
    @FXML private ToggleButton explicitButton;

    private TrackComponent track;

    private Consumer<TrackComponent> onEditListener;
    private Consumer<TrackComponent> onDeleteListener;
    private boolean isEditing = false;

    private String newCoverPath = null;

    public TrackUIDetailsController(TrackComponent track) {
        this.track = track;
    }

    public TrackUIDetailsController(Playable t) {
        if (!(t instanceof TrackComponent)) {
            throw new RuntimeException("Impossibile visualizzare il dettaglio: il componente non è una traccia.");
        } else {
            this.track = (TrackComponent) t;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (this.track != null) {
            displayTrackDetails();
            updateUIState();
        }
    }

    private void displayTrackDetails() {
        titleField.setText(track.getTitle());
        artistField.setText(track.getAuthor());
        genreField.setText(track.getGenre());
        yearField.setText(String.valueOf(track.getYear()));

        Duration trackDuration = Duration.ofSeconds(track.getDurationInSeconds());
        String formattedDuration = String.format("%02d:%02d:%02d",
                trackDuration.toHoursPart(), trackDuration.toMinutesPart(), trackDuration.toSecondsPart());
        durationField.setText(formattedDuration);

        loadCoverImage(track.getCoverImagePath());

        if (favoriteButton != null)  favoriteButton.setSelected(track.hasTag(TrackComponent.Tag.FAVORITE));
        if (newReleaseButton != null) newReleaseButton.setSelected(track.hasTag(TrackComponent.Tag.NEW_RELEASE));
        if (explicitButton != null)  explicitButton.setSelected(track.hasTag(TrackComponent.Tag.EXPLICIT));

        newCoverPath = null;

        TextField[] fields = {titleField, artistField, genreField, yearField, durationField};
        for (TextField field : fields) {
            field.requestLayout();
        }
    }

    @FXML
    private void handleChangeCover() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Scegli immagine di copertina");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.webp")
        );
        File file = chooser.showOpenDialog(root.getScene().getWindow());
        if (file != null) {
            String copied = copyToCoverStore(file);
            if (copied != null) {
                newCoverPath = copied;
                trackImageView.setImage(new Image(file.toURI().toString()));
            }
        }
    }

    @FXML
    private void handleLeftAction(ActionEvent event) {
        if (!isEditing) {
            isEditing = true;
            updateUIState();
        } else {
            saveTrackDetails();
        }
    }

    @FXML
    private void handleRightAction(ActionEvent event) {
        if (!isEditing) {
            String title = "Conferma eliminazione";
            String header = "Eliminare definitivamente la traccia?";
            String context = new StringBuilder()
                        .append("Stai per eliminare '" + track.getTitle() + "' di '" + track.getAuthor() + "'.\n")
                        .append("La traccia verrà rimossa da:\n")
                        .append("- Catalogo principale\n")
                        .append("- Tutte le playlist\n")
                        .append("- Coda di riproduzione (se presente)\n")
                        .append("AL MOMENTO è IRREVERSIBILE")
                        .toString();
            if (MainViewController.getInstance().showConfirmation(title, header, context)) {
                CommandManager.getInstance().executeCommand(new DeleteTrackCommand(track));
                displayTrackDetails();
                isEditing = false;
                updateUIState();
            }
        } else {
            displayTrackDetails();
            isEditing = false;
            updateUIState();
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }

    private void updateUIState() {
        if (isEditing) {
            sectionTitle.setText("Modifica traccia");
            btnLeft.setText("Salva");
            btnRight.setText("Annulla");
        } else {
            sectionTitle.setText("Dettaglio brano");
            btnLeft.setText("Modifica");
            btnRight.setText("Elimina");
        }

        if (changeCoverButton != null) {
            changeCoverButton.setVisible(isEditing);
        }

        TextField[] editableFields = {titleField, artistField, genreField, yearField};

        for (TextField field : editableFields) {
            field.setEditable(isEditing);

            if (isEditing) {
                field.setStyle(
                    "-fx-background-color: white; " +
                    "-fx-border-color: #bdc3c7; " +
                    "-fx-border-radius: 4; " +
                    "-fx-background-radius: 4; " +
                    "-fx-padding: 4;"
                );
            } else {
                field.setStyle(
                    "-fx-background-color: transparent; " +
                    "-fx-border-color: transparent; " +
                    "-fx-padding: 0;"
                );
            }
        }

        if (durationField != null) {
            durationField.setEditable(false);
            durationField.setMouseTransparent(true);
            durationField.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-border-color: transparent; " +
                "-fx-padding: 0;"
            );
        }

        updateTagButton(favoriteButton, TrackComponent.Tag.FAVORITE);
        updateTagButton(newReleaseButton, TrackComponent.Tag.NEW_RELEASE);
        updateTagButton(explicitButton, TrackComponent.Tag.EXPLICIT);
    }

    private void updateTagButton(ToggleButton btn, TrackComponent.Tag tag) {
        if (btn == null) return;
        btn.setSelected(track.hasTag(tag));
        btn.setOpacity(btn.isSelected() ? 1.0 : 0.2);
        btn.setDisable(!isEditing);

        btn.setOnAction(e -> btn.setOpacity(btn.isSelected() ? 1.0 : 0.2));
    }

    private void saveTrackDetails() {
        try {
            int year = Integer.parseInt(yearField.getText().trim());

            String coverPath = (newCoverPath != null) ? newCoverPath : track.getCoverImagePath();

            TrackBuilder tb = new TrackBuilder()
                .setTitle(titleField.getText())
                .setAuthor(artistField.getText())
                .setGenre(genreField.getText())
                .setYear(year)
                .setDuration(track.getDurationInSeconds())
                .setCoverImagePath(coverPath);

            if (favoriteButton != null && favoriteButton.isSelected()) {
                tb.addTag(TrackComponent.Tag.FAVORITE);
            }
            if (newReleaseButton != null && newReleaseButton.isSelected()) {
                tb.addTag(TrackComponent.Tag.NEW_RELEASE);
            }
            if (explicitButton != null && explicitButton.isSelected()) {
                tb.addTag(TrackComponent.Tag.EXPLICIT);
            }

            TrackComponent updatedTrack = tb.build();
            CommandManager.getInstance().executeCommand(new UpdateTrackCommand(this.track, updatedTrack));
            this.track = updatedTrack;

            isEditing = false;
            updateUIState();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Impossibile salvare la modifica");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void loadCoverImage(String coverImagePath) {
        try {
            if (coverImagePath != null && !coverImagePath.isEmpty()) {
                File file = new File(coverImagePath);
                if (file.exists()) {
                    trackImageView.setImage(new Image(file.toURI().toString()));
                    return;
                }
            }
            trackImageView.setImage(
                new Image(getClass().getResourceAsStream("/com/group10/images/covers/default-cover.png"))
            );
        } catch (Exception e) {
            System.err.println("Errore nel caricamento della cover: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // copia il file scelto dall'utente in data/covers con nome univoco
    // e retsituisce il path relativo
    private String copyToCoverStore(File source) {
        try {
            Path coversDir = Paths.get("data", "covers");
            Files.createDirectories(coversDir);
            String fileName = System.currentTimeMillis() // per nome univoco
                    + source.getName().substring(source.getName().lastIndexOf('.')); // + estensione
            Path dest = coversDir.resolve(fileName);
            Files.copy(source.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            return dest.toString();
        } catch (IOException e) {
            System.err.println("Errore durante la copia della copertina");
            e.printStackTrace();
            return null;
        }
    }
}
