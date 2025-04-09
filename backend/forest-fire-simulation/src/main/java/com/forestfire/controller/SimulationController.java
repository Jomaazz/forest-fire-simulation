package com.forestfire.controller;

import com.forestfire.model.Forest;
import com.forestfire.service.SimulationService;

import java.io.IOException;
import java.util.Scanner;

/**
 * Controller class that handles user interaction with the simulation.
 * Provides a command-line interface for running and controlling the simulation.
 */
public class SimulationController {
    
    private SimulationService simulationService;
    private Scanner scanner;
    
    /**
     * Creates a new simulation controller with the specified configuration file path.
     * 
     * @param configFilePath Path to the configuration properties file
     * @throws IOException If the configuration file cannot be read
     * @throws IllegalArgumentException If the configuration contains invalid values
     */
    public SimulationController(String configFilePath) throws IOException, IllegalArgumentException {
        this.simulationService = new SimulationService(configFilePath);
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Starts the interactive simulation interface.
     */
    public void start() {
        System.out.println("Forest Fire Simulation");
        System.out.println("=====================");
        
        boolean exit = false;
        while (!exit) {
            // Display the current forest state
            displayForest();
            
            // Display options
            System.out.println("\nOptions:");
            System.out.println("1. Run single step");
            System.out.println("2. Run to completion");
            System.out.println("3. Reset simulation");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            
            // Get user input
            String input = scanner.nextLine().trim();
            
            // Process user choice
            switch (input) {
                case "1":
                    runSingleStep();
                    break;
                case "2":
                    runToCompletion();
                    break;
                case "3":
                    resetSimulation();
                    break;
                case "4":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        System.out.println("Simulation ended.");
    }
    
    /**
     * Displays the current state of the forest.
     */
    private void displayForest() {
        System.out.println("\nInitial forest state:");
        Forest forest = simulationService.getForest();
        System.out.println(forest.toString());
    }
    
    /**
     * Runs a single step of the simulation.
     */
    private void runSingleStep() {
        boolean isRunning = simulationService.executeStep();
        
        if (isRunning) {
            System.out.println("Step " + simulationService.getStepCount() + " completed.");
        } else {
            System.out.println("Simulation complete after " + simulationService.getStepCount() + " steps.");
            System.out.println("No more cells are on fire.");
        }
    }
    
    /**
     * Runs the simulation until completion.
     */
    private void runToCompletion() {
        int steps = simulationService.runToCompletion();
        System.out.println("Simulation complete after " + steps + " steps.");
        System.out.println("No more cells are on fire.");
    }
    
    /**
     * Resets the simulation to its initial state.
     */
    private void resetSimulation() {
        simulationService.reset();
        System.out.println("Simulation reset to initial state.");
    }
    
    /**
     * Closes resources used by the controller.
     */
    public void close() {
        scanner.close();
    }
}
