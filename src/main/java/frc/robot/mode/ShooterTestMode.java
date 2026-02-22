package frc.robot.mode;

import frc.robot.usecase.commands.ShooterCommands;


public class ShooterTestMode extends Mode {
    static void configureDefault() {

    }

    public static void configureBindings() {
        configureDefault();

        operateController.a().onTrue(ShooterCommands.shootToHub());
        operateController.b().onTrue(ShooterCommands.feed());
        operateController.x().onTrue(ShooterCommands.reverseShooter());

        operateController.leftTrigger().whileTrue(ShooterCommands.reverseShooter());
        operateController.rightTrigger().whileTrue(ShooterCommands.shootToHub());
    }
}