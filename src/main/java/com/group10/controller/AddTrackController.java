package com.group10.controller;

import com.group10.model.TrackBuilder;
import com.group10.model.TrackComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddTrackController {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField durationField;
    @FXML private TextField genreField;
    @FXML private TextField yearField;
    @FXML private Label errorLabel;

    @FXML
    private void handleSave() {
        errorLabel.setVisible(false);

        try {
            String title    = titleField.getText().trim();
            String author   = authorField.getText().trim();
            String genre    = genreField.getText().trim();
            String durText  = durationField.getText().trim();
            String yearText = yearField.getText().trim();

            // Parsing durata
            int duration = 0;
            if (!durText.isEmpty()) {
                duration = Integer.parseInt(durText);
            }

            // Parsing anno (opzionale, usa default 2026 se vuoto)
            TrackBuilder builder = new TrackBuilder()
                    .title(title)
                    .author(author)
                    .duration(duration)
                    .genre(genre);

            if (!yearText.isEmpty()) {
                builder.year(Integer.parseInt(yearText));
            }

            TrackComponent track = builder.build();

            // TODO T4/T5: aggiungere track alla MusicLibrary
            System.out.println("Traccia creata: " + track.getTitle() + " - " + track.getAuthor());

            closeWindow();

        } catch (NumberFormatException e) {
            showError("Durata e Anno devono essere numeri interi.");
        } catch (IllegalStateException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void closeWindow() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}