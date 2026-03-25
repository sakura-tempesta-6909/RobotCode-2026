package frc.robot.usecase.commands;

import java.util.function.DoubleSupplier;

import frc.robot.domain.state.DriveState;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.components.shooter.infrastructure.Shooter;
import frc.robot.domain.repository.ShooterRepository;
import frc.robot.domain.state.ShooterState;
import frc.robot.domain.state.StateGroup;
import frc.robot.components.shooter.ShooterConst;
import frc.robot.components.shooter.ShooterParameter;
import frc.robot.components.shooter.ShooterTools;
import frc.robot.domain.repository.ShooterRepository;

import frc.robot.domain.state.StateGroup;

import java.util.function.DoubleSupplier;

public class ShooterCommands {
    private static ShooterRepository ShooterRepository;

    public static void init(ShooterRepository sh) {
        ShooterRepository = sh;
    }

    public static Command resetPID() {
        return new InstantCommand(ShooterRepository::resetPID);
    }

    /**
    * シューターを指定の表面速度で回す
    * @param supplier : 目標の表面速度(m/s)
    */
    public static Command moveShooterSpecifiedSpeed(DoubleSupplier supplier){
        return ShooterRepository.startRun(
            () -> ShooterRepository.resetPID(),
            () -> {
                ShooterRepository.moveShooterSpecifiedSpeed(supplier.getAsDouble());
            }
        );
    }

    /**
     * ハブへシュート
     */
    public static Command shootToHub() {
        return moveShooterSpecifiedSpeed(() -> {
            double distance = StateGroup.getDistanceToHub(); //ここで距離を取得する
            double power = ShooterTools.distanceToMps(distance,DriveState.driveXYOmegaSpeed); // Fuelの初速度
            double surfaceSpeed = ShooterTools.rpmToSurfaceSpeed(ShooterTools.fuelVelocityToSurfaceRPM(power)); // wheelの表面速度
            return surfaceSpeed;
        });
    }

    /**
     *  自アライアンス側エリアにボールを投げ入れる
     */
    public static Command feed() {
    return moveShooterSpecifiedSpeed(() -> ShooterParameter.feedMps);
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