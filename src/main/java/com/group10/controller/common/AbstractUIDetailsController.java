/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.controller.common;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

/**
 *
 * @author group10
 */
public abstract class AbstractUIDetailsController implements Initializable {
    /**
     * Initializes the controller class.
     */
    @Override
    public abstract void initialize(URL url, ResourceBundle rb);   
    
    public abstract Parent getRoot();
}
