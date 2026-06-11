package com.group10.controller.track;

import com.group10.controller.MainViewController;
import com.group10.controller.command.CommandManager;
import com.group10.controller.command.DeleteTrackCommand;
import com.group10.controller.command.UpdateTrackCommand;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * Controller della View di dettaglio del brano
 * @author group10
 */
public class TrackUIDetailsController implements AbstractUIComponent, Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView trackImageView;
    @FXML
    private TextField titleField;
    @FXML
    private TextField artistField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField durationField;
    @FXML
    private Button btnLeft;
    @FXML
    private Button btnRight;
    @FXML
    private Label sectionTitle;

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
        
        TextField[] fields = {titleField, artistField, genreField, yearField, durationField};
        
        // 1. Gestione dei TextField
        for (TextField field : fields) {
            field.requestLayout();
        }
        /*javafx.scene.paint.Color antracite = javafx.scene.paint.Color.web("#37474F");
        javafx.scene.paint.Color ottanio = javafx.scene.paint.Color.web("#00BFA5");
        
        if (titleField != null) titleField.setTextFill(antracite);
        if (artistField != null) artistField.setTextFill(ottanio);
        if (genreField != null) genreField.setTextFill(antracite);
        if (yearField != null) yearField.setTextFill(antracite);
        if (durationField != null) durationField.setTextFill(antracite);*/
    }
    
    @FXML
    private void handleLeftAction(ActionEvent event) {
        if(!isEditing) {
            //è il tasto Modifica che deve attivare le modifiche 
            isEditing = true;
            updateUIState();
        } else {
            //è il tasto Salva Modifiche che deve salvare le modifiche
            System.out.println("init"+track.toString());
            saveTrackDetails(); //chiama un comando notifyAll che fa ricaricare la pagina
        }
    }

    @FXML
    private void handleRightAction(ActionEvent event) {
        if(!isEditing) {
            //è il tasto Elimina che deve eliminare la traccia
            //mostra alert
            //se conferma si chiude questa view è il brano viene eliminato da qui con MusicCatalogue.getInstance().removeTrack(track):
            if (MainViewController.getInstance().showDeleteConfirmation(track.getTitle())) {
                CommandManager.getInstance().executeCommand(new DeleteTrackCommand(track));
                displayTrackDetails();
                isEditing = false;
                updateUIState();
            }
        } else {
            //è il tasto Annulla Modifiche che deve ripristinare il brano
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
        //impostazione della schermata
        if (isEditing) {
            sectionTitle.setText("Modifica traccia");
            btnLeft.setText("Salva");
            btnRight.setText("Annulla");
        } else {
            sectionTitle.setText("Dettaglio brano");
            btnLeft.setText("Modifica");
            btnRight.setText("Elimina");
        }
        
        //campi che possono essere modificabili
        TextField[] editableFields = {titleField, artistField, genreField, yearField};

        for (TextField field : editableFields) {
            field.setEditable(isEditing);

            if (isEditing) {
                //se in modalità modifica
                field.setStyle(
                    "-fx-background-color: white; " +
                    "-fx-border-color: #bdc3c7; " +
                    "-fx-border-radius: 4; " +
                    "-fx-background-radius: 4; " +
                    "-fx-padding: 4;"
                );
            } else {
                //se in modalità sola lettura
                field.setStyle(
                    "-fx-background-color: transparent; " +
                    "-fx-border-color: transparent; " +
                    "-fx-padding: 0;"
                );
            }
        }

        //modifica sul text fied della durata
        if (durationField != null) {
            durationField.setEditable(false);
            durationField.setMouseTransparent(true);
            durationField.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-border-color: transparent; " +
                "-fx-padding: 0;"
            );
        }
    }
    
    private void saveTrackDetails() {
        try {
            int year = Integer.parseInt(yearField.getText().trim());
            //int duration = Integer.parseInt(durationField.getText().trim());
            
            TrackBuilder tb = new TrackBuilder()
                .setTitle(titleField.getText())
                .setAuthor(artistField.getText())
                .setGenre(genreField.getText())
                .setYear(year)
                .setDuration(track.getDurationInSeconds());
                        
            //creazione e validazione della traccia non nuovi parametri
            TrackComponent updatedTrack = tb.build();
            //esecuzione del comando modifica traccia mediante sostituzione
            CommandManager.getInstance().executeCommand(new UpdateTrackCommand(this.track, updatedTrack));
            //in execute() del command viene fatta una notifySubscriber che in MainViewController.update() aggiornra questa view
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Impossibile salvare la modifica");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}