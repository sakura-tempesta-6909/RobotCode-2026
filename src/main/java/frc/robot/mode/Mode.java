package frc.robot.mode;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.RobotContainer.ModeType;

public abstract class Mode {
    static CommandXboxController driveController, operateController;

    public static void setupMode() {
        driveController = new CommandXboxController(0);
        operateController = new CommandXboxController(1);
    }

    public static void configureModeBindings() {
        driveController.back().onTrue(getSwitchModeCommand(ModeType.k_drive));
        driveController.start().onTrue(getSwitchModeCommand(ModeType.k_example));
    }

    private static Command getSwitchModeCommand(ModeType mode) {
        return new InstantCommand(() ->{
            Robot.modeResetter = () -> {
                CommandScheduler.getInstance().cancelAll();
                CommandScheduler.getInstance().getActiveButtonLoop().clear();   
                RobotContainer.mode = mode;
    
                Mode.configureModeBindings();
                mode.configureBindings();
            };
        });
    }
}