package frc.robot.mode;

import frc.robot.RobotContainer;
import frc.robot.domain.option.DriveOption;
import frc.robot.domain.option.ExampleOption;
import frc.robot.domain.option.DriveOption.DriveOriented;
import frc.robot.domain.option.DriveOption.DriveSpeed;
import frc.robot.domain.option.ExampleOption.SpeedOption;
import frc.robot.usecase.commands.CommandsGroup;
import frc.robot.usecase.commands.DriveCommands;
import frc.robot.usecase.commands.ExampleCommands;
import frc.robot.usecase.commands.TemplateCommands;

public class ExampleMode extends Mode {
    static void configureDefault() {
        RobotContainer.getDriveInstance().setDefaultCommand(
            DriveCommands.ManualDrive(
                () -> -driveController.getLeftY(),
                () -> -driveController.getLeftX(),
                () -> -driveController.getRightX()
            )
            );
        
        RobotContainer.getExampleInstance().setDefaultCommand(
            ExampleCommands.keepCurrentPosition()
        );


        RobotContainer.getTemplateInstance().setDefaultCommand(
            TemplateCommands.templateCommand()
        );
        
        DriveOption.driveOriented.setDefault(DriveOriented.s_fieldOriented);
        DriveOption.driveSpeed.setDefault(DriveSpeed.s_fastDrive);

        ExampleOption.speedOption.setDefault(SpeedOption.k_fast);
    }

    public static void configureBindings() {
        configureDefault();

        driveController.rightBumper().whileTrue(DriveOption.driveOriented.set(DriveOriented.s_robotOriented));

        driveController.y().whileTrue(CommandsGroup.intake());
        driveController.b().whileTrue(CommandsGroup.score());

        driveController.leftTrigger(0.6).whileTrue(DriveOption.driveSpeed.set(DriveSpeed.s_slowDrive));
        driveController.rightTrigger(0.6).whileTrue(DriveOption.driveSpeed.set(DriveSpeed.s_midDrive));

        driveController.a().whileTrue(CommandsGroup.readytoScore());

        driveController.povUp().onTrue(DriveCommands.resetGyroSensor());
    }
}
