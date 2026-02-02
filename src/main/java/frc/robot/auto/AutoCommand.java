package frc.robot.auto;

import com.pathplanner.lib.commands.PathPlannerAuto;
import edu.wpi.first.wpilibj2.command.Command;

public class AutoCommand {
    public static Command getAutonomousCommand() {
        return new PathPlannerAuto("test");
    }
}
