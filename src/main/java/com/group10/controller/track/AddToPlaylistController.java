package com.group10.controller.track;

import com.group10.model.MusicCatalogue;
import com.group10.model.PlaylistComponent;
import com.group10.model.TrackComponent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AddToPlaylistController implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private Label trackTitleLabel;
    @FXML
    // Sostituita la ComboBox con la ListView per gestire la selezione multipla (Task T6.7)
    private ListView<String> playlistListView; 
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    private TrackComponent selectedTrack;
    private MusicCatalogue catalogue;
    private Parent view = null;

    // Questa mappa associa il nome di ogni playlist a un valore booleano (selezionata o meno)
    private Map<String, BooleanProperty> itemStates = new HashMap<>();

    /**
     * Il costruttore riceve la traccia da aggiungere e carica l'FXML
     */
    public AddToPlaylistController(TrackComponent track) {
        this.selectedTrack = track;
        this.catalogue = MusicCatalogue.getInstance();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group10/view/AddToPlaylistView.fxml"));
        loader.setController(this);
        try {
            view = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Impossibile caricare AddToPlaylistView.fxml", e);
        }
    }

    public Parent getRoot() {
        return view;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Mostriamo dinamicamente il titolo del brano che si sta salvando
        trackTitleLabel.setText("Aggiungi \"" + selectedTrack.getTitle() + "\" a:");

        // 1. Diciamo alla ListView di iniettare una Checkbox accanto al nome di ogni playlist
        playlistListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            return itemStates.get(item);
        }));

        // 2. Popoliamo la ListView e la mappa degli stati leggendo dal MusicCatalogue
        if (catalogue.getPlaylists() != null) {
            for (PlaylistComponent playlist : catalogue.getPlaylists()) {
                String playlistName = playlist.getName();
                
                // Di base, le caselle partono tutte deselezionate (false)
                itemStates.put(playlistName, new SimpleBooleanProperty(false));
                
                // Aggiungiamo il nome della playlist nella lista visibile
                playlistListView.getItems().add(playlistName);
            }
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closePopup();
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        boolean alMenoUnaAggiunta = false;

        // Cicliamo sulla mappa per verificare quali playlist l'utente ha spuntato
        for (Map.Entry<String, BooleanProperty> entry : itemStates.entrySet()) {
            String playlistName = entry.getKey();
            boolean isChecked = entry.getValue().get();

            // Se la casella ha la spunta (true), procediamo all'inserimento
            if (isChecked) {
                // Cerchiamo l'oggetto PlaylistComponent corrispondente nel catalogo
                PlaylistComponent targetPlaylist = null;
                for (PlaylistComponent p : catalogue.getPlaylists()) {
                    if (p.getName().equals(playlistName)) {
                        targetPlaylist = p;
                        break;
                    }
                }

                // Task T6.4: Aggiunge la traccia usando il tuo metodo .add() corretto
                if (targetPlaylist != null) {
                    targetPlaylist.add(selectedTrack); // Modificato in .add() come richiesto!
                    System.out.println("Traccia '" + selectedTrack.getTitle() + "' aggiunta alla playlist '" + playlistName + "'!");
                    alMenoUnaAggiunta = true;
                }
            }
        }

        // Se è stata aggiornata almeno una playlist, notifichiamo gli osservatori (Task T4.8 / T5.5)
        if (alMenoUnaAggiunta) {
            catalogue.notifySubscribers();
        }

        closePopup();
    }

    /**
     * Sfrutta la struttura del vostro StackPane per rimuovere l'ultimo layer inserito (il popup)
     * e togliere l'effetto sfocatura dalla schermata principale.
     */
    private void closePopup() {
        if (root != null && root.getParent() != null) {
            // Risale fino allo StackPane di root principale
            StackPane mainRoot = (StackPane) root.getParent().getParent(); 
            if (mainRoot.getChildren().size() > 1) {
                mainRoot.getChildren().remove(mainRoot.getChildren().size() - 1); // Rimuove il layer di popup
                mainRoot.getChildren().get(0).setEffect(null); // Toglie il GaussianBlur
            }
        }
    }
}