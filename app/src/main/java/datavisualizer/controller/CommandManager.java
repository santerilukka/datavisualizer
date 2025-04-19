package datavisualizer.controller;

import datavisualizer.model.command.Command;
import java.util.Stack;

public class CommandManager {
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();
}
