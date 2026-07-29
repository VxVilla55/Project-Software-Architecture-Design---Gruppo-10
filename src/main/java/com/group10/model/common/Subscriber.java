/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.group10.model.common;

/**
 *
 * @author group10
 * PATTERN: Observer (il Subscriber).
 * Chi implementa questa interfaccia si registra presso un Publisher (con addSubscriber)
 * per essere avvisato dei suoi cambiamenti: update() viene chiamato dal Publisher, e ogni
 * Subscriber decide da solo cosa fare. Il Publisher non sa e non deve sapere cosa fa
 * ogni singolo Subscriber al suo interno.
 */
public interface Subscriber {

    public void update();

}