package frc.robot.domain.repository;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Subsystem;

public interface DriveRepository extends Subsystem{
    /**
     * ロボットを動かす (robot oriented)
     * @param sideSpeed   左右方向に移動するスピード | m/s | 止めたいとき0 | Robotに対して右に進むとき正
     * @param forwardSpeed   前後方向に移動するスピード | m/s | 止めたいとき0 | Robotに対して前に進むとき正
     * @param thetaSpeed    回転するスピード | m/s | 止めたいとき0 | Robotに対して反時計回りを正とする
     */
    void setChassisSpeeds(ChassisSpeeds speeds);

    /**
     * ロボットを動かす (field oriented)
     * @param sideSpeed   左右方向に移動するスピード | m/s | 止めたいとき0 | Robotに対して右に進むとき正
     * @param forwardSpeed   前後方向に移動するスピード | m/s | 止めたいとき0 | Robotに対して前に進むとき正
     * @param thetaSpeed    回転するスピード | m/s | 止めたいとき0 | Robotに対して反時計回りを正とする
     */
    void setChassisSpeedsFiledOriented(ChassisSpeeds speeds);
    
    /** ジャイロセンサーをリセットする */
    void resetGyroSensor();

    /** ロボットを任意の角度に回転させる 
     * @param setAngle フィールドに対して前を0とした目標の角度。Robotに対して反時計回りが正。度数法
     * @param currentXSoeed 現在のx軸方向の速度
     * @param currentYSpeed 現在のy軸方向の速度
     * PathPlannerで良さそうだけど一応置いとく
    */
    void setAngle(double setAngle ,double currentXSpeed, double currentYSpeed);


    void buildAuto();
}
