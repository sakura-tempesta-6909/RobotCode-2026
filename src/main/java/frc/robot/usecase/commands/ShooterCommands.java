package frc.robot.usecase.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.components.shooter.infrastructure.Shooter;
import frc.robot.domain.repository.ShooterRepository;
import frc.robot.domain.state.ShooterState;
import frc.robot.components.shooter.ShooterConst;
import frc.robot.components.shooter.ShooterParameter;
import frc.robot.components.shooter.ShooterTools;

public class ShooterCommands {
    private static ShooterRepository ShooterRepository;

    public static void init(ShooterRepository sh) {
        ShooterRepository = sh;
    }

    /**
    * シューターを指定の表面速度で回す
    * @param supplier : 目標の表面速度(m/s)
    */
    public static Command moveShooterSpecifiedSpeed(DoubleSupplier supplier){
        return ShooterRepository.startEnd(
            () -> {
                ShooterRepository.resetPID();
                ShooterRepository.moveShooterSpecifiedSpeed(supplier.getAsDouble());
                ShooterState.targetMotorSpeed = supplier.getAsDouble();
            },
            () -> ShooterRepository.moveShooterSpecifiedPower(0)
        );
    }

    /**
     * ハブへシュート
     */
    public static Command shootToHub() {
        return moveShooterSpecifiedSpeed(() -> {
            double distance = 0.0; //ここで距離を取得する
            return ShooterTools.distanceToMps(distance);
        });
    }

    /**
     *  自アライアンス側エリアにボールを投げ入れる
     */
    public static Command feed() {
        return ShooterRepository.startEnd(
            () -> ShooterRepository.moveShooterSpecifiedSpeed((ShooterParameter.feedMps)),
            () -> {
                ShooterRepository.moveShooterSpecifiedPower(0);
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
        return ShooterRepository.run(
            () -> ShooterRepository.moveShooterSpecifiedPower(0.0)
        );
    }
}
