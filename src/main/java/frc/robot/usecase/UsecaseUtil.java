package frc.robot.usecase;

import java.util.Optional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.components.drive.DriveParameter;
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
        }else{
            hub = UsecaseConst.Hubs.HubPositionForRed;
        }
        return hub;
    }

    /** Hubに向くときの目標の角度を計算する
     * @return theta 目標に向かった角度[degree]
    */
    public static double calcurateTargetAngle() {
        Pose2d current = DriveState.drivePosition;
        Pose2d relativePose = current.relativeTo(getHubPosition());
        
        double Xdifference = relativePose.getX();
        double Ydifference = relativePose.getY();
        Rotation2d theta = new Rotation2d(Xdifference, Ydifference);
        return theta.getDegrees();
    }
}
