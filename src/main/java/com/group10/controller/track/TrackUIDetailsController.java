package com.group10.controller.track;

import com.group10.controller.common.AbstractUIDetailsController;
import com.group10.model.common.Playable;
import com.group10.model.TrackComponent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.util.function.Consumer;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * Controller della View di dettaglio del brano
 * @author group10
 */
public class TrackUIDetailsController extends AbstractUIDetailsController {

    @FXML private AnchorPane root; 
    @FXML private ImageView trackImageView;
    @FXML private Label titleLabel;
    @FXML private Label artistLabel; 
    @FXML private Label genreLabel;
    @FXML private Label yearLabel;
    @FXML private Label durationLabel;

    private TrackComponent track;
    
    private Consumer<TrackComponent> onEditListener;
    private Consumer<TrackComponent> onDeleteListener;
    
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
    // 🔴 FORZATURA ESTREMA PER TEST: Se i testi diventano neri, il codice risponde!
    if (titleLabel != null) titleLabel.setStyle("-fx-text-fill: #37474F !important;");
    if (genreLabel != null) genreLabel.setStyle("-fx-text-fill: #37474F !important;");
    if (yearLabel != null) yearLabel.setStyle("-fx-text-fill: #37474F !important;");
    if (durationLabel != null) durationLabel.setStyle("-fx-text-fill: #37474F !important;");
    
    if (this.track != null) {
        displayTrackDetails();
    }
}

    public void setTrackData(TrackComponent track, Consumer<TrackComponent> onEdit, Consumer<TrackComponent> onDelete) {
        this.track = track;
        this.onEditListener = onEdit;
        this.onDeleteListener = onDelete;

        if (this.track != null && titleLabel != null) {
            displayTrackDetails();
        }
    }

    private void displayTrackDetails() {
        titleLabel.setText(track.getTitle());
        artistLabel.setText(track.getAuthor()); 
        genreLabel.setText(track.getGenre());
        yearLabel.setText(String.valueOf(track.getYear()));
        
        long totalSeconds = track.getDurationInSeconds();
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        durationLabel.setText(String.format("%02d:%02d", minutes, seconds));

        // 🛠️ PROTEZIONE RUNTIME: Forza i colori corretti via codice bypassando i temi Windows/Mac
        javafx.scene.paint.Color antracite = javafx.scene.paint.Color.web("#37474F");
        javafx.scene.paint.Color ottanio = javafx.scene.paint.Color.web("#00BFA5");
        
        if (titleLabel != null) titleLabel.setTextFill(antracite);
        if (artistLabel != null) artistLabel.setTextFill(ottanio);
        if (genreLabel != null) genreLabel.setTextFill(antracite);
        if (yearLabel != null) yearLabel.setTextFill(antracite);
        if (durationLabel != null) durationLabel.setTextFill(antracite);
    }

    @FXML
    private void handleEdit() {
        if (onEditListener != null && track != null) {
            onEditListener.accept(track);
        }
    }

    @FXML
    private void handleDelete() {
        if (onDeleteListener != null && track != null) {
            onDeleteListener.accept(track);
        }
    }

    @Override
    public Parent getRoot() {
        return root; 
    }
}