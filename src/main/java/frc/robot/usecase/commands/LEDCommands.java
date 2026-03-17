package frc.robot.usecase.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.domain.repository.LEDRepository;
import frc.robot.domain.state.DriveState;
import frc.robot.domain.state.ExtenderState;
import frc.robot.domain.state.ShooterState;
import frc.robot.domain.state.StateGroup;

public class LEDCommands {
    private static LEDRepository LEDRepository;

    public static void init(LEDRepository led) {
        LEDRepository = led;
    }

    public static Command set() {
        // TODO MUDに配慮したRGBにあとでなおす
        return LEDRepository.run(()->{
            if (DriverStation.isDisabled()) {
                LEDRepository.changeLight(255, 100, 0);
            } else if (StateGroup.readyToShoot()) {
                LEDRepository.flashLight(0, 255, 0);
            } else if (DriveState.isShootPosition) {
                LEDRepository.flashLight(255, 0, 0);
            } else if (ShooterState.isReadyToShoot) {
                LEDRepository.flashLight(0, 0, 255);
            } else if (ExtenderState.isIntakePosition) {
                LEDRepository.changeLight(120, 0, 220);
            } else if (DriverStation.isEnabled()) {
                LEDRepository.changeLight(0, 255, 0);
            } else {
                LEDRepository.changeLight(0, 0, 0);
            }
        });
    }
}
