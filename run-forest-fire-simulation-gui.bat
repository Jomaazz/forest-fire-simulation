@echo off
echo Forest Fire Simulation Launcher (GUI Mode)
echo =======================================
echo.
echo This batch file will help you run the Forest Fire Simulation application with a graphical user interface.
echo.

REM Navigate to the project directory
cd backend\forest-fire-simulation

REM Clean and compile the project
echo Building the project...
call mvn clean compile
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to build the project.
    echo.
    pause
    exit /b 1
)

REM Run the simulation in GUI mode
echo.
echo Starting the Forest Fire Simulation GUI...
echo.
call mvn exec:java "-Dexec.mainClass=com.forestfire.App" "-Dexec.args=--gui"

REM Return to the original directory
cd ..\..

echo.
echo Simulation ended.
pause
