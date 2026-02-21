package frc.robot.usecase;

import edu.wpi.first.math.geometry.Pose2d;
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
    */
    public static double calcurateTargetAngle(Pose2d currentPosition) {
        Pose2d hub = UsecaseConst.Poses.HubPosition;
        Pose2d current = currentPosition;
        Pose2d relativePose = current.relativeTo(hub);
        
        double Xdifference = relativePose.getX();
        double Ydifference = relativePose.getY();
        double tangentTheta = Xdifference/Ydifference;
        double theta = Math.atan(tangentTheta);
        return theta;
    }
}
