package frc.robot.components.drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.components.drive.DriveConst.DriveConstants;

//ファイルにはConstInitメソッドのみをもつクラスを記述する
public class DriveParameter {
    /** ロボットの速さを3段階で調整する */
    public static final class Speeds {
        /** ロボットを止めるときの速度 */
        public static final double Neutral = 0;

        /** ロボットが速いときの進む速度 */
        public static final double FastDrive = 1.0;
        /** ロボットが普通の速さのときの進む速度 */
        public static final double MidDrive = 0.6;
        /** ロボットが遅いときの進む速度 */
        public static final double SlowDrive = 0.25;

        /** ロボットが速いときの回転速度 */
        public static final double FastThetaDrive = 0.8;
        /** ロボットが普通の速さときの回転速度 */
        public static final double MidThetaDrive = 0.6;
        /** ロボットが遅いときの回転速度 */
        public static final double SlowThetaDrive = 0.1875;

        /** gyroのPID制御　Pの値 */
        public static final double kP = 0.008;
        /** gyroのPID制御　Iの値 */
        public static final double kI = 0.0005;
        /** gyroのPID制御　Dの値 */
        public static final double kD = 0;
    }

    public static final class Module {
        public static final double kPTurning = 0.5; 
    }

    public static final class Auto {
        public static final double kMaxSpeedMetersPerSecond = DriveConstants.kPhysicalMaxSpeedMetersPerSecond / 4;
        public static final double kMaxAccelerationMetersPerSecondSquared = 0.6;

        /** 左右方向のPID制御のPの値 */
        public static final double kPXYController = 0.35;
        /** 回転のPID制御のPの値 */
        public static final double kPThetaController = 1;

        public static final TrapezoidProfile.Constraints kThetaControllerConstraints = //
                new TrapezoidProfile.Constraints(
                    DriveConstants.kPhysicalMaxAngularSpeedRadiansPerSecond / 10,
                        Math.PI / 4);
    }

    public static final class Poses {
        public static final Pose2d inFrontOfGoal = new Pose2d(new Translation2d(5, 6), new Rotation2d(Math.PI / 2));
        /** Hubへの目標地点 */
        public static final Pose2d TargetPoseOfHub = new Pose2d(new Translation2d(0, 0), new Rotation2d(0));
        /** Hubへの目標角度 */
        public static final double TargetAngleOfHub = 0;

    }
}
