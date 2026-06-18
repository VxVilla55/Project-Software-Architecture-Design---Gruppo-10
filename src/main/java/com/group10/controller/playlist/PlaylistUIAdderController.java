package com.group10.controller.playlist;

import com.group10.controller.MainViewController; 
import com.group10.controller.common.AbstractUIComponent;
import com.group10.model.MusicCatalogue;
import com.group10.model.builder.PlaylistBuilder;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import com.group10.service.command.AddPlaylistCommand;
import com.group10.service.command.CommandManager;
import com.group10.service.filter.GenreFilterStrategy;
import com.group10.service.filter.TagFilterStrategy;
import com.group10.service.filter.YearFilterStrategy;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlaylistUIAdderController implements AbstractUIComponent, Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private TextField playlistNameInput;
    @FXML
    private CheckBox autoCreateCheckBox;
    @FXML
    private VBox autoMenuContainer;
    @FXML
    private CheckBox filterByTagCheckBox;
    @FXML
    private HBox tagOptionsContainer;
    @FXML
    private CheckBox favCheckBox;
    @FXML
    private CheckBox newCheckBox;
    @FXML
    private CheckBox explicitCheckBox;
    @FXML
    private CheckBox filterByYearCheckBox;
    @FXML
    private HBox yearOptionsContainer;
    @FXML
    private TextField yearFromInput;
    @FXML
    private TextField yearToInput;
    @FXML
    private CheckBox filterByGenreCheckBox;
    @FXML
    private ComboBox<String> genreComboBox;
    @FXML
    private Label errorLabel;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genreComboBox.setItems(FXCollections.observableArrayList(MusicCatalogue.getInstance().getGenres()) );
        hideError();
        update();
    }
    
    public void update() {
        //visualizzazione del container per la generazione automatica
        boolean isAutoCreate = autoCreateCheckBox.isSelected();
        toggleNodeVisibility(autoMenuContainer, isAutoCreate);
        
        //gestione dei filtri per la generazione al menu automatico
        if (isAutoCreate) {
            toggleNodeVisibility(tagOptionsContainer, filterByTagCheckBox.isSelected());
            toggleNodeVisibility(yearOptionsContainer, filterByYearCheckBox.isSelected());
            toggleNodeVisibility(genreComboBox, filterByGenreCheckBox.isSelected());
        } else {
            toggleNodeVisibility(tagOptionsContainer, false);
            toggleNodeVisibility(yearOptionsContainer, false);
            toggleNodeVisibility(genreComboBox, false);
        }
    }
    
    public Parent getRoot() {
        return root;
    }
    
    @FXML
    private void handleCreatePlaylist(ActionEvent event) {
        String playlistName = playlistNameInput.getText().trim();
        if (playlistName == null || playlistName.trim().isEmpty()) {
            showError("Il nome della playlist non può essere vuoto.");
        }

        PlaylistComponent playlist;
        try {
            PlaylistBuilder builder = new PlaylistBuilder()
                        .setName(playlistName);

            if (autoCreateCheckBox.isSelected()) {
                
                if (!automaticCreationValidation()) {
                    return;
                }
                if (filterByTagCheckBox.isSelected()) {
                    builder.addStrategy(new TagFilterStrategy(
                        favCheckBox.isSelected(),
                        newCheckBox.isSelected(),
                        explicitCheckBox.isSelected()
                    ));
                }
                if (filterByYearCheckBox.isSelected()) {
                    int from = yearFromInput.getText().isEmpty() ? Integer.MIN_VALUE 
                            : Integer.parseInt(yearFromInput.getText());
                    int to   = yearToInput.getText().isEmpty()   ? Integer.MAX_VALUE 
                            : Integer.parseInt(yearToInput.getText());
                    builder.addStrategy(new YearFilterStrategy(from, to));
                }
                if (filterByGenreCheckBox.isSelected()) {
                    builder.addStrategy(new GenreFilterStrategy(genreComboBox.getValue()));
                }

                
                playlist = builder.build();
            } else {
                // Playlist manuale vuota
                playlist = new PlaylistBuilder()
                        .setName(playlistName)
                        .build();
            }
            
            hideError();
            new CommandManager().executeCommand(new AddPlaylistCommand(playlist));
            playlistNameInput.clear();
            MainViewController.getInstance().closePopup();
        } catch (IllegalArgumentException ex ) {
            showError("Errore: " + ex.getMessage());
        }
    }


    @FXML
    private void handleAnnulla(ActionEvent event) {
        if (playlistNameInput != null) {
            playlistNameInput.clear();
        }
        MainViewController.getInstance().closePopup();
    }
    
    @FXML
    private void toggleAutoMenu(ActionEvent event) {
        update();
    }

    @FXML
    private void handleCriterionChange(ActionEvent event) {
    }

    @FXML
    private void handleFilterToggle(ActionEvent event) {
        update();
    }
    
    private void toggleNodeVisibility(javafx.scene.Node node, boolean isVisible) {
        node.setVisible(isVisible);
        node.setManaged(isVisible);
    }
    private void showError(String message) {
        errorLabel.setText(message);
        toggleNodeVisibility(errorLabel, true);
    }
    
    private void hideError() {
        toggleNodeVisibility(errorLabel, false);
    }

    private boolean automaticCreationValidation() {
        if (!filterByTagCheckBox.isSelected() && !filterByYearCheckBox.isSelected() && !filterByGenreCheckBox.isSelected()) {
            showError("Seleziona almeno un criterio per la generazione automatica.");
            return false;
        }
        
        // Validazione filtro tag
        if (filterByTagCheckBox.isSelected()) {
            if (!favCheckBox.isSelected() && !newCheckBox.isSelected() && !explicitCheckBox.isSelected()) {
                showError("Seleziona almeno un tag (Preferito, Novità, Esplicito).");
                return false;
            }
        }
        
        // Validazione filtro anno
        if (filterByYearCheckBox.isSelected()) {
            String fromStr = yearFromInput.getText().trim();
            String toStr = yearToInput.getText().trim();
            
            if (fromStr.isEmpty() && toStr.isEmpty()) {
                showError("Inserisci almeno un anno (Da / A) per filtrare temporaneamente.");
                return false;
            }
            
            try {
                int fromYear = fromStr.isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(fromStr);
                int toYear = toStr.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(toStr);
                
                if (fromYear < 0 || (toYear != Integer.MAX_VALUE && toYear < 0)) {
                    showError("Gli anni non possono essere valori negativi.");
                    return false;
                }
                
                if (fromYear > toYear) {
                    showError("L'anno di inizio ('Da') non può essere maggiore dell'anno di fine ('A').");
                    return false;
                }
            } catch (NumberFormatException e) {
                showError("I campi anno devono contenere solo numeri interi.");
                return false;
            }
        }
        
        // Validazione filtro genere
        if (filterByGenreCheckBox.isSelected()) {
            if (genreComboBox.getValue() == null || genreComboBox.getValue().toString().isEmpty()) {
                showError("Seleziona un genere dal menu a tendina.");
                return false;
            }
        }
        
        return true;
    }
}