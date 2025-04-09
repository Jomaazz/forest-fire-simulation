package com.forestfire;

import com.forestfire.controller.SimulationController;

import java.io.IOException;

/**
 * Main application class for the Forest Fire Simulation.
 * Entry point for running the simulation.
 */
public class App {
    
    // Default configuration file path
    private static final String DEFAULT_CONFIG_PATH = "config/simulation.properties";
    
    /**
     * Main method to start the application.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Starting Forest Fire Simulation...");
        
        try {
            // Create and start the simulation controller
            SimulationController controller = new SimulationController(DEFAULT_CONFIG_PATH);
            controller.start();
            controller.close();
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            System.err.println("Please ensure the configuration file exists at: " + DEFAULT_CONFIG_PATH);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid configuration: " + e.getMessage());
            System.err.println("Please check the configuration file for errors.");
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
