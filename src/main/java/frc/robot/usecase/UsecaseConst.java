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

        /** fieldの中心座標(x[m],y[m]) */
        public static final Translation2d CenterOfTheField = new Translation2d(8.25,4.61);
        /** Hubへの目標角度 [degree]
         * 反時計回りが正
         * 正面が0
        */
        public static final double TargetAngleOfHub = 0;

        /**
         * BlueAlliance側のFeedする位置
         * 右側と左側
         */
        public static final Translation2d[] TargetBlueFeedPose = new Translation2d []{
            new Translation2d(1.3, 1.2),
            new Translation2d(1.3, 6.8)
        };

        /**
         * RedAlliance側のFeedする位置
         * 右側と左側
         */
        public static final Translation2d[] TargetRedFeedPose = new Translation2d[] {
            new Translation2d(14.8, 1.2),
            new Translation2d(14.8, 6.8)
        };

        
        }

    public static final class Hubs{
        /** Hubの中心の座標(red) */
        public static final Pose2d HubPositionForRed = new Pose2d(new Translation2d(11.9,4.61),new Rotation2d(0));
        /** Hubの中心の座標(blue) */
        public static final Pose2d HubPositionForBlue = new Pose2d(new Translation2d(4.02,4.61),new Rotation2d(0));
    
    }
}
