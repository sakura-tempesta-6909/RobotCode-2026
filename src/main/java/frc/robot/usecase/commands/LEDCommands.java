package frc.robot.usecase.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.domain.repository.TemplateRepository;

public class LEDCommands {
    private static TemplateRepository templateRepository;

    public static void init(TemplateRepository tr) {
        templateRepository = tr;
    }

    public static Command templateCommand() {
        return templateRepository.run(()->{});
    }
}
