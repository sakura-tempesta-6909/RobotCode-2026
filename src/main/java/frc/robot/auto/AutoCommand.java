package frc.robot.auto;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.usecase.commands.DriveCommands;

public class AutoCommand {
    private static SendableChooser<Command> autoChooser;

    public static void buildAutoChooser() {
        autoChooser = AutoBuilder.buildAutoChooser();
        autoChooser.addOption("qua for", DriveCommands.dynamicReverse());
        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    public static Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
}