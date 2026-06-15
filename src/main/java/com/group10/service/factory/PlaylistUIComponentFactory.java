/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.service.factory;

import com.group10.controller.common.AbstractUIComponent;
import com.group10.controller.playlist.*;
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


public class PlaylistUIComponentFactory implements UIComponentFactory{

    @Override
    public AbstractUIComponent createUIComponentItem(Playable model) {
        //1.preparo la view
        String fxmlPath = "/com/group10/view/PlaylistItem.fxml"; 
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        //2.istanzio il controller
        AbstractUIComponent controller = new PlaylistUIComponentItem(model);
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
            throw new RuntimeException("Errore nel caricamento della View comune dell'Item", e);
        }
    }

    @Override
    public AbstractUIComponent createUIComponentCard(Playable model) {
        //1.preparo la view
        String fxmlPath = "/com/group10/view/Card.fxml"; 
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        //2.istanzio il controller
        AbstractUIComponent controller = new PlaylistUIComponentCard(model);
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
            throw new RuntimeException("Errore nel caricamento della Playlist Card", e);
        }
    }

    @Override
    public AbstractUIComponent createUIComponentDetails(Playable model) {
        //1.preparo la view
        String fxmlPath = "/com/group10/view/PlaylistDetailsView.fxml"; 
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        //2.istanzio il controller
        AbstractUIComponent controller = new PlaylistUIDetailsController(model);
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
            throw new RuntimeException("Errore nel caricamento della PlaylistDetailsView", e);
        }
    }

    @Override
    public AbstractUIComponent createUIComponentAdder() {
        
        //1.preparo la view
        String fxmlPath = "/com/group10/view/PlaylistAdderView.fxml"; 
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        //2.istanzio il controller
        AbstractUIComponent controller = new PlaylistUIAdderController();
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
            throw new RuntimeException("Errore nel caricamento della PlaylistAdderView", e);
        }
    }

    @Override
    public AbstractUIComponent createUIComponentOptions(Playable model) {
        //1.preparo la view
        String fxmlPath = "/com/group10/view/PlaylistOptions.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        //2.istanzio il controller
        AbstractUIComponent controller = new PlaylistUIOptionsController(model);
        //3.associo controller e view
        loader.setController(controller);
        //4.carico la view
        try {
            Parent view = loader.load();
            return controller;
        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento della View comune della Card", e);
        }    }
}