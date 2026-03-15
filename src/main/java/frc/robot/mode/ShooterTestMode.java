package frc.robot.mode;

import frc.robot.RobotContainer;
import frc.robot.usecase.commands.ShooterCommands;

public class ShooterTestMode extends Mode {

    static void configureDefault() {
        RobotContainer.getShooterInstance().setDefaultCommand(
            ShooterCommands.stopShooter()
        );
    }

    public static void configureBindings() {
        configureDefault();

        driveController.a().whileTrue(ShooterCommands.shootToHub());
        driveController.b().whileTrue(ShooterCommands.feed());
        driveController.x().whileTrue(ShooterCommands.reverseShooter());

        driveController.leftTrigger(0.6).whileTrue(ShooterCommands.reverseShooter());
        driveController.rightTrigger(0.6).whileTrue(ShooterCommands.shootToHub());
    }
}