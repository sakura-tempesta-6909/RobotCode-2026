package frc.robot.usecase.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.components.shooter.infrastructure.Shooter;
import frc.robot.domain.repository.ShooterRepository;
import frc.robot.components.shooter.ShooterConst;

public class ShooterCommands {
    private static ShooterRepository ShooterRepository;

    public static void init(ShooterRepository sh) {
        ShooterRepository = sh;
    }

    public static Command moveShooterSpecifiedSpeed(DoubleSupplier supplier){
        return ShooterRepository.startEnd(
            () -> ShooterRepository.moveShooterSpecifiedSpeed(supplier.getAsDouble() * 60 / 3.14 / ShooterConst.WheelDiameter),
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
        return moveShooterSpecifiedSpeed(() -> 20);
    }

    /**
     *  自アライアンス側エリアにボールを投げ入れる
     */
    public static Command feed() {
        return ShooterRepository.startEnd(
            () -> ShooterRepository.moveShooterSpecifiedSpeed(0.7),
            () -> {
                ShooterRepository.moveShooterSpecifiedSpeed(0);
                ShooterRepository.resetPID();
            }
        );
    }

    /** 
     * 詰まり解消のための逆回転
     * 負の数を入れる
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
