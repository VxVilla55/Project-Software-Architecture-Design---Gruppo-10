/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.group10.controller.track;

/**
 * FXML Controller class
 *
 * @author group10
 * 
 * è il Controllerr della View di dettaglio del brano
 */
 
import com.group10.controller.common.AbstractUIDetailsController;
import com.group10.model.common.Playable;
import com.group10.model.TrackComponent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.function.Consumer;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class TrackUIDetailsController extends AbstractUIDetailsController{

    @FXML
    private AnchorPane root;
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
    
    public TrackUIDetailsController (TrackComponent track) {
        this.track = track;
    }
    
    public TrackUIDetailsController (Playable t) {        
        if (!(t instanceof TrackComponent)) {
            throw new RuntimeException("Impossibile crearne card.");
        }
        else {
            track = (TrackComponent) t;
        }
    }
    //più in là
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}