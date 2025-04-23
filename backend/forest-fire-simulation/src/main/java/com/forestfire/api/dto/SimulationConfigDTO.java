package com.forestfire.api.dto;

import java.util.List;

public class SimulationConfigDTO {
    private int forestHeight;
    private int forestWidth;
    private double firePropagationProbability;
    private List<int[]> fireInitialPositions;

    public SimulationConfigDTO() {
    }

    public SimulationConfigDTO(int forestHeight, int forestWidth, double firePropagationProbability, List<int[]> fireInitialPositions) {
        this.forestHeight = forestHeight;
        this.forestWidth = forestWidth;
        this.firePropagationProbability = firePropagationProbability;
        this.fireInitialPositions = fireInitialPositions;
    }

    public int getForestHeight() {
        return forestHeight;
    }

    public void setForestHeight(int forestHeight) {
        this.forestHeight = forestHeight;
    }

    public int getForestWidth() {
        return forestWidth;
    }

    public void setForestWidth(int forestWidth) {
        this.forestWidth = forestWidth;
    }

    public double getFirePropagationProbability() {
        return firePropagationProbability;
    }

    public void setFirePropagationProbability(double firePropagationProbability) {
        this.firePropagationProbability = firePropagationProbability;
    }

    public List<int[]> getFireInitialPositions() {
        return fireInitialPositions;
    }

    public void setFireInitialPositions(List<int[]> fireInitialPositions) {
        this.fireInitialPositions = fireInitialPositions;
    }
}
