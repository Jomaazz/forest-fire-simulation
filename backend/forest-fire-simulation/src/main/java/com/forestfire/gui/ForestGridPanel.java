package com.forestfire.gui;

import com.forestfire.model.Cell;
import com.forestfire.model.Forest;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for visualizing the forest grid.
 * Renders the forest as a grid of colored cells.
 */
public class ForestGridPanel extends JPanel {
    
    private Forest forest;
    private int cellSize = 20; // Default cell size in pixels
    
    /**
     * Creates a new forest grid panel with the specified forest.
     * 
     * @param forest The forest to visualize
     */
    public ForestGridPanel(Forest forest) {
        this.forest = forest;
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
    
    /**
     * Updates the forest grid with a new forest.
     * 
     * @param forest The new forest to visualize
     */
    public void updateGrid(Forest forest) {
        this.forest = forest;
        repaint();
    }
    
    /**
     * Paints the forest grid.
     * 
     * @param g The graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (forest == null) {
            return;
        }
        
        // Calculate cell size based on panel size and forest dimensions
        int width = getWidth();
        int height = getHeight();
        int forestWidth = forest.getWidth();
        int forestHeight = forest.getHeight();
        
        int cellWidth = width / forestWidth;
        int cellHeight = height / forestHeight;
        cellSize = Math.min(cellWidth, cellHeight);
        
        // Draw the forest grid
        Cell[][] grid = forest.getGrid();
        for (int i = 0; i < forestHeight; i++) {
            for (int j = 0; j < forestWidth; j++) {
                // Calculate cell position
                int x = j * cellSize;
                int y = i * cellSize;
                
                // Get cell state and set color
                Cell cell = grid[i][j];
                Color cellColor;
                
                switch (cell.getState()) {
                    case TREE:
                        cellColor = new Color(34, 139, 34); // Forest Green
                        break;
                    case FIRE:
                        cellColor = new Color(255, 69, 0);  // Red-Orange
                        break;
                    case ASH:
                        cellColor = new Color(169, 169, 169); // Dark Gray
                        break;
                    default:
                        cellColor = Color.WHITE;
                }
                
                // Draw the cell
                g.setColor(cellColor);
                g.fillRect(x, y, cellSize, cellSize);
                
                // Draw cell border
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }
    }
    
    /**
     * Returns the preferred size of the panel.
     * 
     * @return The preferred size
     */
    @Override
    public Dimension getPreferredSize() {
        if (forest == null) {
            return new Dimension(400, 400);
        }
        
        int width = forest.getWidth() * cellSize;
        int height = forest.getHeight() * cellSize;
        return new Dimension(width, height);
    }
}
