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

    @FXML private AnchorPane root; // AGGIUNTO: Riferimento al nodo radice dell'FXML
    @FXML private ImageView trackImageView;
    @FXML private Label titleLabel;
    @FXML private Label artistLabel; // Coordinato con l'FXML
    @FXML private Label genreLabel;
    @FXML private Label yearLabel;
    @FXML private Label durationLabel;

    private TrackComponent track;
    
    // Consumatori per modifiche future (US11 / Epic Bassa priorità)
    private Consumer<TrackComponent> onEditListener;
    private Consumer<TrackComponent> onDeleteListener;
    
    // Costruttore standard
    public TrackUIDetailsController(TrackComponent track) {
        this.track = track;
    }
    
    // Costruttore polimorfico basato sull'interfaccia Playable
    public TrackUIDetailsController(Playable t) {        
        if (!(t instanceof TrackComponent)) {
            throw new RuntimeException("Impossibile visualizzare il dettaglio: il componente non è una traccia.");
        } else {
            this.track = (TrackComponent) t;
        }
    }

    /**
     * Inizializzazione automatica di JavaFX.
     * Qui inseriamo il popolamento dei dati all'avvio del componente FXML.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (this.track != null) {
            displayTrackDetails();
        }
    }

    /**
     * Consente l'aggiornamento dinamico o l'iniezione tardiva dei listener esterni
     */
    public void setTrackData(TrackComponent track, Consumer<TrackComponent> onEdit, Consumer<TrackComponent> onDelete) {
        this.track = track;
        this.onEditListener = onEdit;
        this.onDeleteListener = onDelete;

        if (this.track != null && titleLabel != null) {
            displayTrackDetails();
        }
    }

    /**
     * Funzione di utility interna per iniettare i valori del Modello nelle Label grafiche
     */
    private void displayTrackDetails() {
        titleLabel.setText(track.getTitle());
        
        // CORRETTO: Usa artistLabel (attributo FXML) e invoca il metodo corretto del modello track
        artistLabel.setText(track.getAuthor()); 
        
        // Formattazione pulita per la durata
        long totalSeconds = track.getDurationInSeconds();
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        durationLabel.setText(String.format("%02d:%02d", minutes, seconds));
        
        genreLabel.setText(track.getGenre());
        yearLabel.setText(String.valueOf(track.getYear()));
        
        // Qui potrai inserire la logica di caricamento immagine se presente
        // es. trackImageView.setImage(new Image(...));
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
        return root; // Ora compila correttamente perché 'root' è iniettato da @FXML
    }
}