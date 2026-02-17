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

    public static Command moveShooterSpecifiedSpeed(DoubleSupplier targetSupplier){
        return ShooterRepository.startEnd(
            () -> ShooterRepository.moveShooterSpecifiedSpeed(targetSupplier.getAsDouble()),
            () -> {
                ShooterRepository.moveShooterSpecifiedSpeed(0);
                ShooterRepository.resetPID();
            }
        );
    }

    /**
     *  ハブへシュート
     */
    public static Command shootToHub(DoubleSupplier targetSupplier) {
        return moveShooterSpecifiedSpeed(targetSupplier);
    }

    /**
     *  自アライアンス側にフィードする
     */
    public static Command feed(DoubleSupplier targetSupplier) {
        return moveShooterSpecifiedSpeed(targetSupplier);
    }

    /** 
     * 詰まり解消のための逆回転
     * 負の数を入れる
     */
    public static Command reverseShooter(DoubleSupplier targetSupplier) {
        return ShooterRepository.runEnd(
            () -> ShooterRepository.moveShooterSpecifedPower(-0.4),
            () -> {
                ShooterRepository.moveShooterSpecifedPower(0.0);
                ShooterRepository.resetPID();
            }
        );
    }
}
