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

    /** ロボットを任意の角度に回転させる |setAngle:フィールドに対して前を0とした目標の角度。Robotに対して反時計回りが正。度数法[-180~180]*/
    void setAngle(double setAngle);

    /** 指定した座標まで移動する |targetPose:目標の位置、基準はこれ参照https://sakuratempesta6909.sharepoint.com/:w:/r/sites/frc/_layouts/15/Doc.aspx?sourcedoc=%7BA6923B55-C0CE-43E6-A244-C8D7F0712BB0%7D&file=Repository.docx&action=default&mobileredirect=true*/
    void moveToTargetPose(Pose2d targetPose);

    void buildAuto();
}
