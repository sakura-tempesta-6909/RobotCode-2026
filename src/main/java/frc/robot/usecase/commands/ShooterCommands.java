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
     *  指定した目標値（supplier）の速度でシューターを動かす
     *  @param supplier シューターの目標値　単位はm/s
     */
    public static Command moveShooterSpecifiedSpeed(DoubleSupplier supplier){
        return ShooterRepository.startEnd(
            () -> ShooterRepository.moveShooterSpecifiedSpeed(
                ShooterTools.MpsToRPM(supplier.getAsDouble())),
            () -> {
                ShooterRepository.moveShooterSpecifiedSpeed(0);
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
                (ShooterTools.MpsToRPM(ShooterParameter.feedSpeed))),
            () -> {
                ShooterRepository.moveShooterSpecifiedSpeed(0);
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
            () -> ShooterRepository.moveShooterSpecifedPower(-0.7),
            () -> {
                ShooterRepository.moveShooterSpecifedPower(0.0);
            }
        );
    }
}
