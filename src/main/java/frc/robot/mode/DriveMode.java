package frc.robot.mode;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

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
                
     /**
       *operateController.a().onTrue(ShooterCommands.shootToHub());
        operateController.b().onTrue(ShooterCommands.feed());
        operateController.x().onTrue(ShooterCommands.reverseShooter());

        //実験用に
        operateController.leftTrigger().whileTrue(ShooterCommands.reverseShooter());
        operateController.rightTrigger().whileTrue(ShooterCommands.shootToHub());
       */
    }
}
