package frc.robot.domain.repository;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface ShooterRepository extends Subsystem {

    /** 
     * Velocity制御でモーターを任意の速度で動かす 
     * @param targetRPM モーター回転数 [RPM]
    */
    void moveShooterSpecifiedSpeed(double targetSpeed);

    /** 
     * PercentOutputでモーターを任意の速度で動かす
     * @param targetPower 割合の範囲 [-1.0 - 1.0]
     */
    void moveShooterSpecifiedPower(double targetPower);

    /** PIDのリセット */
    void resetPID();

}
