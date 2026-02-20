package frc.robot.mode;

import frc.robot.RobotContainer;
import frc.robot.domain.option.DriveOption;
import frc.robot.domain.option.DriveOption.DriveOriented;
import frc.robot.domain.option.DriveOption.DriveSpeed;
import frc.robot.usecase.commands.DriveCommands;
import frc.robot.usecase.commands.ShooterCommands;


public class DriveMode extends Mode {
    static void configureDefault() {
        RobotContainer.getDriveInstance().setDefaultCommand(
            DriveCommands.ManualDrive(
                () -> -driveController.getLeftY(),
                () -> -driveController.getLeftX(),
                () -> -driveController.getRightX()
            )
        );
        
        DriveOption.driveOriented.setDefault(DriveOriented.s_fieldOriented);
        DriveOption.driveSpeed.setDefault(DriveSpeed.s_fastDrive);

    }

    public static void configureBindings() {
        configureDefault();

        driveController.rightBumper().whileTrue(DriveOption.driveOriented.set(DriveOriented.s_robotOriented));

                
        operateController.a().onTrue(ShooterCommands.shootToHub(() -> 1.0));
        operateController.b().onTrue(ShooterCommands.feed(() -> 0.8));
        operateController.x().onTrue(ShooterCommands.reverseShooter(() -> -0.4));
        operateController.y().onTrue(ShooterCommands.moveShooterSpecifiedSpeed(() -> 0.5));

        //実験用に
        operateController.leftBumper().onTrue(ShooterCommands.moveShooterSpecifiedSpeed(() -> 0.3));
        operateController.rightBumper().onTrue(ShooterCommands.moveShooterSpecifiedSpeed(() -> 0.7));
        operateController.leftTrigger().whileTrue(ShooterCommands.reverseShooter(() -> -0.4));
        operateController.rightTrigger().whileTrue(ShooterCommands.shootToHub(() -> 1.0));
    }
}
