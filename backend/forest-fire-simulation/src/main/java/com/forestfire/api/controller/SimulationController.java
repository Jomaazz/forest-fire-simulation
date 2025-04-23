package com.forestfire.api.controller;

import com.forestfire.api.dto.ForestStateDTO;
import com.forestfire.api.dto.SimulationConfigDTO;
import com.forestfire.api.service.ApiSimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulation")
@CrossOrigin(origins = "*")
public class SimulationController {

    private final ApiSimulationService simulationService;

    @Autowired
    public SimulationController(ApiSimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/init")
    public ResponseEntity<ForestStateDTO> initializeSimulation(@RequestBody SimulationConfigDTO config) {
        ForestStateDTO state = simulationService.initializeSimulation(config);
        return ResponseEntity.ok(state);
    }

    @PostMapping("/step")
    public ResponseEntity<ForestStateDTO> executeStep() {
        ForestStateDTO state = simulationService.executeStep();
        return ResponseEntity.ok(state);
    }

    @PostMapping("/reset")
    public ResponseEntity<ForestStateDTO> resetSimulation() {
        ForestStateDTO state = simulationService.resetSimulation();
        return ResponseEntity.ok(state);
    }

    @GetMapping("/state")
    public ResponseEntity<ForestStateDTO> getCurrentState() {
        ForestStateDTO state = simulationService.getCurrentState();
        return ResponseEntity.ok(state);
    }

    @PutMapping("/config")
    public ResponseEntity<ForestStateDTO> updateConfiguration(@RequestBody SimulationConfigDTO config) {
        ForestStateDTO state = simulationService.updateConfiguration(config);
        return ResponseEntity.ok(state);
    }
}
