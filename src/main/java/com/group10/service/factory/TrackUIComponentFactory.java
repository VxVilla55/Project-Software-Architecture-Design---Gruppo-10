package com.group10.service.factory;

import com.group10.controller.common.AbstractUIComponent;
import com.group10.controller.track.TrackUIOptionsController;
import com.group10.controller.track.TrackUIQueueComponentItem;
import com.group10.controller.track.TrackUIComponentCard;
import com.group10.controller.track.TrackUIComponentItem;
import com.group10.controller.track.TrackUIAdderController;
import com.group10.controller.track.TrackUIDetailsController;
import com.group10.model.common.Playable;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

/**
 * PATTERN: Abstract Factory.
 *
 * Ruolo: ConcreteFactory. Produce la famiglia di componenti UI (prodotti concreti)
 * relativi a una Track. Ogni prodotto e' un  AbstractUIComponent (AbstractProduct).
 *
 * @author group10
 */
public class TrackUIComponentFactory implements UIComponentFactory {

    @Override
    public AbstractUIComponent createUIComponentItem(Playable model) {
        return load("/com/group10/view/TrackItem.fxml", new TrackUIComponentItem(model));
    }

    // prodotto specifico della famiglia Track, usato dalla coda di riproduzione:
    // non fa parte del contratto UIComponentFactory (la famiglia Playlist non ha un equivalente)
    public AbstractUIComponent createUIQueueComponentItem(Playable model) {
        return load("/com/group10/view/TrackItemQueue.fxml", new TrackUIQueueComponentItem(model));
    }

    @Override
    public AbstractUIComponent createUIComponentCard(Playable model) {
        return load("/com/group10/view/Card.fxml", new TrackUIComponentCard(model));
    }

    @Override
    public AbstractUIComponent createUIComponentDetails(Playable model) {
        return load("/com/group10/view/TrackDetailsView.fxml", new TrackUIDetailsController(model));
    }

    @Override
    public AbstractUIComponent createUIComponentAdder() {
        return load("/com/group10/view/TrackAdderView.fxml", new TrackUIAdderController());
    }

    @Override
    public AbstractUIComponent createUIComponentOptions(Playable model) {
        return load("/com/group10/view/TrackOptions.fxml", new TrackUIOptionsController(model));
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
