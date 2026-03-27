package frc.robot.usecase;

import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.path.PathConstraints;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.components.drive.DriveConst;

/**
 * Usecaseで使うような定数
 */
public class UsecaseConst {
    public static final class PathPlannerConst {
        public static final PathConstraints Unlimited = PathConstraints.unlimitedConstraints(12);
        public static final double WheelCOF = 1.2;
        public static final int NumberMotor = 1;
        public static final ModuleConfig ModuleConfig =
            new ModuleConfig(DriveConst.ModuleConstants.kWheelDiameterMeters/2,
                             DriveConst.DriveConstants.kPhysicalMaxSpeedMetersPerSecond,
                             WheelCOF,
                             DCMotor.getNEO(NumberMotor),
                             60,
                             NumberMotor);
        public static final Translation2d[] ModuleOffset = new Translation2d[] {
            new Translation2d(DriveConst.DriveConstants.kWheelBase / 2, DriveConst.DriveConstants.kTrackWidth / 2),   // Front Left
            new Translation2d(DriveConst.DriveConstants.kWheelBase / 2, -DriveConst.DriveConstants.kTrackWidth / 2),  // Front Right
            new Translation2d(-DriveConst.DriveConstants.kWheelBase / 2, DriveConst.DriveConstants.kTrackWidth / 2),  // Back Left
            new Translation2d(-DriveConst.DriveConstants.kWheelBase / 2, -DriveConst.DriveConstants.kTrackWidth / 2)  // Back Right
        };


    }

    /**
     * ロボットの構造に関する定数
     */
    public static final class RobotStructure {
        public static final double DistanceToArm = -10;
        public static final double RobotMass = 56.1;
        public static final double BumperWidth = 0.76;
        public static final double DefaultBumperLength = 0.71;
        public static final double DistanceToExtenderFromDriveBase = 0.3;
        public static final double BumperLength = DefaultBumperLength + DistanceToExtenderFromDriveBase;
        public static final double RobotMOI = (Math.pow(BumperLength, 2) + Math.pow(BumperWidth, 2)) * RobotMass / 12;
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
        }

    public static final class Hubs{
        /** Hubの中心の座標(red) */
        public static final Pose2d HubPositionForRed = new Pose2d(new Translation2d(11.9,4.61),new Rotation2d(0));
        /** Hubの中心の座標(blue) */
        public static final Pose2d HubPositionForBlue = new Pose2d(new Translation2d(4.02,4.61),new Rotation2d(0));
    
    }
}
