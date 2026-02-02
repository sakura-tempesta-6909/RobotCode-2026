package frc.robot.usecase.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.domain.state.StateGroup;

public class CommandsGroup {
    public static Command intake() {
        return TemplateCommands.templateCommand().withName("intake").finallyDo(() -> {System.out.println("endeee");});
    }


    public static Command score() {
        return new SequentialCommandGroup(
            new ParallelCommandGroup(DriveCommands.FollowGoToGoal(), ExampleCommands.moveToGoal()).withTimeout(10),
            TemplateCommands.templateCommand()
        );
    }

    public static Command readytoScore() {
        return new ConditionalCommand(CommandsGroup.score(), new InstantCommand(), StateGroup::readyToScore);
    }
}
