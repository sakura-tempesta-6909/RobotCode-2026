package frc.robot.usecase;

import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

/**
 * Usecaseで使うような定数
 */
public class UsecaseConst {
    public static final class PathPlannerConst {
        public static final PathConstraints Unlimited = PathConstraints.unlimitedConstraints(12);
    }

    /**
     * ロボットの構造に関する定数
     */
    public static final class RobotStructure {
        public static final double DistanceToArm = -10;
    }

    public static final class Poses{
        public static final Pose2d inFrontOfGoal = new Pose2d(new Translation2d(5, 6), new Rotation2d(Math.PI / 2));
        /** Hubへの目標角度 [degree]
         * 反時計回りが正
         * 正面が0
        */
        public static final double TargetAngleOfHub = 0;
        }

    public static final class Hubs{
        /** Hubの中心の座標(red) */
        public static final Pose2d HubPositionForRed = new Pose2d(new Translation2d(16.5,4.61),new Rotation2d(0));
        /** Hubの中心の座標(blue) */
        public static final Pose2d HubPositionForBlue = new Pose2d(new Translation2d(4.02,4.61),new Rotation2d(0));
    
    }
}
