package com.forestfire;

import com.forestfire.gui.ForestFireGUI;

import javax.swing.*;

/**
 * Modified App class that provides options to run either the command-line or GUI version.
 * This serves as the main entry point for the application.
 */
public class App {
    
    /**
     * Main method to start the application.
     * Provides options to run either the command-line or GUI version.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Check if GUI mode is specified
        boolean guiMode = false;
        String configPath = "config/simulation.properties";
        
        // Parse command line arguments
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--gui")) {
                guiMode = true;
            } else if (args[i].equals("--config") && i + 1 < args.length) {
                configPath = args[i + 1];
                i++; // Skip the next argument
            }
        }
        
        // Run in appropriate mode
        if (guiMode) {
            // Run in GUI mode
            runGUI(configPath);
        } else {
            // Run in command-line mode
            runCommandLine(configPath);
        }
    }
    
    /**
     * Runs the application in GUI mode.
     * 
     * @param configPath Path to the configuration file
     */
    private static void runGUI(String configPath) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Start the GUI
        SwingUtilities.invokeLater(() -> {
            try {
                new ForestFireGUI(configPath);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                    "Error starting application: " + e.getMessage(),
                    "Application Error",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Runs the application in command-line mode.
     * 
     * @param configPath Path to the configuration file
     */
    private static void runCommandLine(String configPath) {
        try {
            System.out.println("Starting Forest Fire Simulation...");
            
            // Create and start the simulation controller
            com.forestfire.controller.SimulationController controller = 
                new com.forestfire.controller.SimulationController(configPath);
            controller.start();
            
            // Close resources
            controller.close();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
