/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group10.controller;

import com.group10.controller.common.AbstractUIComponent;
import com.group10.model.common.Playable;
import javafx.fxml.Initializable;

/**
 *
 * @author group10
 * 
 * AbstractFactory nel pattern Factory delle componenti UI per gli elementi di liste come Playlist e Track
 */

public interface UIComponentFactory {
    
    AbstractUIComponent createUIComponentItem(Playable model);
    
    AbstractUIComponent createUIComponentCard(Playable model); 
    
    AbstractUIComponent createUIComponentDetails(Playable model); 
    
    AbstractUIComponent createUIComponentAdder();
    
    AbstractUIComponent createUIComponentOptions(Playable model);
}