// DataVisualizerFX/src/main/java/datavisualizerfx/controller/CommandManager.java
package datavisualizer.controller;

import datavisualizer.model.command.Command;

import java.util.Stack;

/**
 * Manages the execution and undo/redo of commands.
 */
public class CommandManager {

    private final Stack<Command> history = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    /**
     * Executes a command and adds it to the history.
     * Clears the redo stack.
     *
     * @param command The command to execute.
     */
    public void executeCommand(Command command) {
        command.execute();
        history.push(command);
        redoStack.clear();
    }

    /**
     * Undoes the last executed command.
     * If there are commands in the history, it pops the last one, undoes it,
     * and pushes it onto the redo stack.
     */
    public void undo() {
        if (!history.isEmpty()) {
            Command command = history.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    /**
     * Redoes the last undone command.
     * If there are commands in the redo stack, it pops the last one, re-executes it,
     * and pushes it back onto the history.
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            history.push(command);
        }
    }

    /**
     * Clears both the history and redo stacks.
     * Useful when loading new data or closing a file.
     */
    public void clearHistory() {
        history.clear();
        redoStack.clear();
    }
}