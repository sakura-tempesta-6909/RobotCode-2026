package frc.robot.usecase.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.domain.repository.LEDRepository;

public class LEDCommands {
    private static LEDRepository LEDRepository;

    public static void init(LEDRepository led) {
        LEDRepository = led;
    }

    public static Command templateCommand() {
        return LEDRepository.run(()->{});
    }
}
