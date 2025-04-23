// DataVisualizerFX/src/main/java/datavisualizerfx/model/command/Command.java
package datavisualizer.model.command;

/**
 * Interface for command objects that can be executed and undone.
 * Used for implementing the Command pattern for undo/redo functionality.
 */
public interface Command {

    /**
     * Executes the command.
     */
    void execute();

    /**
     * Undoes the command.
     */
    void undo();
}