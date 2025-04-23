package com.forestfire.api.dto;

import java.util.List;

public class ForestStateDTO {
    private String[][] grid;
    private int step;
    private boolean complete;

    public ForestStateDTO() {
    }

    public ForestStateDTO(String[][] grid, int step, boolean complete) {
        this.grid = grid;
        this.step = step;
        this.complete = complete;
    }

    public String[][] getGrid() {
        return grid;
    }

    public void setGrid(String[][] grid) {
        this.grid = grid;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
