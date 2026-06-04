package com.group10.controller.track;

import com.group10.controller.MainViewController;
import com.group10.controller.common.AbstractUIAdderController;
import com.group10.model.MusicCatalogue;
import com.group10.model.builder.TrackBuilder;
import com.group10.model.TrackComponent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class TrackUIAdderController extends AbstractUIAdderController {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField durationField;
    @FXML private TextField genreField;
    @FXML private TextField yearField;
    @FXML private Label errorLabel;
    @FXML private AnchorPane root;
    
    
    public TrackUIAdderController() {
    }
    
    @Override
    public Parent getRoot() {
        return root;
    }

    @FXML
    private void handleSave() {
        errorLabel.setVisible(false);

        try {
            String title    = titleField.getText().trim();
            String author   = authorField.getText().trim();
            String genre    = genreField.getText().trim();
            String durText  = durationField.getText().trim();
            String yearText = yearField.getText().trim();

            
            int duration = 0;
            if (!durText.isEmpty()) {
                duration = Integer.parseInt(durText);
            }

            
            TrackBuilder builder = new TrackBuilder()
                    .setTitle(title)
                    .setAuthor(author)
                    .setDuration(duration)
                    .setGenre(genre);

            if (!yearText.isEmpty()) {
                builder.setYear(Integer.parseInt(yearText));
            }

            TrackComponent track = builder.build();

            MusicCatalogue.getInstance().addTrack(track);
            MainViewController.getInstance().closePopup();

        } catch (NumberFormatException e) {
            showError("Durata e Anno devono essere numeri interi.");
        } catch (IllegalStateException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        MainViewController.getInstance().closePopup();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}