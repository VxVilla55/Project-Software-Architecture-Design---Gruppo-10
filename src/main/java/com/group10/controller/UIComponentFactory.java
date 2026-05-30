/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group10.controller;

import com.group10.model.Playable;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author group10
 * 
 * AbstractFactory nel pattern Factory delle componenti UI per gli elementi di liste come Playlist e Track
 */

public interface UIComponentFactory {
    
    //AnchorPane createUIComponentCard(Playable model); 
    
    AnchorPane createUIComponentItem(Playable model); 
}