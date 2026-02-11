package frc.robot.domain.repository;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface ShooterRepository extends Subsystem {

    /** 
     * Velocity制御でモーターを任意の速度で動かす 
     * 範囲：-1～1　（負方向の最大回転数割合―1～正方向の最大回転数割合1まで）
     * 0で停止
    */
    void moveShooterSpecifiedSpeed(double targetSpeed);

    /** 
     * PercentOutputでモーターを任意の速度で動かす
     * 範囲：-1～1 （負方向の最大出力割合―1～正方向の最大出力割合1まで）
     * 0で停止
     */
    void moveShooterSpecifedPower(double targetPower);

    /** PIDのリセット */
    void resetPID();

}
