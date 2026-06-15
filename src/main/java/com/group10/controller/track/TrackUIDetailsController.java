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
import javafx.scene.image.ImageView;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox; // <-- NUOVO IMPORT
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    // --- NUOVO: Aggiunte le CheckBox per i tag ---
    @FXML private CheckBox favoriteCheckBox;
    @FXML private CheckBox newReleaseCheckBox;
    @FXML private CheckBox explicitCheckBox;

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
        if (favoriteCheckBox != null) {
            favoriteCheckBox.setSelected(track.hasTag(TrackComponent.Tag.FAVORITE));
        }
        if (newReleaseCheckBox != null) {
            newReleaseCheckBox.setSelected(track.hasTag(TrackComponent.Tag.NEW_RELEASE));
        }
        if (explicitCheckBox != null) {
            explicitCheckBox.setSelected(track.hasTag(TrackComponent.Tag.EXPLICIT));
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
            if (MainViewController.getInstance().showConfirmation(title, header, context) ) {
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

        // --- NUOVO: Abilita/Disabilita le checkbox dei tag in base a isEditing ---
        if (favoriteCheckBox != null) favoriteCheckBox.setDisable(!isEditing);
        if (newReleaseCheckBox != null) newReleaseCheckBox.setDisable(!isEditing);
        if (explicitCheckBox != null) explicitCheckBox.setDisable(!isEditing);
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
            if (favoriteCheckBox != null && favoriteCheckBox.isSelected()) {
                tb.addTag(TrackComponent.Tag.FAVORITE);
            }
            if (newReleaseCheckBox != null && newReleaseCheckBox.isSelected()) {
                tb.addTag(TrackComponent.Tag.NEW_RELEASE);
            }
            if (explicitCheckBox != null && explicitCheckBox.isSelected()) {
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