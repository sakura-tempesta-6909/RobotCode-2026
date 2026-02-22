package frc.robot.usecase;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.components.drive.DriveParameter;

/**
 * Usecaseで使うような便利関数の定義
 */
public class UsecaseUtil {
    
    public static double calcArmPosition(double targetPosition) {
        return targetPosition + UsecaseConst.RobotStructure.DistanceToArm;
    }

    /** Hubに向くときの目標の角度を計算する 
     * @param currentPosition 現在の位置
     * @return theta 目標に向かった角度[rotation2d]
    */
    public static Rotation2d calcurateTargetAngle(Pose2d currentPosition) {
        Pose2d hub = UsecaseConst.Poses.HubPosition;
        Pose2d current = currentPosition;
        Pose2d relativePose = current.relativeTo(hub);
        
        double Xdifference = relativePose.getX();
        double Ydifference = relativePose.getY();
        Rotation2d theta = new Rotation2d(Xdifference, Ydifference);
        return theta;
    }
}
