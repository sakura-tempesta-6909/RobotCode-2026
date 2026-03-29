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
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.components.drive.DriveConst;
import frc.robot.components.drive.DriveConst.DriveConstants;
import frc.robot.components.drive.DriveParameter;
import frc.robot.components.drive.DriveTools;
import frc.robot.domain.repository.DriveRepository;
import frc.robot.domain.state.DriveState;
import frc.robot.usecase.UsecaseConst;
import frc.robot.usecase.UsecaseUtil;

import java.util.Optional;


public class BasicDrive implements DriveRepository {
    public final SwerveModule frontLeft = new SwerveModule(DriveConst.ModuleConstants.SwerveModuleConsts.frontLeft);

    public final SwerveModule frontRight = new SwerveModule(DriveConst.ModuleConstants.SwerveModuleConsts.frontRight);

    public final SwerveModule backLeft = new SwerveModule(DriveConst.ModuleConstants.SwerveModuleConsts.backLeft);

    public final SwerveModule backRight = new SwerveModule(DriveConst.ModuleConstants.SwerveModuleConsts.backRight);

    private final static ADXRS450_Gyro gyro = new ADXRS450_Gyro();

    // 加速度測定用
    private double angularLastVelocity = 0.0;
    private double angularAcceleration = 0.0;
    private double linearLastVelocity = 0.0;
    private double linearAcceleration = 0.0;

    public final PIDController anglePID = new PIDController(DriveParameter.Speeds.kP, DriveParameter.Speeds.kI, DriveParameter.Speeds.kD);
    
    public final Field2d field = new Field2d();

    private final SwerveDriveOdometry odometer = new SwerveDriveOdometry(DriveConstants.kDriveKinematics, new Rotation2d(0),
    new SwerveModulePosition[]{
        frontLeft.getPosition(),
        frontRight.getPosition(),
        backLeft.getPosition(),
        backRight.getPosition()
    });

    private final SwerveDrivePoseEstimator m_poseEstimator = new SwerveDrivePoseEstimator(
            DriveConstants.kDriveKinematics,
            gyro.getRotation2d(),
            new SwerveModulePosition[] {
                    frontLeft.getPosition(),
                    frontRight.getPosition(),
                    backLeft.getPosition(),
                    backRight.getPosition()
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
        try {
            config = RobotConfig.fromGUISettings();
        } catch (Exception e) {
            e.printStackTrace();
            config = new RobotConfig(UsecaseConst.RobotStructure.RobotMass, UsecaseConst.RobotStructure.RobotMOI, UsecaseConst.PathPlannerConst.ModuleConfig, UsecaseConst.PathPlannerConst.ModuleOffset);
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

        // 角加速度計算 a = Δω / Δt [deg/s^2]
        double currentAngularVelocity = Math.toDegrees(DriveState.driveXYOmegaSpeed.omegaRadiansPerSecond);
        angularAcceleration = (currentAngularVelocity - angularLastVelocity) / DriveConst.LoopPeriod;
        angularLastVelocity = currentAngularVelocity;

        // 加速度計算 a = Δv / Δt [m/s^2]
        double currentLinearVelocity = Math.hypot(DriveState.driveXYOmegaSpeed.vxMetersPerSecond, DriveState.driveXYOmegaSpeed.vyMetersPerSecond);
        linearAcceleration = (currentLinearVelocity - linearLastVelocity) / DriveConst.LoopPeriod;
        linearLastVelocity = currentLinearVelocity;
        SmartDashboard.putNumber("Drive/AngularAcceleration", angularAcceleration);
        SmartDashboard.putNumber("Drive/LinearAcceleration", linearAcceleration);

        m_poseEstimator.update(getRotation2d(),
                new SwerveModulePosition[]{
                        frontLeft.getPosition(),
                        frontRight.getPosition(),
                        backLeft.getPosition(),
                        backRight.getPosition()
                });

        vision.leftCameraPose.ifPresent(pose -> {
            /** 
             *  現在の座標との差がkMaxVisionPoseErrorMeters以内の場合のみ適用する
             *  AprilTagの性質上タグが1個だと左右反転したり、誤差が大きくなったりして、
             *  座標が大きくずれてしまうのでその対策として入れた
             */
            if (pose.getTranslation().getDistance(getPose().getTranslation()) < DriveParameter.Vision.kMaxVisionPoseErrorMeters) {
                m_poseEstimator.addVisionMeasurement(pose, vision.leftCameraTimestamp);
            }
        });
        
        vision.rightCameraPose.ifPresent(pose -> {
             /** 
             *  現在の座標との差がkMaxVisionPoseErrorMeters以内の場合のみ適用する
             *  AprilTagの性質上タグが1個だと左右反転したり、誤差が大きくなったりして、
             *  座標が大きくずれてしまうのでその対策として入れた
             */
            if (pose.getTranslation().getDistance(getPose().getTranslation()) < DriveParameter.Vision.kMaxVisionPoseErrorMeters) {
                m_poseEstimator.addVisionMeasurement(pose, vision.rightCameraTimestamp);
            }
        });

        DriveState.drivePosition = getPose();
        

        DriveState.targetPosition = new Pose2d(DriveTools.calculateTargetPosition(getPose(),DriveParameter.targetdistanceToShoot),UsecaseUtil.calcurateTargetAngleToShoot(UsecaseUtil.getHubPosition().getTranslation(), getPose(),DriveState.driveXYOmegaSpeed));

        // Swerveモーター情報 [FL, FR, BL, BR]
        SwerveModule[] modules = {frontLeft, frontRight, backLeft, backRight};
        for (int i = 0; i < 4; i++) {
            DriveState.SwerveMotors.driveOutputCurrent[i] = modules[i].getDriveOutputCurrent();
            DriveState.SwerveMotors.driveAppliedOutput[i] = modules[i].getDriveAppliedOutput();
            DriveState.SwerveMotors.driveBusVoltage[i] = modules[i].getDriveBusVoltage();
            DriveState.SwerveMotors.turningOutputCurrent[i] = modules[i].getTurningOutputCurrent();
            DriveState.SwerveMotors.turningAppliedOutput[i] = modules[i].getTurningAppliedOutput();
            DriveState.SwerveMotors.turningBusVoltage[i] = modules[i].getTurningBusVoltage();
        }

        DriveState.heading = getRotation2d();
        SwerveModuleState[] state = {
            frontLeft.getState(),
            frontRight.getState(),
            backLeft.getState(),
            backRight.getState()
        };
        DriveState.swerveModuleState = state;


        field.setRobotPose(DriveState.drivePosition);
        SmartDashboard.putData("field", field);
    }

    private double getHeading(){
        return -Math.IEEEremainder(gyro.getAngle(), 360);
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
        getRotation2d(),
        new SwerveModulePosition[]{
            frontLeft.getPosition(),
            frontRight.getPosition(),
            backLeft.getPosition(),
            backRight.getPosition()
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
        double output = anglePID.calculate(getRotation2d().getDegrees(),setAngle);
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
