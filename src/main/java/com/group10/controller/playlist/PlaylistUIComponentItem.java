package com.group10.controller.playlist;

import com.group10.controller.MainViewController;
import com.group10.controller.common.AbstractUIComponentItem;
import com.group10.controller.factory.PlaylistUIComponentFactory;
import com.group10.controller.factory.TrackUIComponentFactory;
import com.group10.model.common.Playable;
import com.group10.model.PlaylistComponent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * Controller per il singolo item playlist nella lista.
 * Collegato a PlaylistItemView.fxml
 */
public class PlaylistUIComponentItem extends AbstractUIComponentItem {

    @FXML
    private HBox root;
    @FXML
    private Label nameLabel;
    @FXML
    private Label trackCountLabel;
    @FXML
    private Button playlistMenuButton;

    private PlaylistComponent playlist;

    public PlaylistUIComponentItem(Playable playlist) {
        if (!(playlist instanceof PlaylistComponent)) {
            throw new RuntimeException("Impossibile creare l'item: il Playable non è una PlaylistComponent");
        }
        this.playlist = (PlaylistComponent) playlist;
    }

    public PlaylistUIComponentItem(PlaylistComponent playlist) {
        this.playlist = playlist;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameLabel.setText(playlist.getName());

        int size = playlist.getSize();
        trackCountLabel.setText(size == 1 ? "1 brano" : size + " brani");
    }

    @Override
    public Parent getRoot() {
        return root;
    }

    @FXML
    private void handleOptions(ActionEvent event) {
        // TODO: aprire menu contestuale playlist (es. rinomina, elimina)
    }
    
    @FXML
    private void handlePlaylistSelection(MouseEvent event) {
        PlaylistUIDetailsController c = (PlaylistUIDetailsController) new PlaylistUIComponentFactory().createUIComponentDetails(playlist);
        MainViewController.getInstance().showOnCenterPane(c.getRoot());
    }
}