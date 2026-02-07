package frc.robot.usecase.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.domain.option.LEDOption;
import frc.robot.domain.repository.LEDRepository;

public class LEDCommands {
    private static LEDRepository LEDRepository;

    public static void init(LEDRepository led) {
        LEDRepository = led;
    }

    public static Command setState() {
        return LEDRepository.run(()->{
            switch (LEDOption.ledStateOption.get()) {
                case s_readyToShoot:
                    LEDRepository.flashLight(0, 255, 0);
                    break;
                case s_shootablePosition:
                    LEDRepository.flashLight(255, 0, 0);
                    break;
                case s_shootableSpeed:
                    LEDRepository.flashLight(0, 0, 255);
                    break;
                case s_readyToIntake:
                    LEDRepository.changeLight(255, 0, 255);
                    break;
                case s_disable:
                    default:
                    LEDRepository.changeLight(255, 100, 0);
                    break;
            }
        });
    }
}
