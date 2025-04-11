package com.forestfire.gui;

import com.forestfire.config.SimulationConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel for configuring the simulation parameters.
 * Provides input fields for forest dimensions, fire propagation probability, and initial fire positions.
 */
public class ConfigPanel extends JPanel {
    
    private ForestFireGUI parent;
    private JTextField heightField;
    private JTextField widthField;
    private JTextField probabilityField;
    private JTextField positionsField;
    private JButton applyButton;
    
    /**
     * Creates a new configuration panel with the specified configuration and parent GUI.
     * 
     * @param config The simulation configuration
     * @param parent The parent ForestFireGUI
     */
    public ConfigPanel(SimulationConfig config, ForestFireGUI parent) {
        this.parent = parent;
        
        // Set up panel
        setBorder(BorderFactory.createTitledBorder("Configuration"));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Create input fields
        JLabel heightLabel = new JLabel("Forest Height:");
        heightField = new JTextField(String.valueOf(config.getForestHeight()), 5);
        
        JLabel widthLabel = new JLabel("Forest Width:");
        widthField = new JTextField(String.valueOf(config.getForestWidth()), 5);
        
        JLabel probLabel = new JLabel("Fire Probability:");
        probabilityField = new JTextField(String.valueOf(config.getFirePropagationProbability()), 5);
        
        JLabel posLabel = new JLabel("Initial Fire Positions:");
        // Convert positions array to string
        StringBuilder posBuilder = new StringBuilder();
        for (int[] pos : config.getInitialFirePositions()) {
            if (posBuilder.length() > 0) {
                posBuilder.append(";");
            }
            posBuilder.append(pos[0]).append(",").append(pos[1]);
        }
        positionsField = new JTextField(posBuilder.toString(), 10);
        
        // Create apply button
        applyButton = new JButton("Apply Changes");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyConfiguration();
            }
        });
        
        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(heightLabel, gbc);
        
        gbc.gridx = 1;
        add(heightField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(widthLabel, gbc);
        
        gbc.gridx = 1;
        add(widthField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(probLabel, gbc);
        
        gbc.gridx = 1;
        add(probabilityField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(posLabel, gbc);
        
        gbc.gridy = 4;
        add(positionsField, gbc);
        
        gbc.gridy = 5;
        add(applyButton, gbc);
    }
    
    /**
     * Applies the configuration changes.
     */
    private void applyConfiguration() {
        try {
            // Parse input values
            int height = Integer.parseInt(heightField.getText().trim());
            int width = Integer.parseInt(widthField.getText().trim());
            double probability = Double.parseDouble(probabilityField.getText().trim());
            String positions = positionsField.getText().trim();
            
            // Validate input values
            if (height <= 0 || width <= 0) {
                throw new IllegalArgumentException("Forest dimensions must be positive");
            }
            
            if (probability < 0 || probability > 1) {
                throw new IllegalArgumentException("Fire propagation probability must be between 0 and 1");
            }
            
            // Update configuration
            parent.updateConfiguration(height, width, probability, positions);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Invalid number format. Please enter valid numbers.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
