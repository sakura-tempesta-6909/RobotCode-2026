package frc.robot.usecase.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.components.shooter.infrastructure.Shooter;
import frc.robot.domain.repository.ShooterRepository;
import frc.robot.components.shooter.ShooterConst;
import frc.robot.components.shooter.ShooterParameter;
import frc.robot.components.shooter.ShooterTools;

public class ShooterCommands {
    private static ShooterRepository ShooterRepository;

    public static void init(ShooterRepository sh) {
        ShooterRepository = sh;
    }

    /**
    * ratio値をRPMに変換してシューターを回転させる
    * @param supplier : ratio [-1.0, 1.0]
    *  1.0  → 最大正転RPM
    *  0.0  → 停止
    * -1.0  → 最大逆転RPM
    * コマンド終了時 -> モーター停止&PIDリセット
    */
    public static Command moveShooterSpecifiedSpeed(DoubleSupplier supplier){
        return ShooterRepository.startEnd(
            () -> ShooterRepository.moveShooterSpecifiedSpeed(
                ShooterTools.RatioToRPM(supplier.getAsDouble())),
            () -> {
                ShooterRepository.moveShooterSpecifiedPower(0);
                ShooterRepository.resetPID();
            }
        );
    }

    /**
     *  ハブへシュート
     */
    public static Command shootToHub() {
        return moveShooterSpecifiedSpeed(() -> ShooterParameter.shootRatio);
    }

    /**
     *  自アライアンス側エリアにボールを投げ入れる
     */
    public static Command feed() {
        return ShooterRepository.startEnd(
            () -> ShooterRepository.moveShooterSpecifiedSpeed(
                (ShooterTools.RatioToRPM(ShooterParameter.feedRatio))),
            () -> {
                ShooterRepository.moveShooterSpecifiedPower(0);
                ShooterRepository.resetPID();
            }
        );
    }

    /** 
     * 詰まり解消のための逆回転
     * 適当な負の数を入れる（PercentOutput）
     */
    public static Command reverseShooter() {
        return ShooterRepository.runEnd(
            () -> ShooterRepository.moveShooterSpecifiedPower(ShooterParameter.reverseOutput),
            () -> {
                ShooterRepository.moveShooterSpecifiedPower(0.0);
            }
        );
    }

    /** 
     * モーターを停止させるだけ
     */
    public static Command stopShooter() {
        return ShooterRepository.runOnce(
            () -> ShooterRepository.moveShooterSpecifiedPower(0.0)
        );
    }
}
