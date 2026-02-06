package frc.robot.domain.repository;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface ShooterRepository extends Subsystem {

    /** MaxRPMに割合をかけてVelocity制御で動かす 
     * 範囲：-1～1　（負方向の最大出力―1から正方向の最大出力1まで）
    */
    void moveShooterSpecifiedSpeed(double targetSpeed);

    /** PercentOutputでモーターを任意の速度で動かす
     * 範囲：-1～1 （負方向の最大出力―1から正方向の最大出力1まで）
     */
    void moveShooterSpecifedPower(double targetPower);

    void resetPID();

    void resetEncoder();

    /** FuelをHubにShootする */
    void shootToHub();

    /** 自分のアライアンス側にFuelをShootする */
    void feed();

    /** Shooterを負の向きに回す
     *  詰まったときのため
     */
    void reverseShooter();

}
