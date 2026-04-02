package frc.robot.domain.repository;

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
    void setChassisSpeedsFieldOriented(ChassisSpeeds speeds);
    
    /** ジャイロセンサーをリセットする */
    void resetGyroSensor();

    /** ロボットを任意の角度に回転させる 
     * @param setAngle フィールドに対して前を0とした目標の角度。Robotに対して反時計回りが正。度数法
     * @param XSoeed x軸方向のスピード[m/s]
     * @param YSpeed y軸方向のスピード[m/s]
     * PathPlannerで良さそうだけど一応置いとく
    */
    void setAngle(double setAngle ,double XSpeed, double YSpeed);

    void buildAuto();

    void resetPID();

    /**
     * FF測定用の速度を取得する
     * @return 全モジュールの平均速度 [m/s]
     */
    double getFFCharacterizationVelocity();

    /**
     * FF測定用にモーターを電圧でモーターを動かすする
     * @param output モーターへの出力電圧 [V]
     */
    void runCharacterization(double output);

    /* visionを使うか使わないかを切り替える */
    void toggleVisionEnabled();
}
