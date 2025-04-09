package com.forestfire.model;

/**
 * Represents a single cell in the forest grid.
 * A cell can be in one of three states: TREE, FIRE, or ASH.
 */
public class Cell {
    
    /**
     * Possible states for a cell in the forest.
     */
    public enum State {
        TREE, // A healthy tree that can catch fire
        FIRE, // A burning tree
        ASH   // The remains after a tree has burned
    }
    
    private State state;
    private int row;
    private int col;
    
    /**
     * Creates a new cell with the specified state and position.
     * 
     * @param state The initial state of the cell
     * @param row The row position in the forest grid
     * @param col The column position in the forest grid
     */
    public Cell(State state, int row, int col) {
        this.state = state;
        this.row = row;
        this.col = col;
    }
    
    /**
     * Gets the current state of the cell.
     * 
     * @return The current state
     */
    public State getState() {
        return state;
    }
    
    /**
     * Sets the state of the cell.
     * 
     * @param state The new state
     */
    public void setState(State state) {
        this.state = state;
    }
    
    /**
     * Gets the row position of the cell.
     * 
     * @return The row position
     */
    public int getRow() {
        return row;
    }
    
    /**
     * Gets the column position of the cell.
     * 
     * @return The column position
     */
    public int getCol() {
        return col;
    }
    
    /**
     * Returns a string representation of the cell based on its state.
     * 
     * @return "T" for TREE, "F" for FIRE, "A" for ASH
     */
    @Override
    public String toString() {
        switch (state) {
            case TREE:
                return "T";
            case FIRE:
                return "F";
            case ASH:
                return "A";
            default:
                return " ";
        }
    }
}
