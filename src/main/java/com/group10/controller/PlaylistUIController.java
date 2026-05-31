/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group10.controller;


import com.group10.model.PlaylistComponent;
import com.group10.model.TrackBuilder;
import com.group10.model.TrackComponent;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author group10
 * 
 * product del pattern FACTORY
 */
public class PlaylistUIController{

    @FXML
    private AnchorPane anchorPane;
    //@FXML
    //private Button addTrack;
    @FXML
    private VBox vbox;
    
    private UIComponentFactory factory;
    
    private PlaylistComponent playlist;
    
    private final String viewPath =  "/com/group10/view/PlaylistUIComponent.fxml";
    private Parent view = null;
    
    public PlaylistUIController(PlaylistComponent playlist) {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(viewPath)
        );
        
        //loader.setRoot(this);
        loader.setController(this);
        
        try {
            //carica effettivamente la grafica FXML
            view = loader.load();
        } catch (IOException e) {
            System.err.println("Path della view errato: " + viewPath);
        }
        
        //uso la factory per creare gli elementi item per ogni traccia della playlist
        factory = new TrackUIComponentFactory();
        TrackUIComponentItem item;
        
        //DA SOSTITUIRE SE USIAMO ITERATOR PER PLAYLIST
        for(TrackComponent t: playlist.getTracks()) {
            item = (TrackUIComponentItem) factory.createUIComponentItem(t);
            vbox.getChildren().add(item);
        }
    }
    
    public Parent getView() {
        return view;
    }
    
    //DA RIMUOVERE APPENA METTIAMO TUTTO ASSIEME: IL TASTO DI AGGIUNTA TRACCIA NON LO METTIAMO QUI.
    @FXML
    private void addTrack(ActionEvent event) {
        factory = new TrackUIComponentFactory();
        
        
        TrackUIComponentItem item;
        
        item = (TrackUIComponentItem) factory.createUIComponentItem(new TrackBuilder()
                .setTitle("Titolo")
                .setAuthor("Autore")
                .setGenre("Genere")
                .setYear(2026)
                .build());
        
        
        vbox.getChildren().add(item);
        
        //vbox.setStyle("-fx-background-color: blue; -fx-border-color: yellow; -fx-border-width: 5;");
        
        System.out.println(vbox.getChildren().toString());
    }
    
}
