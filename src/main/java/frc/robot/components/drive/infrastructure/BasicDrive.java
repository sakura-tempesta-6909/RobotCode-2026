package frc.robot.components.drive.infrastructure;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.RobotState;
import frc.robot.RobotContainer;
import frc.robot.components.drive.DriveConst;
import frc.robot.components.drive.DriveConst.DriveConstants;
import frc.robot.components.drive.DriveParameter;
import frc.robot.components.drive.DriveTools;
import frc.robot.domain.repository.DriveRepository;
import frc.robot.domain.state.DriveState;
import frc.robot.usecase.UsecaseUtil;

import java.util.Optional;


public class BasicDrive implements DriveRepository {
    public final SwerveModule frontLeft = new SwerveModule(DriveConst.ModuleConstants.SwerveModuleConsts.frontLeft);
    
    public final SwerveModule frontRight = new SwerveModule(DriveConst.ModuleConstants.SwerveModuleConsts.frontRight);

    public final SwerveModule backLeft = new SwerveModule(DriveConst.ModuleConstants.SwerveModuleConsts.backLeft);

    public final SwerveModule backRight = new SwerveModule(DriveConst.ModuleConstants.SwerveModuleConsts.backRight);
        
    private final static ADXRS450_Gyro gyro = new ADXRS450_Gyro();

    public final PIDController anglePID = new PIDController(DriveParameter.Speeds.kP, DriveParameter.Speeds.kI, DriveParameter.Speeds.kD);

    private final SwerveDriveOdometry odometer = new SwerveDriveOdometry(DriveConstants.kDriveKinematics, new Rotation2d(0),
    new SwerveModulePosition[]{
        frontLeft.getPosition(),
        frontRight.getPosition(),
        backLeft.getPosition(),
        backRight.getPosition()
    });

    private final SwerveDrivePoseEstimator m_poseEstimator = new SwerveDrivePoseEstimator(
            DriveConstants.kDriveKinematics,
            // ▼ [Vision実験] ジャイロ無効化：初期角度をゼロ固定
            // gyro.getRotation2d(),
            new Rotation2d(0),
            // ▼ [Vision実験] モジュール位置無効化：ゼロ固定
            // new SwerveModulePosition[] {
            //         frontLeft.getPosition(),
            //         frontRight.getPosition(),
            //         backLeft.getPosition(),
            //         backRight.getPosition()
            // },
            new SwerveModulePosition[] {
                    new SwerveModulePosition(),
                    new SwerveModulePosition(),
                    new SwerveModulePosition(),
                    new SwerveModulePosition()
            },
            new Pose2d(),
            DriveConst.Vision.kStateStdDevs,
            DriveConst.Vision.kVisionStdDevs);

    public final Vision vision = new Vision();
    public BasicDrive() {
        anglePID.enableContinuousInput(-180, 180);
    }

    public void buildAuto() {
        RobotConfig config;
        try{
            config = RobotConfig.fromGUISettings();
        } catch (Exception e) {
            e.printStackTrace();
            config = new RobotConfig(0, 0, null, 0);
        }

        AutoBuilder.configure(
        this::getPose,
        this::resetOdometry,
        this::getChassisSpeeds,
        this::setChassisSpeeds,
        new PPHolonomicDriveController(
            new PIDConstants(DriveParameter.Auto.kPXYController, 0, 0),
            new PIDConstants(DriveParameter.Auto.kPThetaController, 0, 0),
            0.2
        ),
        config,
        () -> {
            //RedAllianceのときはFlipする
            Optional<DriverStation.Alliance> alliance = DriverStation.getAlliance();
            if (alliance.isPresent()) {
                return alliance.get() == DriverStation.Alliance.Red;
            }
            return false;
        }, RobotContainer.getDriveInstance());
    }

    @Override
    public void setChassisSpeeds(ChassisSpeeds speeds) {
        SwerveModuleState[] moduleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(speeds);
        this.setModuleStates(moduleStates);
    }

    @Override
    public void setChassisSpeedsFieldOriented(ChassisSpeeds speeds) {
        if (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == DriverStation.Alliance.Red) {
            speeds = new ChassisSpeeds(
            -speeds.vxMetersPerSecond,
            -speeds.vyMetersPerSecond,
            speeds.omegaRadiansPerSecond);
        }
        this.setChassisSpeeds(ChassisSpeeds.fromFieldRelativeSpeeds(speeds, getRotation2d()));
    }

    @Override
    public void resetGyroSensor() {
        gyro.reset();
    }


    @Override
    public void periodic(){
        vision.periodic();

        odometer.update(getRotation2d(), 
        new SwerveModulePosition[]{
            frontLeft.getPosition(),
            frontRight.getPosition(),
            backLeft.getPosition(),
            backRight.getPosition()
        });
        DriveState.driveXYOmegaSpeed = getChassisSpeeds();

        // ▼▼▼ [Vision実験] オドメトリー＆ジャイロによるEstimator更新を無効化 ▼▼▼
        // m_poseEstimator.update(getRotation2d(),
        //         new SwerveModulePosition[]{
        //                 frontLeft.getPosition(),
        //                 frontRight.getPosition(),
        //                 backLeft.getPosition(),
        //                 backRight.getPosition()
        //         });

        // ▼ [Vision実験] Estimatorの内部バッファを維持するためupdateは呼ぶが、
        //   ジャイロ・モジュール位置はすべてゼロ固定にしてオドメトリーを完全に無効化
        m_poseEstimator.update(
                new Rotation2d(0), // ジャイロ無効化（getRotation2d()をコメントアウト）
                new SwerveModulePosition[]{
                        new SwerveModulePosition(), // ゼロ位置（frontLeft.getPosition()をコメントアウト）
                        new SwerveModulePosition(), // ゼロ位置（frontRight.getPosition()をコメントアウト）
                        new SwerveModulePosition(), // ゼロ位置（backLeft.getPosition()をコメントアウト）
                        new SwerveModulePosition()  // ゼロ位置（backRight.getPosition()をコメントアウト）
                });
        // ▲▲▲ [Vision実験] ここまで ▲▲▲

        vision.leftCameraPose.ifPresent(pose -> {
            // ▼ [Vision実験] 距離フィルター無効化
            //   オドメトリーなしだと初期位置からずれてフィルターに引っかかるため除去
            // if (pose.getTranslation().getDistance(getPose().getTranslation()) < DriveParameter.Vision.kMaxVisionPoseErrorMeters) {
                m_poseEstimator.addVisionMeasurement(pose, vision.leftCameraTimestamp);
            // }
            /** 
             *  現在の座標との差がkMaxVisionPoseErrorMeters以内の場合のみ適用する
             *  AprilTagの性質上タグが1個だと左右反転したり、誤差が大きくなったりして、
             *  座標が大きくずれてしまうのでその対策として入れた
             */
        });
        
        vision.rightCameraPose.ifPresent(pose -> {
            // ▼ [Vision実験] 距離フィルター無効化（左カメラと同様）
            // if (pose.getTranslation().getDistance(getPose().getTranslation()) < DriveParameter.Vision.kMaxVisionPoseErrorMeters) {
                m_poseEstimator.addVisionMeasurement(pose, vision.rightCameraTimestamp);
            // }
             /** 
             *  現在の座標との差がkMaxVisionPoseErrorMeters以内の場合のみ適用する
             *  AprilTagの性質上タグが1個だと左右反転したり、誤差が大きくなったりして、
             *  座標が大きくずれてしまうのでその対策として入れた
             */
        });

        DriveState.drivePosition = getPose();

        DriveState.isShootPosition = DriveTools.isShootPosition(DriveState.targetPosition, DriveState.drivePosition); 

        DriveState.targetPosition = new Pose2d(DriveTools.calculateTargetPosition(getPose()),UsecaseUtil.calcurateTargetAngleToShoot(getPose(),getChassisSpeeds()));
    }

    private double getHeading(){
        return Math.IEEEremainder(gyro.getAngle(), 360);
    }
    private Rotation2d getRotation2d(){
        return DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red
                        ? Rotation2d.fromDegrees(getHeading()).plus(Rotation2d.kPi)
                        : Rotation2d.fromDegrees(getHeading());
    }

    private Pose2d getPose(){
        //return odometer.getPoseMeters();
        return m_poseEstimator.getEstimatedPosition();
    }

    private ChassisSpeeds getChassisSpeeds() {
        return DriveConstants.kDriveKinematics.toChassisSpeeds(
            frontLeft.getState(),
            frontRight.getState(),
            backLeft.getState(),
            backRight.getState()
        );
    }

    private void resetOdometry(Pose2d pose) {
        odometer.resetPosition(getRotation2d(), new SwerveModulePosition[]{
        frontLeft.getPosition(),
        frontRight.getPosition(),
        backLeft.getPosition(),
        backRight.getPosition()
        },pose);

        m_poseEstimator.resetPosition(
        // ▼ [Vision実験] ジャイロ無効化
        // getRotation2d(),
        new Rotation2d(0),
        // ▼ [Vision実験] モジュール位置無効化
        // new SwerveModulePosition[]{
        //     frontLeft.getPosition(),
        //     frontRight.getPosition(),
        //     backLeft.getPosition(),
        //     backRight.getPosition()
        // },
        new SwerveModulePosition[]{
            new SwerveModulePosition(),
            new SwerveModulePosition(),
            new SwerveModulePosition(),
            new SwerveModulePosition()
        },
        pose
        );
    }

    private void setModuleStates(SwerveModuleState[] desiredStates){
        frontLeft.setDesiredState(desiredStates[0]);
        frontRight.setDesiredState(desiredStates[1]);
        backLeft.setDesiredState(desiredStates[2]);
        backRight.setDesiredState(desiredStates[3]);
    }

    @Override
    /** ロボットを任意の角度に回転させる 
     * @param setAngle フィールドに対して前を0とした目標の角度。Robotに対して反時計回りが正。度数法[degree]
     * @param XSpeed X軸方向のスピード[m/s] 
     * @param YSpeed Y軸方向のスピード[m/s] */
     
    public void setAngle(double setAngle, double XSpeed, double YSpeed) {
        double output = anglePID.calculate(getHeading(),setAngle);
        double targetAngularSpeed = MathUtil.clamp(output, -DriveConstants.kPhysicalMaxAngularSpeedRadiansPerSecond, DriveConstants.kPhysicalMaxAngularSpeedRadiansPerSecond);
        ChassisSpeeds speed = new ChassisSpeeds(XSpeed,YSpeed,targetAngularSpeed);
        setChassisSpeedsFieldOriented(speed);
        
    }

    @Override
    public void resetPID(){
        
    }

    public double getFFCharacterizationVelocity() {
        double output = (frontLeft.getDriveVelocity() + frontRight.getDriveVelocity() + backLeft.getDriveVelocity() + backRight.getDriveVelocity())/ 4.0;
        return output;
    }

    public void runCharacterization(double output) {
        frontLeft.runCharacterization(output);
        frontRight.runCharacterization(output);
        backLeft.runCharacterization(output);
        backRight.runCharacterization(output);
    }
}