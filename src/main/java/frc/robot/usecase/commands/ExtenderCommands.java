package frc.robot.usecase.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.components.extender.ExtenderParameter;
import frc.robot.components.intake.IntakeParameter;
import frc.robot.domain.repository.ExtenderRepository;
import frc.robot.domain.repository.IntakeRepository;
import frc.robot.usecase.UsecaseConst;

public class ExtenderCommands {
    private static ExtenderRepository ExtenderRepository;

    public static void init(ExtenderRepository ex) {
        ExtenderRepository = ex;
    }

    public static Command resetEncoder() {
        return new InstantCommand(() -> ExtenderRepository.resetEncoder(90.0));
    }

    public static Command resetPID() {
        return new InstantCommand(ExtenderRepository::resetPID);
    }

    public static Command templateCommand() {
        return ExtenderRepository.run(()->{});
    }

    /** Extenderを特定の角度に動かす
     * 目標の角度に到達したら終了
     * @param targetSupplier 目標の角度[degree]
     */
    public static Command moveExtenderSpecifiedAngle(DoubleSupplier targetSupplier) {
        return ExtenderRepository.startRun(()->{
            ExtenderRepository.resetPID();
        },()->{
            ExtenderRepository.moveExtenderSpecifiedAngle(targetSupplier.getAsDouble());
        });
    }
    
    /** Extenderを一定の力で動かす
     * @param targetPower 目標の力[percentooutput]
     */
    public static Command moveExtenderSpecifiedPower(double targetPower) {
        return ExtenderRepository.run(()->{
            ExtenderRepository.moveExtenderSpecifiedPower(targetPower);
        });
    }

    /** ExtenderをIntake位置方向に一定の力で動かす */
    public static Command moveExtenderMaxPowerToIntakePosition() {
        return moveExtenderSpecifiedPower(ExtenderParameter.MaxPowerToIntakePosition);
    }

    /** Extenderを上方向に一定の力で動かす */
    public static Command moveExtenderMaxPowerToInitialPosition() {
        return moveExtenderSpecifiedPower(ExtenderParameter.MaxPowerToInitialPosition);
    }
    
    /** ExtenderをIntakeの角度に動かす
     * 目標の角度に到達したら終了
     */
    public static Command moveToIntakeAngle(){
        return moveExtenderSpecifiedAngle(()->(ExtenderParameter.IntakeAngle));
    }

    /** Extenderをデフォルトの角度に動かす
     * 目標の角度に到達したら終了
     */
    public static Command moveToInitialAngle(){
        return moveExtenderSpecifiedAngle(()->(ExtenderParameter.InitialAngle));
    }

    /** Extenderの現在の角度を維持する */
    public static Command keepCurrentAngle(){
        return ExtenderRepository.run(()->{
            ExtenderRepository.keepCurrentAngle();
        });
    }

    public static Command stopExtender(){
        return ExtenderRepository.run(() -> {
            ExtenderRepository.moveExtenderSpecifiedPower(ExtenderParameter.Power.Neutral);
        });
    }


} 
