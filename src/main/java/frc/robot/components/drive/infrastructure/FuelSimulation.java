package frc.robot.components.drive.infrastructure;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.usecase.commands.CommandsGroup;
import frc.robot.util.FuelSim;


public class FuelSimulation {

    private final FuelSim fuelSim;

    private int fuelStored = 0;
    private static final int CAPACITY = 50;

    public FuelSimulation (FuelSim f){
        this.fuelSim = f;
    }

    public void configureFuelSim() {
        fuelSim.spawnStartingFuel();
        fuelSim.start();
        SmartDashboard.putData(Commands.runOnce(() -> {
                    fuelSim.clearFuel();
                    fuelSim.spawnStartingFuel();
                })
                .withName("Reset Fuel")
                .ignoringDisable(true));
        
        SmartDashboard.putData(
            Commands.defer(
                () -> CommandsGroup.shoot(),
                java.util.Set.of()
            )
            .withName("Shoot")
            .ignoringDisable(true)
        );
    }

    public void launchFuel() {
        if (fuelStored == 0) return;
            fuelStored--;

            fuelSim.launchFuel(
                Units.MetersPerSecond.of(6.5), 
                Units.Degrees.of(50),
                Units.Degrees.of(0),
                Units.Meters.of(0.45)
            );
    }

    public boolean canIntake() {
        return fuelStored < CAPACITY;
    }

    public void intakeFuel() {
        fuelStored++;
    }

    public int getFuelStored(){
        return fuelStored;
    }
}
