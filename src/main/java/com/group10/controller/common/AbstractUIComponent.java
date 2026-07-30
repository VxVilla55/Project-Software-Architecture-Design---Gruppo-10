package com.group10.controller.common;

import javafx.scene.Parent;

/**
 *
 * @author group10
 * PATTERN: Abstract Factory (l'AbstractProduct)
 * interfaccia comune a tutti i controller UI creati dalle factory,  possono
 * essere gestiti in modo uniforme
 */

public interface AbstractUIComponent{

    public Parent getRoot();
}
