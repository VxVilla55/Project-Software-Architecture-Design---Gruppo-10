package com.group10.controller;

import com.group10.model.TrackBuilder;
import com.group10.model.TrackComponent;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    
    private final String viewPath  = "/com/group10/view/AddTrackView.fxml";
    private Parent view = null;
    
    public AddTrackController() {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(viewPath)
        );
        
        //loader.setRoot(this);
        loader.setController(this);
        
        try {
            //carica effettivamente la grafica FXML
            view = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Path della view errato: " + viewPath);
        }
    }

    public Parent getView() {
        return view;
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

            // Parsing durata
            int duration = 0;
            if (!durText.isEmpty()) {
                duration = Integer.parseInt(durText);
            }

            // Parsing anno (opzionale, usa default 2026 se vuoto)
            TrackBuilder builder = new TrackBuilder()
                    .setTitle(title)
                    .setAuthor(author)
                    .setDuration(duration)
                    .setGenre(genre);

            if (!yearText.isEmpty()) {
                builder.setYear(Integer.parseInt(yearText));
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