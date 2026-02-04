package frc.robot.usecase.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.domain.repository.VisionRepository;

public class VisionCommands {
    private static VisionRepository VisionRepository;

    public static void init(VisionRepository vi) {
        VisionRepository = vi;
    }

    public static Command templateCommand() {
        return VisionRepository.run(()->{});
    }
}
