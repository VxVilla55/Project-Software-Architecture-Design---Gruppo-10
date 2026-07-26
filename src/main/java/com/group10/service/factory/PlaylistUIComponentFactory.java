package com.group10.service.factory;

import com.group10.controller.common.AbstractUIComponent;
import com.group10.controller.playlist.*;
import com.group10.model.common.Playable;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

/**
 * PATTERN: Abstract Factory.
 *
 * Ruolo: ConcreteFactory. Produce la famiglia di componenti UI (prodotti concreti)
 * relativi a una Playlist. Ogni prodotto e' un AbstractUIComponent (AbstractProduct).
 *
 * @author group10
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

    /**
     * Carica la view FXML associandole il controller passato e lo restituisce.
     * Il {@code loader.load()} esegue l'initialize() del controller, che popola le label dal model.
     */
    private AbstractUIComponent load(String fxmlPath, AbstractUIComponent controller) {
        //preparo la view
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        //associo controller e view
        loader.setController(controller);
        try {
            loader.load();
            return controller;
        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento della view: " + fxmlPath, e);
        }
    }
}
