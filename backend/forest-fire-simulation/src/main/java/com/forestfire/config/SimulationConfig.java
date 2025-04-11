package com.forestfire.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Configuration class for the forest fire simulation.
 * Loads and validates simulation parameters from a properties file.
 */
public class SimulationConfig {
    
    private int forestHeight;
    private int forestWidth;
    private double firePropagationProbability;
    private int[][] initialFirePositions;
    private String configFilePath;
    
    /**
     * Creates a new simulation configuration with the specified config file path.
     * 
     * @param configFilePath Path to the configuration properties file
     * @throws IOException If the configuration file cannot be read
     * @throws IllegalArgumentException If the configuration contains invalid values
     */
    public SimulationConfig(String configFilePath) throws IOException, IllegalArgumentException {
        this.configFilePath = configFilePath;
        loadConfig();
    }
    
    /**
     * Loads configuration from the properties file.
     * 
     * @throws IOException If the configuration file cannot be read
     * @throws IllegalArgumentException If the configuration contains invalid values
     */
    private void loadConfig() throws IOException, IllegalArgumentException {
        Properties properties = new Properties();
        
        try (InputStream input = new FileInputStream(configFilePath)) {
            properties.load(input);
            
            // Parse forest dimensions
            forestHeight = Integer.parseInt(properties.getProperty("forest.height", "10"));
            forestWidth = Integer.parseInt(properties.getProperty("forest.width", "10"));
            
            // Parse fire propagation probability
            firePropagationProbability = Double.parseDouble(
                properties.getProperty("fire.propagation.probability", "0.5"));
            
            // Parse initial fire positions
            String positionsStr = properties.getProperty("fire.initial.positions", "0,0");
            initialFirePositions = parseFirePositions(positionsStr);
            
            // Validate configuration
            validateConfig();
        }
    }
    
    /**
     * Parses the initial fire positions from a string.
     * Format: "row1,col1;row2,col2;..."
     * 
     * @param positionsStr String containing the positions
     * @return 2D array of positions where each position is [row, col]
     */
    private int[][] parseFirePositions(String positionsStr) {
        String[] positions = positionsStr.split(";");
        List<int[]> positionsList = new ArrayList<>();
        
        for (String position : positions) {
            String[] coordinates = position.split(",");
            if (coordinates.length == 2) {
                try {
                    int row = Integer.parseInt(coordinates[0].trim());
                    int col = Integer.parseInt(coordinates[1].trim());
                    positionsList.add(new int[]{row, col});
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid fire position format: " + position);
                }
            }
        }
        
        return positionsList.toArray(new int[0][]);
    }
    
    /**
     * Validates the configuration parameters.
     * 
     * @throws IllegalArgumentException If any parameter is invalid
     */
    private void validateConfig() throws IllegalArgumentException {
        if (forestHeight <= 0) {
            throw new IllegalArgumentException("Forest height must be positive");
        }
        
        if (forestWidth <= 0) {
            throw new IllegalArgumentException("Forest width must be positive");
        }
        
        if (firePropagationProbability < 0 || firePropagationProbability > 1) {
            throw new IllegalArgumentException("Fire propagation probability must be between 0 and 1");
        }
        
        for (int[] position : initialFirePositions) {
            if (position[0] < 0 || position[0] >= forestHeight || 
                position[1] < 0 || position[1] >= forestWidth) {
                throw new IllegalArgumentException(
                    "Initial fire position (" + position[0] + "," + position[1] + ") is outside the forest");
            }
        }
    }
    
    /**
     * Gets the forest height.
     * 
     * @return The height of the forest grid
     */
    public int getForestHeight() {
        return forestHeight;
    }
    
    /**
     * Gets the forest width.
     * 
     * @return The width of the forest grid
     */
    public int getForestWidth() {
        return forestWidth;
    }
    
    /**
     * Gets the fire propagation probability.
     * 
     * @return The probability of fire spreading to adjacent cells
     */
    public double getFirePropagationProbability() {
        return firePropagationProbability;
    }
    
    /**
     * Gets the initial fire positions.
     * 
     * @return 2D array of positions where each position is [row, col]
     */
    public int[][] getInitialFirePositions() {
        return initialFirePositions;
    }
    
    /**
     * Gets the configuration file path.
     * 
     * @return The path to the configuration file
     */
    public String getConfigFilePath() {
        return configFilePath;
    }
    
    /**
     * Sets the forest height.
     * 
     * @param forestHeight The new height of the forest grid
     * @throws IllegalArgumentException If the height is not positive
     */
    public void setForestHeight(int forestHeight) throws IllegalArgumentException {
        if (forestHeight <= 0) {
            throw new IllegalArgumentException("Forest height must be positive");
        }
        this.forestHeight = forestHeight;
    }
    
    /**
     * Sets the forest width.
     * 
     * @param forestWidth The new width of the forest grid
     * @throws IllegalArgumentException If the width is not positive
     */
    public void setForestWidth(int forestWidth) throws IllegalArgumentException {
        if (forestWidth <= 0) {
            throw new IllegalArgumentException("Forest width must be positive");
        }
        this.forestWidth = forestWidth;
    }
    
    /**
     * Sets the fire propagation probability.
     * 
     * @param firePropagationProbability The new probability of fire spreading to adjacent cells
     * @throws IllegalArgumentException If the probability is not between 0 and 1
     */
    public void setFirePropagationProbability(double firePropagationProbability) throws IllegalArgumentException {
        if (firePropagationProbability < 0 || firePropagationProbability > 1) {
            throw new IllegalArgumentException("Fire propagation probability must be between 0 and 1");
        }
        this.firePropagationProbability = firePropagationProbability;
    }
    
    /**
     * Sets the initial fire positions.
     * 
     * @param positionsStr String containing the positions in format "row1,col1;row2,col2;..."
     * @throws IllegalArgumentException If any position is invalid
     */
    public void setInitialFirePositions(String positionsStr) throws IllegalArgumentException {
        int[][] positions = parseFirePositions(positionsStr);
        
        // Validate positions
        for (int[] position : positions) {
            if (position[0] < 0 || position[0] >= forestHeight || 
                position[1] < 0 || position[1] >= forestWidth) {
                throw new IllegalArgumentException(
                    "Initial fire position (" + position[0] + "," + position[1] + ") is outside the forest");
            }
        }
        
        this.initialFirePositions = positions;
    }
}
