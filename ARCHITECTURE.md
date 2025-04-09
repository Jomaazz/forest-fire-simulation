# Forest Fire Simulation - Architecture

This document describes the architecture and design decisions of the Forest Fire Simulation project.

## Architecture Overview

The project follows a layered architecture with clear separation of concerns:

```
+------------------+
|    Controller    |  User interaction layer
+------------------+
         |
         v
+------------------+
|     Service      |  Business logic layer
+------------------+
         |
         v
+------------------+
|      Model       |  Domain model layer
+------------------+
         |
         v
+------------------+
|  Configuration   |  Configuration layer
+------------------+
```

## Components

### Model Layer

The model layer contains the core domain entities:

- **Cell**: Represents a single cell in the forest grid with states (TREE, FIRE, ASH)
- **Forest**: Represents the entire forest grid and contains the fire propagation logic

### Configuration Layer

The configuration layer handles loading and validating simulation parameters:

- **SimulationConfig**: Loads parameters from a properties file and validates them

### Service Layer

The service layer contains the business logic for running the simulation:

- **SimulationService**: Manages the simulation state and execution

### Controller Layer

The controller layer handles user interaction:

- **SimulationController**: Provides a command-line interface for interacting with the simulation

### Application Entry Point

- **App**: Main class that initializes the application and handles exceptions

## Design Decisions

### Discrete Time Simulation

The simulation uses discrete time steps rather than continuous time. This makes the simulation easier to understand and implement, and allows for step-by-step visualization.

### Probabilistic Fire Propagation

Fire propagation is modeled as a probabilistic process, where fire has a probability p of spreading to adjacent cells. This creates more realistic and varied simulation outcomes.

### Configuration via Properties File

Simulation parameters are loaded from a properties file, making it easy to modify the simulation behavior without changing the code.

### Command-Line Interface

The application uses a simple command-line interface for user interaction, focusing on functionality rather than graphical presentation.

### Immutable State Transitions

When simulating a step, a new grid is created rather than modifying the existing one. This ensures that all state transitions for a given step are based on the same initial state.

## Extension Points

The architecture allows for several potential extensions:

1. **GUI Interface**: The controller layer could be extended with a graphical interface
2. **Additional Cell States**: The Cell enum could be extended with more states (e.g., different tree types)
3. **Different Propagation Models**: The propagation logic in Forest could be modified or extended
4. **Persistence**: Simulation states could be saved and loaded
5. **Statistics Collection**: Additional classes could be added to collect and analyze simulation statistics
