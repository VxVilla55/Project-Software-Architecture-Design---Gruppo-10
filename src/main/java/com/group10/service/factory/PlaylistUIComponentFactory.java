package com.group10.service.factory;

import com.group10.controller.common.AbstractUIComponent;
import com.group10.controller.playlist.*;
import com.group10.model.common.Playable;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

/**
 *
 * @author group10
 * PATTERN: Abstract Factory. ConcreteFactory, produce la famiglia di componenti UI
 * (prodotti concreti) relativi a una Playlist.
 */
public class PlaylistUIComponentFactory implements UIComponentFactory {

    @Override
    public AbstractUIComponent createUIComponentItem(Playable model) {
        return load("/com/group10/view/PlaylistItem.fxml", new PlaylistUIComponentItem(model));
    }

    @Override
    public AbstractUIComponent createUIComponentCard(Playable model) {
        return load("/com/group10/view/Card.fxml", new PlaylistUIComponentCard(model));
    }

    @Override
    public AbstractUIComponent createUIComponentDetails(Playable model) {
        return load("/com/group10/view/PlaylistDetailsView.fxml", new PlaylistUIDetailsController(model));
    }

    @Override
    public AbstractUIComponent createUIComponentAdder() {
        return load("/com/group10/view/PlaylistAdderView.fxml", new PlaylistUIAdderController());
    }

    @Override
    public AbstractUIComponent createUIComponentOptions(Playable model) {
        return load("/com/group10/view/PlaylistOptions.fxml", new PlaylistUIOptionsController(model));
    }

    // carica la view fxml, ci associa il controller passato e lo ritorna.
    // loader.load() esegue anche l'initialize() del controller (popola le label dal model)
    private AbstractUIComponent load(String fxmlPath, AbstractUIComponent controller) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setController(controller);
        try {
            loader.load();
            return controller;
        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento della view: " + fxmlPath, e);
        }
    }
}
