package frc.robot.usecase;

import java.util.Optional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.components.drive.DriveParameter;
import frc.robot.components.shooter.ShooterTools;
import frc.robot.domain.state.DriveState;

/**
 * Usecaseで使うような便利関数の定義
 */
public class UsecaseUtil {
    
    public static double calcArmPosition(double targetPosition) {
        return targetPosition + UsecaseConst.RobotStructure.DistanceToArm;
    }

    /** チームを見分けてHubの位置を返す
     * 
     * @return hubのいち[Pose2d]
     */
    public static Pose2d getHubPosition() {
        Optional<Alliance> ally = DriverStation.getAlliance();
        Pose2d hub = UsecaseConst.Hubs.HubPositionForRed;
        if(ally.isPresent()){
            if(ally.get() == Alliance.Red){
                hub = UsecaseConst.Hubs.HubPositionForRed;
            }
            if(ally.get() == Alliance.Blue){
                hub = UsecaseConst.Hubs.HubPositionForBlue;
            }
        }
        return hub;
    }

    /** 現在の位置からHubへの目標の角度を計算する
     * @return targetTheta 目標に向かった角度[rotation2d]
     * @param speeds 現在の速度 型はChassisSpeeds(m/s)
    */
    public static Rotation2d calcurateTargetAngleToShoot(Pose2d current, ChassisSpeeds speeds) {
        if (current == null) {
            return new Rotation2d(); // もしくは現在の角度を返すなど、null安全な処理を追加
        }
        Pose2d relativePose = current.relativeTo(getHubPosition());
        double Xdifference = relativePose.getX();
        double Ydifference = relativePose.getY();

        double distance = Math.hypot(Xdifference, Ydifference);

        Rotation2d theta = new Rotation2d(Xdifference, Ydifference);

        Translation3d fuelVector = ShooterTools.distanceToVector(distance, speeds);
        // speedsが0の時は0になるので大丈夫
        Rotation2d fuelTheta = new Rotation2d(fuelVector.getX(), fuelVector.getY());
        // HUBに向く角度＋動いてることによる誤差の修正のための角度を目標値とする
        Rotation2d targetTheta = theta.plus(fuelTheta);

        return targetTheta;
    }
}
