package frc.robot.usecase.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.domain.repository.IntakeRepository;

public class IntakeCommands {
    private static IntakeRepository IntakeRepository;

    public static void init(IntakeRepository intake) {
        IntakeRepository = intake;
    }

    public static Command templateCommand() {
        return IntakeRepository.run(()->{});
    }
}
