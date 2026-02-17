package frc.robot.usecase.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.components.extender.ExtenderParameter;
import frc.robot.domain.repository.ExtenderRepository;

public class ExtenderCommands {
    private static ExtenderRepository ExtenderRepository;

    public static void init(ExtenderRepository ex) {
        ExtenderRepository = ex;
    }

    public static Command templateCommand() {
        return ExtenderRepository.run(()->{});
    }

    /** Extenderを特定の角度に動かす
     * 目標の角度に到達したら終了
     */
    public static Command moveExtenderSpecifiedAngle(DoubleSupplier targetSupplier) {
        return ExtenderRepository.startRun(()->{
            ExtenderRepository.resetPID();
        },()->{

            ExtenderRepository.moveExtenderSpecifiedAngle(targetSupplier.getAsDouble());
        });
    }
    
    public static Command moveToIntakeAngle(){
        return moveExtenderSpecifiedAngle(()->(ExtenderParameter.IntakeAngle));
    }

    public static Command moveToInitialAngle(){
        return moveExtenderSpecifiedAngle(()->(ExtenderParameter.InitialAngle));
    }
}
