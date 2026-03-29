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
        /** カーペットとwheelの摩擦係数 */
        public static final double WheelCOF = 1.2;
        /** DriveMotorの個数 */
        public static final int NumberMotor = 1;
        /** Moduleの設定(摩擦係数、Wheelの半径、最大速度、DriveMotorの種類、DriveMotorの電流制限、DriveMotorの個数) */
        public static final ModuleConfig ModuleConfig =
            new ModuleConfig(DriveConst.ModuleConstants.kWheelDiameterMeters/2,
                             DriveConst.DriveConstants.kPhysicalMaxSpeedMetersPerSecond,
                             WheelCOF,
                             DCMotor.getNEO(NumberMotor),
                             60,
                             NumberMotor);
        /** 機体の回転中心から見たwheelの (x座標 , y座標) */
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
        /** ロボットのバンパー、バッテリー、FUELを含んだときの重さ [単位:kg] */
        public static final double RobotMass = 56.1;
        /** バンパーの横幅 [単位:m] */
        public static final double BumperWidth = 0.76;
        /** バンパーの縦幅 [単位:m] */
        public static final double DefaultBumperLength = 0.71;
        /** Extenderを一番展開したときのバンパーからExtenderの先端までの距離 [単位:m] */
        public static final double DistanceToExtenderFromDriveBase = 0.3;
        /** Extenderを一番展開した時の縦幅 */
        public static final double BumperLength = DefaultBumperLength + DistanceToExtenderFromDriveBase;
        /** ロボットの慣性モーメント
         * (近似式) = (length*2 + width*2) * mass /12
         * https://pathplanner.dev/robot-config.html#robot-config-options
         */
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
        public static final Pose2d HubPositionForRed = new Pose2d(new Translation2d(11.9,4.05),new Rotation2d(0));
        /** Hubの中心の座標(blue) */
        public static final Pose2d HubPositionForBlue = new Pose2d(new Translation2d(4.02,4.05),new Rotation2d(0));
    
    }

    public static final class Shake{
        /** Extenderを揺らすときの角度[degree] */
        public static final double shakeUpAngle = 45;
        /** Extenderを揺らす際下ろす時の角度[degree] */
        public static final double shakeDownAngle = 15;
        /** Extenderを揺らす際上げる時のtimeoutの時間[s] */
        public static final double shakeUpTimeout = 1.5;
        /** Extenderを揺らす際下げる時のtimeoutの時間1個目[s] */
        public static final double shakeDownTimeout = 1.5;
    }
}
