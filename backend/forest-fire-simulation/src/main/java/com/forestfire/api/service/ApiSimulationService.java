package com.forestfire.api.service;

import com.forestfire.api.dto.ForestStateDTO;
import com.forestfire.api.dto.SimulationConfigDTO;
import com.forestfire.model.Cell;
import com.forestfire.model.Forest;
import com.forestfire.service.SimulationService;
import org.springframework.stereotype.Service;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiSimulationService {
    
    private SimulationService simulationService;
    private int currentStep = 0;
    
    public ApiSimulationService() {
        this.simulationService = new SimulationService();
    }
    
    public ForestStateDTO initializeSimulation(SimulationConfigDTO configDTO) {
        // Convert DTO to domain model
        List<Point> initialPositions = new ArrayList<>();
        for (int[] position : configDTO.getFireInitialPositions()) {
            initialPositions.add(new Point(position[0], position[1]));
        }
        
        // Update configuration
        simulationService.updateConfiguration(
            configDTO.getForestHeight(),
            configDTO.getForestWidth(),
            configDTO.getFirePropagationProbability(),
            initialPositions.stream()
                .map(p -> p.x + "," + p.y)
                .collect(Collectors.joining(";"))
        );
        
        // Reset simulation
        simulationService.resetSimulation();
        currentStep = 0;
        
        // Return current state
        return getCurrentState();
    }
    
    public ForestStateDTO executeStep() {
        boolean hasMoreSteps = simulationService.executeStep();
        currentStep++;
        
        ForestStateDTO stateDTO = getCurrentState();
        stateDTO.setComplete(!hasMoreSteps);
        
        return stateDTO;
    }
    
    public ForestStateDTO resetSimulation() {
        simulationService.resetSimulation();
        currentStep = 0;
        return getCurrentState();
    }
    
    public ForestStateDTO getCurrentState() {
        Forest forest = simulationService.getForest();
        Cell[][] grid = forest.getGrid();
        
        String[][] gridDTO = new String[grid.length][grid[0].length];
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                gridDTO[i][j] = grid[i][j].getState().name();
            }
        }
        
        return new ForestStateDTO(gridDTO, currentStep, isSimulationComplete());
    }
    
    public ForestStateDTO updateConfiguration(SimulationConfigDTO configDTO) {
        // Convert DTO to domain model
        List<Point> initialPositions = new ArrayList<>();
        for (int[] position : configDTO.getFireInitialPositions()) {
            initialPositions.add(new Point(position[0], position[1]));
        }
        
        // Update configuration
        simulationService.updateConfiguration(
            configDTO.getForestHeight(),
            configDTO.getForestWidth(),
            configDTO.getFirePropagationProbability(),
            initialPositions.stream()
                .map(p -> p.x + "," + p.y)
                .collect(Collectors.joining(";"))
        );
        
        // Reset simulation with new configuration
        return resetSimulation();
    }
    
    private boolean isSimulationComplete() {
        Forest forest = simulationService.getForest();
        Cell[][] grid = forest.getGrid();
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].getState() == Cell.State.FIRE) {
                    return false;
                }
            }
        }
        
        return true;
    }
}
