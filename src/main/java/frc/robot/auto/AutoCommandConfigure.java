package frc.robot.auto;

import com.pathplanner.lib.auto.NamedCommands;

import frc.robot.usecase.commands.CommandsGroup;

public class AutoCommandConfigure {
    public static void registerCommands() {
        NamedCommands.registerCommand("Intake", CommandsGroup.intake());
        NamedCommands.registerCommand("Score", CommandsGroup.score());
        NamedCommands.registerCommand("ReadytoScore", CommandsGroup.readytoScore());
    }
}
