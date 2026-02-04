package frc.robot.usecase.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.domain.repository.ExtenderRepository;

public class ExtenderCommands {
    private static ExtenderRepository ExtenderRepository;

    public static void init(ExtenderRepository ex) {
        ExtenderRepository = ex;
    }

    public static Command templateCommand() {
        return ExtenderRepository.run(()->{});
    }
}
