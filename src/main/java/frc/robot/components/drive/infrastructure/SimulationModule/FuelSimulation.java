package frc.robot.components.drive.infrastructure.SimulationModule;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.usecase.commands.CommandsGroup;


public class FuelSimulation {

    private final FuelSim fuelSim;

    private int fuelStored = 0;
    private static final int CAPACITY = 50;

    public FuelSimulation (FuelSim f){
        this.fuelSim = f;
    }

    public void configureFuelSim() {
        fuelSim.enableAirResistance();
        fuelSim.spawnStartingFuel();
        fuelSim.start();
        SmartDashboard.putData(Commands.runOnce(() -> {
                    FuelSim.Hub.BLUE_HUB.resetScore();
                    FuelSim.Hub.RED_HUB.resetScore();
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

    public void launchFuel(double launchPower) {
        if (fuelStored == 0) return;
            fuelStored--;

            fuelSim.launchFuel(
                Units.MetersPerSecond.of(launchPower), 
                SimulationConst.Shooter.hoodAngle,
                SimulationConst.Shooter.turretYaw,
                SimulationConst.Shooter.shooterOffset
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

    public int getBlueScore(){
        return FuelSim.Hub.BLUE_HUB.getScore();
    }

    public int getRedScore(){
        return FuelSim.Hub.RED_HUB.getScore();
    }
}
