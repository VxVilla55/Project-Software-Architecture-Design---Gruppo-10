package com.group10.controller.track;

import com.group10.controller.MainViewController;
import com.group10.controller.common.AbstractUIComponent;
import com.group10.model.MusicCatalogue;
import com.group10.model.builder.TrackBuilder;
import com.group10.model.TrackComponent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class TrackUIAdderController implements AbstractUIComponent, Initializable {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField durationField;
    @FXML private TextField genreField;
    @FXML private TextField yearField;
    @FXML private Label errorLabel;
    @FXML private AnchorPane root;
    @FXML private ImageView coverPreview;
    @FXML private Button pickImageButton;
    @FXML private Label coverNameLabel;

    private String selectedCoverPath = null;

    public TrackUIAdderController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDefaultCover();
    }

    public Parent getRoot() {
        return root;
    }

    @FXML
    private void handlePickImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Scegli immagine di copertina");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.webp")
        );
        File file = chooser.showOpenDialog(root.getScene().getWindow());
        if (file != null) {
            selectedCoverPath = copyToCoversFolder(file);
            if (selectedCoverPath != null) {
                coverPreview.setImage(new Image(file.toURI().toString()));
                coverNameLabel.setText(file.getName());
            }
        }
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
                    .setGenre(genre)
                    .setCoverImagePath(selectedCoverPath);

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

    private void loadDefaultCover() {
        try {
            Image defaultImg = new Image(getClass().getResourceAsStream("/com/group10/images/covers/default-cover.png"));
            coverPreview.setImage(defaultImg);
        } catch (Exception e) {
            System.err.println("Errore nel caricamento della cover di default: " + e.getMessage());
        }
    }

    // copia il file scelto dall'utente in data/covers con nome univoco
    // e retsituisce il path relativo
    private String copyToCoversFolder(File source) {
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