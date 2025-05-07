# DataVisualizer

DataVisualizer is a JavaFX-based desktop application developed as part of the **DTEK2074** university course. The application allows users to load and visualize data from CSV and JSON files interactively. Users can select which columns to display, switch between different chart types, and undo/redo their actions. The project emphasizes clean architecture and the use of design patterns to ensure maintainability and extensibility.

## Table of Contents

1. [Introduction](#introduction)
2. [Features](#features)
3. [Architecture](#architecture)
4. [Design Patterns](#design-patterns)
5. [Learning Objectives](#learning-objectives)
6. [How to Run](#how-to-run)
7. [Example Images](#example-images)
8. [Future Extensions](#future-extensions)
9. [Acknowledgements](#acknowledgements)

---

## Features

- **Interactive Data Visualization**: Load CSV or JSON files and visualize data as bar, line, or pie charts.
- **Column Selection**: Choose which columns to display in the chart.
- **Undo/Redo Functionality**: Revert or reapply changes to chart configurations.
- **Extensible Design**: Easily add support for new file formats or chart types.

---

## Architecture

The application follows **Model-View-Controller (MVC)** architecture:

- **Model**: Manages the application's data and state (e.g., `ChartStateModel`, `DataSet` & `ChartFactory`).
- **View**: Handles the user interface and displays data (e.g., `MainView`, `ChartView`).
- **Controller**: Acts as an intermediary between the Model and View, handling user input and updating the Model (e.g., `AppController`).

This separation of concerns ensures modularity, making the application easier to maintain and extend.

---

## Design Patterns

The application incorporates **four design patterns** to meet the functional and technical requirements:

### 1. **Observer Pattern**

- **Purpose**: Keeps the user interface components in sync with the application's state.
- **Implementation**:
  - The `ChartStateModel` acts as the **Subject**, maintaining a list of observers.
  - The `AppController` implements the `ChartStateObserver` interface and updates the `View` when the `Model` changes.
- **Example**: When the user selects a new column or chart type, the `ChartStateModel` notifies the `AppController`, which updates the `ChartView`.

### 2. **Factory Pattern**

- **Purpose**: Simplifies the creation of different chart types.
- **Implementation**:
  - The `ChartFactory` class provides a method to create charts (e.g., bar, line, pie) based on the user's selection.
- **Example**: When the user selects a chart type, the `ChartFactory` dynamically creates the appropriate chart object and returns it to the `ChartView`.

### 3. **Strategy Pattern**

- **Purpose**: Enables flexible handling of different file formats.
- **Implementation**:
  - The `DataParser` interface defines a common method for parsing files.
  - Specific implementations like `CSVParser` and `JSONParser` handle the parsing logic for their respective formats.
- **Example**: When a file is loaded, the `FileController` selects the appropriate parser (e.g., `CSVParser` for `.csv` files) and uses it to parse the data.

### 4. **Command Pattern**

- **Purpose**: Implements undo/redo functionality.
- **Implementation**:
  - The `Command` interface defines methods for executing and undoing actions.
  - The `UpdateChartStateCommand` encapsulates changes to the chart state.
  - The `CommandManager` manages a history of commands and provides undo/redo functionality.
- **Example**: When the user changes the chart type or selected columns, the action is encapsulated in a command. The `CommandManager` allows the user to undo or redo the action.

---

## Learning Objectives

This project was designed to meet the following learning objectives:

1. **Familiarisation with Design Patterns**: Implementing and understanding the practical use of design patterns.
2. **High-Level Design**: Designing the application as a UML diagram.
3. **Structured Implementation**: Developing the application according to the design, ensuring modularity and maintainability.

---

## How to Run

1. Clone the repository.
2. Ensure you have Java 23 and JavaFX 21.0.7 installed.
3. Build the project using Gradle:

   ```sh
   ./gradlew build

   ```

4. Run the application:
   ```sh
   ./gradlew run
   ```

---

## Example Images

Below are example screenshots of the application in action:

### 1. **Bar Chart Example**

![Bar Chart Example](images/bar_chart.png 'Bar Chart Example')

### 2. **Line Chart Example**

![Line Chart Example](images/line_chart.png 'Line Chart Example')

### 3. **Pie Chart Example**

![Pie Chart Example](images/pie_chart.png 'Pie Chart Example')

### 4. **UML-diagram of the application**

![UML-diagram of the application](images/uml_final.png 'UML-diagram')

## Future Extensions

Add support for additional file formats (e.g., XML).
Implement more chart types (e.g., scatter plots, histograms).
Enhance the user interface with advanced styling and animations.

## Acknowledgements

This project was developed as part of the University of Turku DTEK2074 course. Special thanks to the course instructors and teaching assistants for their guidance.
