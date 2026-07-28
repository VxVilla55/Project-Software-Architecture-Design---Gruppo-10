/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.group10.service.command;

/**
 * PATTERN: Command
 *
 * Ruolo: Command (interfaccia). incapsula una richiesta come oggetto, così può essere
 * eseguita, annullata e conservata in uno storico. Ogni ConcreteCommand implementa
 * {execute() (l'azione) e undo() (l'operazione inversa richiesta per
 * il supporto all'undo). l'invoker è CommandManager, il client sono i controller.
 *
 * @author group10
 */
public interface Command {

    void execute();

    void undo();
}
