package com.group10.controller.track;

import com.group10.controller.MainViewController;
import com.group10.service.command.CommandManager;
import com.group10.service.command.DeleteTrackCommand;
import com.group10.service.command.UpdateTrackCommand;
import com.group10.controller.common.AbstractUIComponent;
import com.group10.model.common.Playable;
import com.group10.model.TrackComponent;
import com.group10.model.builder.TrackBuilder;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * Controller della View di dettaglio del brano
 * @author group10
 */
public class TrackUIDetailsController implements AbstractUIComponent, Initializable {

    @FXML private AnchorPane root;
    @FXML private ImageView trackImageView;
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
        String formattedDuration = String.format("%02d:%02d:%02d", trackDuration.toHoursPart(), trackDuration.toMinutesPart(), trackDuration.toSecondsPart());
        durationField.setText(formattedDuration);

        // --- NUOVO: Carica lo stato dei tag nelle checkbox ---
        if (favoriteButton != null) {
            favoriteButton.setSelected(track.hasTag(TrackComponent.Tag.FAVORITE));
        }
        if (newReleaseButton != null) {
            newReleaseButton.setSelected(track.hasTag(TrackComponent.Tag.NEW_RELEASE));
        }
        if (explicitButton != null) {
            explicitButton.setSelected(track.hasTag(TrackComponent.Tag.EXPLICIT));
        }
        
        TextField[] fields = {titleField, artistField, genreField, yearField, durationField};
        
        for (TextField field : fields) {
            field.requestLayout();
        }
    }
    
    @FXML
    private void handleLeftAction(ActionEvent event) {
        if(!isEditing) {
            isEditing = true;
            updateUIState();
        } else {
            System.out.println("init"+track.toString());
            saveTrackDetails(); 
        }
    }

    @FXML
    private void handleRightAction(ActionEvent event) {
        if(!isEditing) {
            if (MainViewController.getInstance().showDeleteConfirmation(track.getTitle())) {
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

        //if (favoriteButton != null) favoriteButton.setDisable(!isEditing);
        //if (newReleaseButton != null) newReleaseButton.setDisable(!isEditing);
        //if (explicitButton != null) explicitButton.setDisable(!isEditing);
        updateTagButton(favoriteButton, TrackComponent.Tag.FAVORITE);
        updateTagButton(newReleaseButton, TrackComponent.Tag.NEW_RELEASE);
        updateTagButton(explicitButton, TrackComponent.Tag.EXPLICIT);
    }

    private void updateTagButton(ToggleButton btn, TrackComponent.Tag tag) {
        if (btn == null) return;
        btn.setSelected(track.hasTag(tag));
        btn.setOpacity(btn.isSelected() ? 1.0 : 0.2);
        btn.setDisable(!isEditing);           // cliccabile solo in edit

        btn.setOnAction(e -> btn.setOpacity(btn.isSelected() ? 1.0 : 0.2));
    }

    private void saveTrackDetails() {
        try {
            int year = Integer.parseInt(yearField.getText().trim());
            
            TrackBuilder tb = new TrackBuilder()
                .setTitle(titleField.getText())
                .setAuthor(artistField.getText())
                .setGenre(genreField.getText())
                .setYear(year)
                .setDuration(track.getDurationInSeconds());
            
            // --- NUOVO: Aggiungi i tag al builder se le checkbox sono spuntate ---
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
            
            // Rimettiamo la view in modalità lettura dopo aver salvato
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
}