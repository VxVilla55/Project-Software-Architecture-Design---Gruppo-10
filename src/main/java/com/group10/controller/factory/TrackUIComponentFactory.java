/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.controller.factory;

import com.group10.controller.common.AbstractUIAdderController;
import com.group10.controller.common.AbstractUIComponentCard;
import com.group10.controller.common.AbstractUIComponentItem;
import com.group10.controller.common.AbstractUIDetailsController;
import com.group10.controller.common.AbstractUIOptionsComponent;
import com.group10.controller.UIComponentFactory;
import com.group10.controller.track.TrackUIOptionsController;
import com.group10.controller.track.TrackUIComponentCard;
import com.group10.controller.track.TrackUIComponentItem;
import com.group10.controller.track.TrackUIAdderController;
import com.group10.controller.track.TrackUIDetailsController;
import com.group10.model.common.Playable;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 *
 * @author group10
 * 
 * ConcreteFactory: 
 * per JavaFx forse è meglio usare 'fxmlLoader.setControllerFactory(param -> new PlaylistUIComponent(playlist);' 
 * invece di 'fxmlLoader.setControllerFactory(new PlaylistUIComponentFactory(playlistSelected));' 
 */


public class TrackUIComponentFactory implements UIComponentFactory{

    @Override
    public AbstractUIComponentItem createUIComponentItem(Playable model) {
        //1.preparo la view
        String fxmlPath = "/com/group10/view/TrackItem.fxml"; 
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        //2.istanzio il controller
        AbstractUIComponentItem controller = new TrackUIComponentItem(model);
        //3.associo controller e view
        loader.setController(controller);
        //4.carico la view
        try {
            Parent view = loader.load();
            
            //-- viene eseguito l'intialize() del controller associatogli: carichiamo i valori del model nelle label
            
            //iniettiamo la view al controller
            //controller.setViewNode(viewNode); //non serve, il controller ha già l'attributo fxml dell'elemento root
            return controller;
            
        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento della View comune della Card", e);
        }
    }

    @Override
    public AbstractUIComponentCard createUIComponentCard(Playable model) {
        //1.preparo la view
        String fxmlPath = "/com/group10/view/Card.fxml"; 
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        //2.istanzio il controller
        AbstractUIComponentCard controller = new TrackUIComponentCard(model);
        //3.associo controller e view
        loader.setController(controller);
        //4.carico la view
        try {
            Parent view = loader.load();
            
            //-- viene eseguito l'intialize() del controller associatogli: carichiamo i valori del model nelle label
            
            //iniettiamo la view al controller
            //controller.setViewNode(viewNode); //non serve, il controller ha già l'attributo fxml dell'elemento root
            return controller;
            
        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento della View comune della Card", e);
        }
    }

    @Override
    public AbstractUIDetailsController createUIComponentDetails(Playable model) {
        //1.preparo la view
        String fxmlPath = "/com/group10/view/TrackDetailsView.fxml"; 
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        //2.istanzio il controller
        AbstractUIDetailsController controller = new TrackUIDetailsController(model);
        //3.associo controller e view
        loader.setController(controller);
        //4.carico la view
        try {
            Parent view = loader.load();
            
            //-- viene eseguito l'intialize() del controller associatogli: carichiamo i valori del model nelle label
            
            //iniettiamo la view al controller
            //controller.setViewNode(viewNode); //non serve, il controller ha già l'attributo fxml dell'elemento root
            return controller;
            
        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento TrackDetailsView", e);
        }
    }

    @Override
    public AbstractUIAdderController createUIComponentAdder() {
        
        //1.preparo la view
        String fxmlPath = "/com/group10/view/TrackAdderView.fxml"; 
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        //2.istanzio il controller
        AbstractUIAdderController controller = new TrackUIAdderController();
        //3.associo controller e view
        loader.setController(controller);
        //4.carico la view
        try {
            Parent view = loader.load();
            
            //-- viene eseguito l'intialize() del controller associatogli: carichiamo i valori del model nelle label
            
            //iniettiamo la view al controller
            //controller.setViewNode(viewNode); //non serve, il controller ha già l'attributo fxml dell'elemento root
            return controller;
            
        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento della View comune della Card", e);
        }
    }

    @Override
    public AbstractUIOptionsComponent createUIComponentOptions(Playable model) {
        //1.preparo la view
        String fxmlPath = "/com/group10/view/TrackOptions.fxml"; 
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        //2.istanzio il controller
        AbstractUIOptionsComponent controller = new TrackUIOptionsController(model);
        //3.associo controller e view
        loader.setController(controller);
        //4.carico la view
        try {
            Parent view = loader.load();
            
            //-- viene eseguito l'intialize() del controller associatogli: carichiamo i valori del model nelle label
            
            //iniettiamo la view al controller
            //controller.setViewNode(viewNode); //non serve, il controller ha già l'attributo fxml dell'elemento root
            return controller;
            
        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento della View comune della Card", e);
        }
    }    
}