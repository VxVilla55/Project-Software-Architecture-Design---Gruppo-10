/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group10.service.factory;

import com.group10.controller.common.AbstractUIComponent;
import com.group10.model.common.Playable;
import javafx.fxml.Initializable;

/**
 *
 * @author group10
 * PATTERN: Abstract Factory (l'AbstractFactory).
 * Dichiara un metodo di creazione per ogni tipo di componente UI della famiglia (Item,
 * Card, Details, Adder, Options). Le due ConcreteFactory (TrackUIComponentFactory e
 * PlaylistUIComponentFactory) producono l'intera famiglia, ciascuna per il proprio tipo
 * di model; il prodotto astratto e' AbstractUIComponent.
 */
public interface UIComponentFactory {
    
    AbstractUIComponent createUIComponentItem(Playable model);
    
    AbstractUIComponent createUIComponentCard(Playable model); 
    
    AbstractUIComponent createUIComponentDetails(Playable model); 
    
    AbstractUIComponent createUIComponentAdder();
    
    AbstractUIComponent createUIComponentOptions(Playable model);
}