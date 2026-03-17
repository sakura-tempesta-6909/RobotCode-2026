package frc.robot.usecase.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.domain.repository.IntakeRepository;
import frc.robot.components.intake.IntakeParameter;

public class IntakeCommands {
    private static IntakeRepository IntakeRepository;

    public static void init(IntakeRepository intake) {
        IntakeRepository = intake;
    }

    public static Command intakeFuel() {
        return IntakeRepository.runEnd(() -> {
            IntakeRepository.moveIntakeSpecifiedSpeed(IntakeParameter.Speed.IntakeRollerSpeed);
        },() -> 
            IntakeRepository.moveIntakeSpecifiedSpeed(IntakeParameter.Speed.Neutral));
    }
    public static Command outtakeFuel() {
        return IntakeRepository.runEnd(() -> {
                IntakeRepository.moveIntakeSpecifiedSpeed(IntakeParameter.Speed.OuttakeRollerSpeed);
        },() ->
            IntakeRepository.moveIntakeSpecifiedSpeed(IntakeParameter.Speed.Neutral));
    }
    public static Command stopIntake(){
        return IntakeRepository.run(() -> {
            IntakeRepository.moveIntakeSpecifiedSpeed(IntakeParameter.Speed.Neutral);
        });
    }
}