package com.forestfire.gui;

import com.forestfire.model.Cell;
import com.forestfire.model.Forest;
import com.forestfire.service.SimulationService;
import com.forestfire.config.SimulationConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Main GUI class for the Forest Fire Simulation.
 * Provides a graphical interface for visualizing and controlling the simulation.
 */
public class ForestFireGUI extends JFrame {
    
    private SimulationService simulationService;
    private ForestGridPanel forestGridPanel;
    private ControlPanel controlPanel;
    private ConfigPanel configPanel;
    private Timer simulationTimer;
    private int delay = 500; // milliseconds between simulation steps
    
    /**
     * Creates a new Forest Fire GUI with the specified configuration file path.
     * 
     * @param configFilePath Path to the configuration properties file
     * @throws IOException If the configuration file cannot be read
     * @throws IllegalArgumentException If the configuration contains invalid values
     */
    public ForestFireGUI(String configFilePath) throws IOException, IllegalArgumentException {
        super("Forest Fire Simulation");
        
        // Initialize simulation service
        simulationService = new SimulationService(configFilePath);
        
        // Set up the GUI components
        setupUI();
        
        // Set up the simulation timer
        simulationTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stepSimulation();
            }
        });
        
        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set size and make visible
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Sets up the user interface components.
     */
    private void setupUI() {
        // Set layout
        setLayout(new BorderLayout());
        
        // Create menu bar
        setupMenuBar();
        
        // Create forest grid panel
        forestGridPanel = new ForestGridPanel(simulationService.getForest());
        add(forestGridPanel, BorderLayout.CENTER);
        
        // Create side panel for controls and configuration
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        
        // Create configuration panel
        configPanel = new ConfigPanel(simulationService.getConfig(), this);
        sidePanel.add(configPanel);
        
        // Create control panel
        controlPanel = new ControlPanel(this);
        sidePanel.add(controlPanel);
        
        // Add side panel to the frame
        add(sidePanel, BorderLayout.EAST);
    }
    
    /**
     * Sets up the menu bar.
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        // Simulation menu
        JMenu simMenu = new JMenu("Simulation");
        JMenuItem startItem = new JMenuItem("Start");
        startItem.addActionListener(e -> startSimulation());
        JMenuItem stopItem = new JMenuItem("Stop");
        stopItem.addActionListener(e -> stopSimulation());
        JMenuItem stepItem = new JMenuItem("Step");
        stepItem.addActionListener(e -> stepSimulation());
        JMenuItem resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(e -> resetSimulation());
        
        simMenu.add(startItem);
        simMenu.add(stopItem);
        simMenu.add(stepItem);
        simMenu.add(resetItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(simMenu);
        menuBar.add(helpMenu);
        
        // Set menu bar
        setJMenuBar(menuBar);
    }
    
    /**
     * Starts the simulation timer.
     */
    public void startSimulation() {
        if (!simulationTimer.isRunning() && simulationService.isRunning()) {
            simulationTimer.start();
            controlPanel.updateStartStopButton(true);
        }
    }
    
    /**
     * Stops the simulation timer.
     */
    public void stopSimulation() {
        if (simulationTimer.isRunning()) {
            simulationTimer.stop();
            controlPanel.updateStartStopButton(false);
        }
    }
    
    /**
     * Executes a single step of the simulation.
     */
    public void stepSimulation() {
        if (simulationService.isRunning()) {
            boolean isRunning = simulationService.executeStep();
            forestGridPanel.updateGrid(simulationService.getForest());
            controlPanel.updateStatus(simulationService.getStepCount(), isRunning);
            
            if (!isRunning) {
                stopSimulation();
            }
        }
    }
    
    /**
     * Resets the simulation to its initial state.
     */
    public void resetSimulation() {
        stopSimulation();
        simulationService.reset();
        forestGridPanel.updateGrid(simulationService.getForest());
        controlPanel.updateStatus(0, true);
    }
    
    /**
     * Updates the simulation speed.
     * 
     * @param speed The speed value (1-10)
     */
    public void setSimulationSpeed(int speed) {
        // Convert speed (1-10) to delay (1000-100 ms)
        delay = 1100 - (speed * 100);
        simulationTimer.setDelay(delay);
    }
    
    /**
     * Updates the simulation configuration.
     * 
     * @param height The forest height
     * @param width The forest width
     * @param probability The fire propagation probability
     * @param positions The initial fire positions
     */
    public void updateConfiguration(int height, int width, double probability, String positions) {
        try {
            // Stop the simulation
            stopSimulation();
            
            // Update the configuration using the SimulationService
            simulationService.updateConfiguration(height, width, probability, positions);
            
            // Update the forest grid panel with the new forest
            forestGridPanel.updateGrid(simulationService.getForest());
            
            // Update the control panel status
            controlPanel.updateStatus(0, true);
            
            // Resize the window if needed for larger forests
            if (height > 20 || width > 20) {
                setSize(Math.max(800, width * 20), Math.max(600, height * 20));
                setLocationRelativeTo(null);
            }
            
            JOptionPane.showMessageDialog(this, 
                "Configuration updated successfully.\nForest dimensions: " + width + "x" + height, 
                "Configuration Update", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error updating configuration: " + e.getMessage(), 
                "Configuration Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Shows the about dialog.
     */
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "Forest Fire Simulation\n\n" +
            "A graphical simulation of fire propagation through a forest.\n\n" +
            "Created by: Forest Fire Simulation Team",
            "About Forest Fire Simulation",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Main method to start the GUI application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Start the GUI
        SwingUtilities.invokeLater(() -> {
            try {
                String configPath = "config/simulation.properties";
                if (args.length > 0) {
                    configPath = args[0];
                }
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
}
