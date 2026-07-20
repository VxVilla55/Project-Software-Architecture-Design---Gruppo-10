/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.service.command;

import java.util.Stack;

import com.group10.model.MusicCatalogue;

/**
 *
 * @author group10
 *
 * Esegue i Command dell'applicazione e ne tiene lo storico per l'undo.
 *
 * PATTERN: Invoker del Command; Singleton
 */
public class CommandManager {

    private static CommandManager instance;

    private final Stack<Command> undoStack = new Stack<>();

    private CommandManager() {}

    public static CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        MusicCatalogue.getInstance().notifySubscribers();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            MusicCatalogue.getInstance().notifySubscribers();
        }
    }
}
