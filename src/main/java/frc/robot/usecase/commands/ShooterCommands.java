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
     *  指定した目標値（supplier）の割合でシューターを動かす
     *  @param supplier シューターの目標値　1.0で全速シュート┃-1.0で全速逆回転　0で停止
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
        return moveShooterSpecifiedSpeed(() -> ShooterParameter.shootSpeed);
    }

    /**
     *  自アライアンス側エリアにボールを投げ入れる
     */
    public static Command feed() {
        return ShooterRepository.startEnd(
            () -> ShooterRepository.moveShooterSpecifiedSpeed(
                (ShooterTools.RatioToRPM(ShooterParameter.feedSpeed))),
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
            () -> ShooterRepository.moveShooterSpecifiedPower(ShooterParameter.reverseSpeed),
            () -> {
                ShooterRepository.moveShooterSpecifiedPower(0.0);
            }
        );
    }

    /** 
     * モーターを停止させるだけ
     */
    public static Command stopShooter() {
        return ShooterRepository.startEnd(
            () -> ShooterRepository.moveShooterSpecifiedPower(0.0),
            () -> ShooterRepository.moveShooterSpecifiedPower(0.0));
    }
}
