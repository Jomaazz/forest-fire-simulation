package com.forestfire.model;

import java.util.Random;

/**
 * Represents the forest grid containing cells.
 * Manages the state of all cells and handles the fire propagation logic.
 */
public class Forest {
    
    private Cell[][] grid;
    private int height;
    private int width;
    private double propagationProbability;
    private Random random;
    
    /**
     * Creates a new forest with the specified dimensions and fire propagation probability.
     * 
     * @param height The height of the forest grid
     * @param width The width of the forest grid
     * @param propagationProbability The probability of fire spreading to adjacent cells
     */
    public Forest(int height, int width, double propagationProbability) {
        this.height = height;
        this.width = width;
        this.propagationProbability = propagationProbability;
        this.random = new Random();
        
        // Initialize the forest grid with trees
        initializeGrid();
    }
    
    /**
     * Initializes the forest grid with all cells set to TREE state.
     */
    private void initializeGrid() {
        grid = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Cell(Cell.State.TREE, i, j);
            }
        }
    }
    
    /**
     * Sets the initial fire positions in the forest.
     * 
     * @param positions Array of positions where each position is an array of [row, column]
     */
    public void setInitialFirePositions(int[][] positions) {
        for (int[] position : positions) {
            int row = position[0];
            int col = position[1];
            
            if (isValidPosition(row, col)) {
                grid[row][col].setState(Cell.State.FIRE);
            }
        }
    }
    
    /**
     * Simulates one step of the forest fire propagation.
     * 
     * @return true if there are still cells on fire, false otherwise
     */
    public boolean simulateStep() {
        // Create a copy of the current grid to calculate the next state
        Cell[][] nextGrid = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Create a new cell with the same state
                nextGrid[i][j] = new Cell(grid[i][j].getState(), i, j);
            }
        }
        
        // Apply fire propagation rules
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = grid[i][j];
                
                // If the cell is on fire, it becomes ash in the next step
                if (cell.getState() == Cell.State.FIRE) {
                    nextGrid[i][j].setState(Cell.State.ASH);
                    
                    // Try to spread fire to adjacent cells
                    spreadFireToAdjacentCells(i, j, nextGrid);
                }
            }
        }
        
        // Update the grid with the new state
        grid = nextGrid;
        
        // Check if there are still cells on fire
        return hasFireCells();
    }
    
    /**
     * Attempts to spread fire from a burning cell to its adjacent cells.
     * 
     * @param row The row of the burning cell
     * @param col The column of the burning cell
     * @param nextGrid The grid representing the next state
     */
    private void spreadFireToAdjacentCells(int row, int col, Cell[][] nextGrid) {
        // Check the four adjacent cells (up, right, down, left)
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            
            // Check if the position is valid and contains a tree
            if (isValidPosition(newRow, newCol) && grid[newRow][newCol].getState() == Cell.State.TREE) {
                // Determine if fire spreads based on the propagation probability
                if (random.nextDouble() < propagationProbability) {
                    nextGrid[newRow][newCol].setState(Cell.State.FIRE);
                }
            }
        }
    }
    
    /**
     * Checks if the given position is within the forest grid boundaries.
     * 
     * @param row The row position to check
     * @param col The column position to check
     * @return true if the position is valid, false otherwise
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < height && col >= 0 && col < width;
    }
    
    /**
     * Checks if there are any cells on fire in the forest.
     * 
     * @return true if there are cells on fire, false otherwise
     */
    public boolean hasFireCells() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[i][j].getState() == Cell.State.FIRE) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Gets the current state of the forest grid.
     * 
     * @return The 2D array of cells representing the forest
     */
    public Cell[][] getGrid() {
        return grid;
    }
    
    /**
     * Gets the height of the forest grid.
     * 
     * @return The height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Gets the width of the forest grid.
     * 
     * @return The width
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Resets the forest to its initial state with all cells as trees.
     */
    public void reset() {
        initializeGrid();
    }
    
    /**
     * Returns a string representation of the forest grid.
     * 
     * @return A string showing the current state of the forest
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(grid[i][j].toString()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
