package frc.robot.auto;

import com.pathplanner.lib.auto.NamedCommands;

import frc.robot.domain.state.ExtenderState;
import frc.robot.usecase.commands.CommandsGroup;
import frc.robot.usecase.commands.ExtenderCommands;

public class AutoCommandConfigure {
    public static void registerCommands() {
        NamedCommands.registerCommand("Intake", CommandsGroup.intake().withTimeout(5.0));
        NamedCommands.registerCommand("shoot", CommandsGroup.shoot().withTimeout(7.0));
        NamedCommands.registerCommand("move to intake angle", ExtenderCommands.moveToIntakeAngle().until(() -> ExtenderState.isIntakePosition));
    }
}
