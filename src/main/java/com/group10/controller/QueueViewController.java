package com.group10.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.group10.service.factory.TrackUIComponentFactory;
import com.group10.controller.track.TrackUIComponentItem;
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
        System.out.println(">>> QueueView initialize PARTITO");
        System.out.println(">>> root = " + root);
        System.out.println(">>> container = " + container);

        PlaybackEngine.getInstance().addSubscriber(this);
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
        //if (container.getChildren().size() > 1) {
        //    container.getChildren().remove(1, container.getChildren().size());
        //}

        List<TrackComponent> queue = PlaybackEngine.getInstance().getQueue();

        if (queue.isEmpty()) {
            container.getChildren().add(new Label("La coda è vuota"));
            return;
        }

        TrackUIComponentFactory factory = new TrackUIComponentFactory();
        for (TrackComponent t : queue) {
            TrackUIComponentItem item = (TrackUIComponentItem) factory.createUIComponentItem(t);
            container.getChildren().add(item.getRoot());
        }
        System.out.println(">>> container figli = " + container.getChildren().size()
                + ", queue = " + queue.size());
    }

    private void buildQueue() {
        if (container.getChildren().size() > 1) {
            container.getChildren().remove(1, container.getChildren().size());
        }

       List<TrackComponent> queue = PlaybackEngine.getInstance().getQueue();

        if (queue.isEmpty()) {
            container.getChildren().add(new Label("La coda è vuota"));
            return;
        }

        TrackUIComponentFactory factory = new TrackUIComponentFactory();
        for (TrackComponent t : queue) {
            TrackUIComponentItem item = (TrackUIComponentItem) factory.createUIComponentItem(t);
            container.getChildren().add(item.getRoot());
        }
        System.out.println(">>> container figli = " + container.getChildren().size()
                + ", queue = " + queue.size());
    }

    public Parent getRoot() {
        return root;
    }
}