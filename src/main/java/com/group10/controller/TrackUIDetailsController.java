/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.group10.controller;

/**
 * FXML Controller class
 *
 * @author group10
 * 
 * è il Controllerr della View di dettaglio del brano
 */
 
import com.group10.model.TrackComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.function.Consumer;

public class TrackUIDetailsController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label durationLabel;

    @FXML
    private Label genreLabel;

    @FXML
    private Label yearLabel;

    private TrackComponent track;
    private Consumer<TrackComponent> onEditListener;
    private Consumer<TrackComponent> onDeleteListener;

    public void setTrackData(TrackComponent track, Consumer<TrackComponent> onEdit, Consumer<TrackComponent> onDelete) {
        this.track = track;
        this.onEditListener = onEdit;
        this.onDeleteListener = onDelete;

        if (track != null) {
            titleLabel.setText(track.getTitle());
            authorLabel.setText(track.getAuthor());
            durationLabel.setText(track.getDurationInSeconds() + "s");
            genreLabel.setText(track.getGenre());
            yearLabel.setText(String.valueOf(track.getYear()));
        }
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
}