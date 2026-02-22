package frc.robot.components.drive;

import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;

import java.util.function.Supplier;

public final class DriveConst {
    public static final double LoopPeriod = 0.02;
    public static final class ModuleConstants{
        public static final double kWheelDiameterMeters = edu.wpi.first.math.util.Units.inchesToMeters(4);
        public static final double kDriveMotorGearRatio = 1/6.12; // NEO 1回転でdrive motor が 1/6.12 回転する | drive motor 1回転 : NEO 6.12回転
        public static final double kTurningMotorGearRatio = 7./150; // NEO 1回転でturning motor が 7/150 回転する | turning motor 7回転 : NEO 150回転
        public static final double kDriveEncoderRot2Meter = kDriveMotorGearRatio * Math.PI * kWheelDiameterMeters; // NEOが1回転すると何メートル進むか
        public static final double kTurningEncoderRot2Rad = kTurningMotorGearRatio * 2 * Math.PI; //NEOが1回転すると何ラジアン回転するか
        public static final double kDriveEncoderRPM2MeterPerSec = kDriveEncoderRot2Meter/60;
        public static final double kTurningEncoderRPM2RadPerSec = kTurningEncoderRot2Rad/60;

        public static final SwerveModuleConstGroup SwerveModuleConsts = DriveConst.getSwerveModuleConstGroup();
    }

    public static final class DriveConstants {
        public static final double kTrackWidth = Units.inchesToMeters(21);
        public static final double kWheelBase = 0.65;

        // 機体の回転中心から見たwheelの (x座標 , y座標)
        public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
                new Translation2d(kWheelBase / 2, kTrackWidth / 2), 
                new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
                new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
                new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

        public static final double kPhysicalMaxSpeedMetersPerSecond = 5676. * ModuleConstants.kDriveEncoderRPM2MeterPerSec;
        public static final double kPhysicalMaxAngularSpeedRadiansPerSecond = kPhysicalMaxSpeedMetersPerSecond / (kWheelBase / 2);

        public static final double kTeleDriveMaxAccelerationUnitsPerSecond = 3;
        public static final double kTeleDriveMaxAngularAccelerationUnitsPerSecond = 3;
    }
    
    public static SwerveModuleConstGroup getSwerveModuleConstGroup() {
        SwerveModuleConstGroup group = new SwerveModuleConstGroup();
        Supplier<SparkBaseConfig> driveConfigBase = () -> {
            SparkBaseConfig driveConfig = new SparkMaxConfig();
            driveConfig.encoder
                .positionConversionFactor(DriveConst.ModuleConstants.kDriveEncoderRot2Meter)
                .velocityConversionFactor(DriveConst.ModuleConstants.kDriveEncoderRPM2MeterPerSec);
            driveConfig
                .inverted(true)
                .idleMode(IdleMode.kCoast)
                ;
    
            return driveConfig;
        };

        Supplier<SparkBaseConfig> turningConfigBase = () -> {
            SparkBaseConfig turningConfig = new SparkMaxConfig();
            turningConfig.encoder
                .positionConversionFactor(DriveConst.ModuleConstants.kTurningEncoderRot2Rad)
                .velocityConversionFactor(DriveConst.ModuleConstants.kTurningEncoderRPM2RadPerSec);
            turningConfig.inverted(true);
    
            turningConfig.closedLoop
                .pid(DriveParameter.Module.kPTurning, 0, 0)
                .outputRange(-1, 1);
                
    
            return turningConfig;
        };

        group
            .fl(new SwerveModuleConst(1, 3, 2, false, driveConfigBase.get(), turningConfigBase.get()))
            .fr(new SwerveModuleConst(4, 6, 5, false, driveConfigBase.get(), turningConfigBase.get()))
            .bl(new SwerveModuleConst(10, 12, 11, false, driveConfigBase.get(), turningConfigBase.get()))
            .br(new SwerveModuleConst(7, 9, 8, false, driveConfigBase.get(), turningConfigBase.get()));
        return group;
    }

    public static class SwerveModuleConst {
        public final SparkBaseConfig driveConfig, turningConfig;
        public final int driveMotorID, turningMotorID, encoderID;
        public final boolean encoderReversed;
        public SwerveModuleConst(int driveMotorID, int turningMotorID, int EncoderID, boolean encoderReversed,
                                    SparkBaseConfig driveConfig, SparkBaseConfig turningConfig) {
            this.driveMotorID = driveMotorID;
            this.turningMotorID = turningMotorID;
            this.encoderID = EncoderID;
            this.encoderReversed = encoderReversed;
            this.driveConfig = driveConfig;
            this.turningConfig = turningConfig;
        }
    }

    public static class SwerveModuleConstGroup {
        public SwerveModuleConst frontLeft, frontRight, backLeft, backRight;
        public SwerveModuleConstGroup fl(SwerveModuleConst fl) {
            this.frontLeft = fl;
            return this;
        }

        public SwerveModuleConstGroup fr(SwerveModuleConst fr) {
            this.frontRight = fr;
            return this;
        }

        public SwerveModuleConstGroup bl(SwerveModuleConst bl) {
            this.backLeft = bl;
            return this;
        }

        public SwerveModuleConstGroup br(SwerveModuleConst br) {
            this.backRight = br;
            return this;
        }
    }

    public static final class Vision{
        public static final AprilTagFieldLayout kTagLayout =
                AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltWelded);
        /** ロボットの中心から見た左カメラの位置 */
        public static final Transform3d kRobotToLeftCamera =
                new Transform3d(new Translation3d(0, 0, 0), new Rotation3d(0, 0, 0));
        /** ロボットの中心から見た右カメラの位置 */
        public static final Transform3d kRobotToRightCamera =
                new Transform3d(new Translation3d(0, 0, 0), new Rotation3d(0, 0, 0));

        /** ロボットの内部センサー（エンコーダ・ジャイロ）の推定精度。数値が小さいほど信頼する */
        public static final Vector<N3> kStateStdDevs = VecBuilder.fill(0.05, 0.05, Units.degreesToRadians(5));

        /** ビジョン（カメラ）による推定精度。数値が小さいほど信頼する */
        public static final Vector<N3> kVisionStdDevs = VecBuilder.fill(0.5, 0.5, Units.degreesToRadians(30));
    }
}
