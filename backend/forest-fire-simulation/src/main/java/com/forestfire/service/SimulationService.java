package com.forestfire.service;

import com.forestfire.model.Cell;
import com.forestfire.model.Forest;
import com.forestfire.config.SimulationConfig;

import java.io.IOException;

/**
 * Service class that manages the forest fire simulation.
 * Handles initialization, step execution, and simulation state.
 */
public class SimulationService {
    
    private Forest forest;
    private SimulationConfig config;
    private boolean isRunning;
    private int stepCount;
    
    /**
     * Creates a new simulation service with the specified configuration file path.
     * 
     * @param configFilePath Path to the configuration properties file
     * @throws IOException If the configuration file cannot be read
     * @throws IllegalArgumentException If the configuration contains invalid values
     */
    public SimulationService(String configFilePath) throws IOException, IllegalArgumentException {
        this.config = new SimulationConfig(configFilePath);
        initialize();
    }
    
    /**
     * Creates a new simulation service with default configuration.
     * This constructor is provided for API compatibility.
     */
    public SimulationService() {
        // Create default configuration
        this.config = new SimulationConfig();
        initialize();
    }
    
    /**
     * Initializes the simulation with the loaded configuration.
     */
    private void initialize() {
        // Create a new forest with the configured dimensions and propagation probability
        forest = new Forest(
            config.getForestHeight(),
            config.getForestWidth(),
            config.getFirePropagationProbability()
        );
        
        // Set the initial fire positions
        forest.setInitialFirePositions(config.getInitialFirePositions());
        
        // Reset simulation state
        isRunning = true;
        stepCount = 0;
    }
    
    /**
     * Updates the simulation configuration and reinitializes the simulation.
     * 
     * @param height The new forest height
     * @param width The new forest width
     * @param probability The new fire propagation probability
     * @param positions The new initial fire positions string
     * @throws IllegalArgumentException If any parameter is invalid
     */
    public void updateConfiguration(int height, int width, double probability, String positions) 
            throws IllegalArgumentException {
        // Update configuration
        config.setForestHeight(height);
        config.setForestWidth(width);
        config.setFirePropagationProbability(probability);
        config.setInitialFirePositions(positions);
        
        // Reinitialize the simulation with the new configuration
        initialize();
    }
    
    /**
     * Executes a single step of the simulation.
     * 
     * @return true if the simulation is still running, false if it has ended
     */
    public boolean executeStep() {
        if (!isRunning) {
            return false;
        }
        
        // Simulate one step of fire propagation
        boolean hasFireCells = forest.simulateStep();
        
        // Increment step count
        stepCount++;
        
        // Update running state
        isRunning = hasFireCells;
        
        return isRunning;
    }
    
    /**
     * Runs the simulation until completion (no more fire cells).
     * 
     * @return The number of steps executed
     */
    public int runToCompletion() {
        while (isRunning) {
            executeStep();
        }
        return stepCount;
    }
    
    /**
     * Resets the simulation to its initial state.
     */
    public void reset() {
        initialize();
    }
    
    /**
     * Resets the simulation to its initial state.
     * This method is an alias for reset() to maintain API compatibility.
     * 
     * @return The forest object after reset
     */
    public Forest resetSimulation() {
        reset();
        return forest;
    }
    
    /**
     * Gets the current state of the forest.
     * 
     * @return The forest object
     */
    public Forest getForest() {
        return forest;
    }
    
    /**
     * Gets the current step count.
     * 
     * @return The number of steps executed
     */
    public int getStepCount() {
        return stepCount;
    }
    
    /**
     * Checks if the simulation is still running.
     * 
     * @return true if the simulation is running, false if it has ended
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    /**
     * Gets the simulation configuration.
     * 
     * @return The configuration object
     */
    public SimulationConfig getConfig() {
        return config;
    }
}
