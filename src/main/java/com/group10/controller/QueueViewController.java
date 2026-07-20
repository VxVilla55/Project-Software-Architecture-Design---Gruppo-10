package com.group10.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.group10.service.factory.TrackUIComponentFactory;
import com.group10.controller.track.TrackUIComponentItem;
import com.group10.controller.track.TrackUIQueueComponentItem;
import com.group10.model.TrackComponent;
import com.group10.model.common.Subscriber;
import com.group10.model.state.PlaybackEngine;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class QueueViewController implements Initializable, Subscriber {

    @FXML private AnchorPane root;
    @FXML private VBox container;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PlaybackEngine.getInstance().addSubscriber(this);

        //quando la view esce di scena si disiscrive, per non restare in ascolto inutilmente
        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) {
               PlaybackEngine.getInstance().removeSubscriber(this);
            }
        });
        update();
    }

    @Override
    public void update() {
        container.getChildren().clear();

        List<TrackComponent> queue = PlaybackEngine.getInstance().getQueue();

        if (queue.isEmpty()) {
            container.getChildren().add(new Label("La coda è vuota"));
            return;
        }

        TrackUIComponentFactory factory = new TrackUIComponentFactory();
        for (TrackComponent t : queue) {
            TrackUIQueueComponentItem item = (TrackUIQueueComponentItem) factory.createUIQueueComponentItem(t);
            container.getChildren().add(item.getRoot());
        }
    }

    public Parent getRoot() {
        return root;
    }
}