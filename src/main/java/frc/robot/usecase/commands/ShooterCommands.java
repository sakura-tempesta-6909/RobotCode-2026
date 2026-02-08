package frc.robot.usecase.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.domain.repository.ShooterRepository;

public class ShooterCommands {
    private static ShooterRepository ShooterRepository;

    public static void init(ShooterRepository sh) {
        ShooterRepository = sh;
    }

    public static Command moveShooterSpecifiedSpeed(DoubleSupplier targetSupplier) {
        return ShooterRepository.startEnd(() -> ShooterRepository.);
    }
}
