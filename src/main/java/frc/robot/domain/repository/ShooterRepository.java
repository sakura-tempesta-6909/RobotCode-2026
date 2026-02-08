package frc.robot.domain.repository;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface ShooterRepository extends Subsystem {

    /** 
     * MaxRPMに割合をかけてVelocity制御で動かす 
     * 範囲：-1～1　（負方向の最大回転数割合―1～正方向の最大回転数割合1まで）
     * モーターの最大回転数5600RPM
    */
    void moveShooterSpecifiedSpeed(double targetSpeed);

    /** 
     * PercentOutputでモーターを任意の速度で動かす
     * 範囲：-1～1 （負方向の最大出力割合―1～正方向の最大出力割合1まで）
     * モーターの最大回転数5600RPM
     */
    void moveShooterSpecifedPower(double targetPower);

    /** PIDのリセット */
    void resetPID();
    
    /** 
     * 自分のアライアンス側にFuelをShootする
     * PercentOutputで割合1の速度で回す
     */
    void feed();

    /** 
     * Shooterを負の向きに回す　詰まったときのため
     */
    void reverseShooter();

}
