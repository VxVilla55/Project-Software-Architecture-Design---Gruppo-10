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
 * PATTERN: Abstract Factory.
 *
 * Ruolo: AbstractFactory. Dichiara un metodo di creazione per ogni tipo di
 * componente UI della famiglia (Item, Card, Details, Adder, Options).
 * Ogni ConcreteFactory ({@link TrackUIComponentFactory}, {@link PlaylistUIComponentFactory})
 * produce l'intera famiglia di componenti per il proprio tipo di model.
 * Il prodotto astratto e' {@link AbstractUIComponent}.
 *
 * @author group10
 */

public interface UIComponentFactory {
    
    AbstractUIComponent createUIComponentItem(Playable model);
    
    AbstractUIComponent createUIComponentCard(Playable model); 
    
    AbstractUIComponent createUIComponentDetails(Playable model); 
    
    AbstractUIComponent createUIComponentAdder();
    
    AbstractUIComponent createUIComponentOptions(Playable model);
}