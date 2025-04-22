// ChangeChartTypeCommand.java
package datavisualizer.model.command;

import datavisualizer.controller.AppController;
import datavisualizer.model.ChartType;

public class ChangeChartTypeCommand implements Command {
    private AppController controller;
    private ChartType oldType;
    private ChartType newType;
    
    public ChangeChartTypeCommand(AppController controller, ChartType oldType, ChartType newType) {
        this.controller = controller;
        this.oldType = oldType;
        this.newType = newType;
    }
    
    @Override
    public void execute() {
        controller.setChartType(newType);
    }
    
    @Override
    public void undo() {
        controller.setChartType(oldType);
    }
}