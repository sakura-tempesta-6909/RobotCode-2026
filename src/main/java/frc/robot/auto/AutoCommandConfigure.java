package frc.robot.auto;

import com.pathplanner.lib.auto.NamedCommands;

import frc.robot.Robot;
import frc.robot.domain.state.ExtenderState;
import frc.robot.usecase.commands.CommandsGroup;
import frc.robot.usecase.commands.ExtenderCommands;

public class AutoCommandConfigure {
    public static void registerCommands() {
        NamedCommands.registerCommand("Intake", CommandsGroup.intake());
        NamedCommands.registerCommand("shoot", CommandsGroup.shoot());
        NamedCommands.registerCommand("move to intake angle", ExtenderCommands.moveToIntakeAngle().until(() -> ExtenderState.isIntakePosition || Robot.isSimulation()));
    }
}
