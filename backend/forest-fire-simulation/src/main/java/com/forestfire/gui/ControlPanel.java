package com.forestfire.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel for controlling the simulation.
 * Provides buttons and controls for running, stepping, and resetting the simulation.
 */
public class ControlPanel extends JPanel {
    
    private ForestFireGUI parent;
    private JButton startStopButton;
    private JButton stepButton;
    private JButton resetButton;
    private JSlider speedSlider;
    private JLabel statusLabel;
    private boolean isRunning = false;
    
    /**
     * Creates a new control panel with the specified parent GUI.
     * 
     * @param parent The parent ForestFireGUI
     */
    public ControlPanel(ForestFireGUI parent) {
        this.parent = parent;
        
        // Set up panel
        setBorder(BorderFactory.createTitledBorder("Controls"));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Create buttons
        startStopButton = new JButton("Start");
        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunning) {
                    parent.stopSimulation();
                } else {
                    parent.startSimulation();
                }
            }
        });
        
        stepButton = new JButton("Step");
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.stepSimulation();
            }
        });
        
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.resetSimulation();
            }
        });
        
        // Create speed slider
        JLabel speedLabel = new JLabel("Speed:");
        speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 5);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.addChangeListener(e -> {
            if (!speedSlider.getValueIsAdjusting()) {
                parent.setSimulationSpeed(speedSlider.getValue());
            }
        });
        
        // Create status label
        statusLabel = new JLabel("Status: Ready (Step 0)");
        
        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(startStopButton, gbc);
        
        gbc.gridx = 1;
        add(stepButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(resetButton, gbc);
        
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(speedLabel, gbc);
        
        gbc.gridx = 1;
        add(speedSlider, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(statusLabel, gbc);
    }
    
    /**
     * Updates the start/stop button text and state.
     * 
     * @param running Whether the simulation is running
     */
    public void updateStartStopButton(boolean running) {
        isRunning = running;
        startStopButton.setText(running ? "Stop" : "Start");
    }
    
    /**
     * Updates the status label with the current step count and running state.
     * 
     * @param stepCount The current step count
     * @param running Whether the simulation is still running
     */
    public void updateStatus(int stepCount, boolean running) {
        String status = running ? "Running" : "Completed";
        statusLabel.setText("Status: " + status + " (Step " + stepCount + ")");
    }
}
