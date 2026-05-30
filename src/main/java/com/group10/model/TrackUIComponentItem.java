/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.group10.model;

/**
 * FXML Controller class
 *
 * @author group10
 * 
 * è una concrete factory del Controller dell'Item.fxml che mostra i dettagli della traccia
 */
 
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

public class TrackUIComponentItem extends AbstractUIComponentItem{
    @FXML
    private Label ItemPlace1;
    @FXML
    private Label ItemPlace2;
    @FXML
    private Label ItemPlace3;
    
    private TrackComponent track;
    
    TrackUIComponentItem() {
        track = new TrackBuilder()
            .setTitle("Titolo")
            .setAuthor("Autore")
            .setDuration(5)
            //.genre("Rock") //per il momento ommettiamo perché opzionale
            .setYear(2026)
            .build();
        
        
        System.out.println(track);
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("Item.fxml")
        );
        
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            //carica effettivamente la grafica FXML
            loader.load();
            //this.setStyle("-fx-background-color: red;"); // Gli dà uno sfondo rosso per vederlo
            //this.setPrefHeight(100); // Forza l'altezza a 100 pixel
            //this.setPrefWidth(200);  // Forza la larghezza a 200 pixel
        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento del file FXML", e);
        }
        
        //popola i campi
        ItemPlace1.setText(track.getTitle());
        ItemPlace2.setText(track.getGenre());
        ItemPlace3.setText(String.valueOf(track.getYear()));
    }
    
    public TrackUIComponentItem(Playable t) {
        TrackComponent track;
        if (!(t instanceof TrackComponent)) {
            throw new RuntimeException("Impossibile crearne card.");
        }
        else {
            track = (TrackComponent) t;
        }
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("Item.fxml")
        );
        
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            //carica effettivamente la grafica FXML
            loader.load();
            //this.setStyle("-fx-background-color: red;"); // Gli dà uno sfondo rosso per vederlo
            //this.setPrefHeight(100); // Forza l'altezza a 100 pixel
            //this.setPrefWidth(200);  // Forza la larghezza a 200 pixel
        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento del file FXML", e);
        }
        
        // popola i campi
        ItemPlace1.setText(track.getTitle());
        ItemPlace2.setText(track.getGenre());
        ItemPlace3.setText(String.valueOf(track.getYear()));
    }
}
