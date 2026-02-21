package frc.robot.usecase.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.components.shooter.infrastructure.Shooter;
import frc.robot.domain.repository.ShooterRepository;

public class ShooterCommands {
    private static ShooterRepository ShooterRepository;

    public static void init(ShooterRepository sh) {
        ShooterRepository = sh;
    }

    public static Command moveShooterSpecifiedSpeed(DoubleSupplier supplier){
        return ShooterRepository.startEnd(
            () -> ShooterRepository.moveShooterSpecifiedSpeed(supplier.getAsDouble()),
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
        return moveShooterSpecifiedSpeed(() -> 1.0);
    }

    /**
     *  自アライアンス側にフィードする
     */
    public static Command feed() {
        return moveShooterSpecifiedSpeed(() -> 0.7);
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
