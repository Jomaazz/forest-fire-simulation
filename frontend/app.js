// Forest Fire Simulation - JavaScript Frontend

// Cell states
const CellState = {
    TREE: 'TREE',
    FIRE: 'FIRE',
    ASH: 'ASH'
};

// Simulation class
class ForestFireSimulation {
    constructor() {
        // DOM elements
        this.canvas = document.getElementById('forestGrid');
        this.ctx = this.canvas.getContext('2d');
        this.startBtn = document.getElementById('startBtn');
        this.stepBtn = document.getElementById('stepBtn');
        this.resetBtn = document.getElementById('resetBtn');
        this.speedSlider = document.getElementById('speedSlider');
        this.statusText = document.getElementById('statusText');
        this.applyBtn = document.getElementById('applyBtn');
        
        // Configuration inputs
        this.forestHeightInput = document.getElementById('forestHeight');
        this.forestWidthInput = document.getElementById('forestWidth');
        this.fireProbabilityInput = document.getElementById('fireProbability');
        this.initialPositionsInput = document.getElementById('initialPositions');
        
        // Simulation state
        this.config = {
            forestHeight: 10,
            forestWidth: 10,
            firePropagationProbability: 0.5,
            fireInitialPositions: [[0, 0], [5, 5]]
        };
        this.forest = [];
        this.step = 0;
        this.running = false;
        this.animationId = null;
        this.cellSize = 30;
        
        // Initialize
        this.initEventListeners();
        this.initForest();
        this.drawForest();
    }
    
    // Initialize event listeners
    initEventListeners() {
        this.startBtn.addEventListener('click', () => this.toggleSimulation());
        this.stepBtn.addEventListener('click', () => this.executeStep());
        this.resetBtn.addEventListener('click', () => this.resetSimulation());
        this.applyBtn.addEventListener('click', () => this.applyConfiguration());
        
        // Resize canvas when window resizes
        window.addEventListener('resize', () => this.resizeCanvas());
    }
    
    // Initialize forest grid
    initForest() {
        this.forest = [];
        for (let i = 0; i < this.config.forestHeight; i++) {
            const row = [];
            for (let j = 0; j < this.config.forestWidth; j++) {
                row.push(CellState.TREE);
            }
            this.forest.push(row);
        }
        
        // Set initial fire positions
        this.config.fireInitialPositions.forEach(pos => {
            const [row, col] = pos;
            if (row >= 0 && row < this.config.forestHeight && 
                col >= 0 && col < this.config.forestWidth) {
                this.forest[row][col] = CellState.FIRE;
            }
        });
        
        this.step = 0;
        this.updateStatus();
        this.resizeCanvas();
    }
    
    // Resize canvas based on forest dimensions
    resizeCanvas() {
        const containerWidth = this.canvas.parentElement.clientWidth;
        this.cellSize = Math.min(30, Math.floor(containerWidth / this.config.forestWidth));
        
        this.canvas.width = this.config.forestWidth * this.cellSize;
        this.canvas.height = this.config.forestHeight * this.cellSize;
        
        this.drawForest();
    }
    
    // Draw the forest grid
    drawForest() {
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        
        for (let i = 0; i < this.forest.length; i++) {
            for (let j = 0; j < this.forest[i].length; j++) {
                const cellState = this.forest[i][j];
                
                // Set fill color based on cell state
                switch (cellState) {
                    case CellState.TREE:
                        this.ctx.fillStyle = '#2ecc71'; // Green
                        break;
                    case CellState.FIRE:
                        this.ctx.fillStyle = '#e74c3c'; // Red
                        break;
                    case CellState.ASH:
                        this.ctx.fillStyle = '#95a5a6'; // Gray
                        break;
                }
                
                // Draw cell
                this.ctx.fillRect(
                    j * this.cellSize, 
                    i * this.cellSize, 
                    this.cellSize, 
                    this.cellSize
                );
                
                // Draw cell border
                this.ctx.strokeStyle = '#34495e';
                this.ctx.strokeRect(
                    j * this.cellSize, 
                    i * this.cellSize, 
                    this.cellSize, 
                    this.cellSize
                );
            }
        }
    }
    
    // Execute one step of the simulation
    executeStep() {
        if (this.isSimulationComplete()) {
            this.running = false;
            this.startBtn.textContent = 'Start';
            this.updateStatus('Simulation complete');
            return false;
        }
        
        const newForest = [];
        let fireCount = 0;
        
        // Create a copy of the current forest
        for (let i = 0; i < this.forest.length; i++) {
            newForest[i] = [...this.forest[i]];
        }
        
        // Apply simulation rules
        for (let i = 0; i < this.forest.length; i++) {
            for (let j = 0; j < this.forest[i].length; j++) {
                const currentState = this.forest[i][j];
                
                if (currentState === CellState.FIRE) {
                    // Rule 1: Fire becomes ash
                    newForest[i][j] = CellState.ASH;
                    
                    // Rule 2: Fire spreads to adjacent trees with probability p
                    this.spreadFire(newForest, i, j);
                }
                
                if (newForest[i][j] === CellState.FIRE) {
                    fireCount++;
                }
            }
        }
        
        this.forest = newForest;
        this.step++;
        this.updateStatus(fireCount > 0 ? `Step ${this.step}` : 'Simulation complete');
        this.drawForest();
        
        return fireCount > 0;
    }
    
    // Spread fire to adjacent cells
    spreadFire(newForest, row, col) {
        const directions = [
            [-1, 0], // Up
            [0, 1],  // Right
            [1, 0],  // Down
            [0, -1]  // Left
        ];
        
        directions.forEach(dir => {
            const newRow = row + dir[0];
            const newCol = col + dir[1];
            
            // Check if the adjacent cell is within bounds
            if (newRow >= 0 && newRow < this.forest.length && 
                newCol >= 0 && newCol < this.forest[0].length) {
                
                // Check if the adjacent cell is a tree
                if (this.forest[newRow][newCol] === CellState.TREE) {
                    // Spread fire with probability p
                    if (Math.random() < this.config.firePropagationProbability) {
                        newForest[newRow][newCol] = CellState.FIRE;
                    }
                }
            }
        });
    }
    
    // Check if simulation is complete (no more fire)
    isSimulationComplete() {
        for (let i = 0; i < this.forest.length; i++) {
            for (let j = 0; j < this.forest[i].length; j++) {
                if (this.forest[i][j] === CellState.FIRE) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // Toggle simulation (start/stop)
    toggleSimulation() {
        this.running = !this.running;
        this.startBtn.textContent = this.running ? 'Stop' : 'Start';
        
        if (this.running) {
            this.runSimulation();
        } else {
            cancelAnimationFrame(this.animationId);
        }
    }
    
    // Run simulation continuously
    runSimulation() {
        const speed = parseInt(this.speedSlider.value);
        const delay = 1000 / speed;
        
        let lastTime = 0;
        
        const animate = (currentTime) => {
            if (!this.running) return;
            
            const elapsed = currentTime - lastTime;
            
            if (elapsed > delay) {
                lastTime = currentTime;
                const hasMoreSteps = this.executeStep();
                
                if (!hasMoreSteps) {
                    this.running = false;
                    this.startBtn.textContent = 'Start';
                    return;
                }
            }
            
            this.animationId = requestAnimationFrame(animate);
        };
        
        this.animationId = requestAnimationFrame(animate);
    }
    
    // Reset simulation
    resetSimulation() {
        this.running = false;
        this.startBtn.textContent = 'Start';
        cancelAnimationFrame(this.animationId);
        
        this.initForest();
        this.drawForest();
    }
    
    // Apply configuration changes
    applyConfiguration() {
        // Parse input values
        const height = parseInt(this.forestHeightInput.value);
        const width = parseInt(this.forestWidthInput.value);
        const probability = parseFloat(this.fireProbabilityInput.value);
        const positions = this.parseInitialPositions(this.initialPositionsInput.value);
        
        // Validate input
        if (isNaN(height) || height < 5 || height > 100 ||
            isNaN(width) || width < 5 || width > 100 ||
            isNaN(probability) || probability < 0 || probability > 1 ||
            !positions) {
            alert('Please enter valid configuration values.');
            return;
        }
        
        // Update configuration
        this.config.forestHeight = height;
        this.config.forestWidth = width;
        this.config.firePropagationProbability = probability;
        this.config.fireInitialPositions = positions;
        
        // Reset and initialize with new configuration
        this.resetSimulation();
    }
    
    // Parse initial positions string (format: row1,col1;row2,col2;...)
    parseInitialPositions(positionsStr) {
        try {
            return positionsStr.split(';')
                .map(pos => pos.trim())
                .filter(pos => pos.length > 0)
                .map(pos => {
                    const [row, col] = pos.split(',').map(num => parseInt(num.trim()));
                    if (isNaN(row) || isNaN(col)) {
                        throw new Error('Invalid position format');
                    }
                    return [row, col];
                });
        } catch (e) {
            alert('Invalid initial positions format. Please use format: row1,col1;row2,col2;...');
            return null;
        }
    }
    
    // Update status text
    updateStatus(message) {
        this.statusText.textContent = message || `Ready (Step ${this.step})`;
    }
}

// Initialize simulation when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    const simulation = new ForestFireSimulation();
});
