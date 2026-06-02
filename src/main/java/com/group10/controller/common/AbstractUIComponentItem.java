/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.group10.controller.common;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
/**
 * FXML Controller class
 *
 * @author group10
 */
public abstract class AbstractUIComponentItem implements Initializable {
    /**
     * Initializes the controller class.
     */
    @Override
    public abstract void initialize(URL url, ResourceBundle rb);    

    public abstract Parent getRoot();
}
