/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.group10.service.command;

/**
 *
 * @author group10
 * PATTERN: Command (l'interfaccia Command).
 * Incapsula una richiesta come oggetto, cosi' puo' essere eseguita, annullata e tenuta
 * in uno storico: execute() applica l'azione, undo() la inverte. CommandManager e'
 * l'Invoker (esegue i comandi e ne tiene lo storico), i controller sono il Client
 * (creano il ConcreteCommand giusto e lo passano all'Invoker). Ogni ConcreteCommand
 * salva nei propri campi tutto cio' che serve per fare e disfare l'operazione, cosi'
 * l'Invoker non deve sapere nulla dei dettagli di ogni singola azione.
 */
public interface Command {

    void execute();

    void undo();
}
