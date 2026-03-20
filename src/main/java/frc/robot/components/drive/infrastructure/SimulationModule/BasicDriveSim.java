package frc.robot.components.drive.infrastructure.SimulationModule;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.simulation.ADXRS450_GyroSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.RobotContainer;
import frc.robot.components.drive.DriveConst;
import frc.robot.components.drive.DriveConst.DriveConstants;
import frc.robot.components.drive.infrastructure.SwerveModuleSim;
import frc.robot.components.drive.infrastructure.Vision;
import frc.robot.components.drive.DriveParameter;
import frc.robot.domain.repository.DriveRepository;
import frc.robot.domain.state.DriveState;
import frc.robot.domain.state.StateGroup;

import org.littletonrobotics.junction.Logger;


public class BasicDriveSim implements DriveRepository {
    public final SwerveModuleSim frontLeft = new SwerveModuleSim(DriveConst.ModuleConstants.SwerveModuleConsts.frontLeft);
    public final SwerveModuleSim frontRight = new SwerveModuleSim(DriveConst.ModuleConstants.SwerveModuleConsts.frontRight);
    public final SwerveModuleSim backLeft = new SwerveModuleSim(DriveConst.ModuleConstants.SwerveModuleConsts.backLeft);
    public final SwerveModuleSim backRight = new SwerveModuleSim(DriveConst.ModuleConstants.SwerveModuleConsts.backRight);

    private final ADXRS450_Gyro gyro = new ADXRS450_Gyro();
    private final ADXRS450_GyroSim gyroSim = new ADXRS450_GyroSim(gyro);

    public final Vision vision = new Vision();
    public final VisionSim visionSim = new VisionSim();

    public static FuelSim fuelSim = new FuelSim();
    public static FuelSimulation fuelSimulation = new FuelSimulation(fuelSim);

    private PPHolonomicDriveController driveController;

    private final SwerveDriveOdometry odometer = new SwerveDriveOdometry(DriveConstants.kDriveKinematics, new Rotation2d(0),
    new SwerveModulePosition[]{
        frontLeft.getPosition(),
        frontRight.getPosition(),
        backLeft.getPosition(),
        backRight.getPosition()
    });

    private final SwerveDrivePoseEstimator m_poseEstimator = new SwerveDrivePoseEstimator(
            DriveConst.DriveConstants.kDriveKinematics,
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

    public BasicDriveSim() {
        driveController = createDriveController();

        fuelSimulation.configureFuelSim();

        fuelSim.registerRobot(
            SimulationConst.RobotSize.weitht,   // width (m)
            SimulationConst.RobotSize.length,   // length (m)
            SimulationConst.RobotSize.bumperHeight,
            () -> getPose(),              // Pose2d
            () -> getChassisSpeeds() // ChassisSpeeds
        );

        fuelSim.registerIntake(
            SimulationConst.IntakeSize.xMin,   // 前方向スタート（中心）
            SimulationConst.IntakeSize.xMax,  // 前方20cm
            SimulationConst.IntakeSize.yMin,  // 右端
            SimulationConst.IntakeSize.yMax,  // 左端
            () -> fuelSimulation.canIntake(),
            () -> fuelSimulation.intakeFuel()
        );

        fuelSim.start();
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
        driveController,
        config,
        () -> {
        return false;
        }, RobotContainer.getDriveInstance());
    }

    private PPHolonomicDriveController createDriveController() {
        return new PPHolonomicDriveController(
            new PIDConstants(DriveParameter.Auto.kPXYController, 0, 0),
            new PIDConstants(DriveParameter.Auto.kPThetaController, 0 ,0)
        );
    }

    @Override
    public void setChassisSpeeds(ChassisSpeeds speeds) {
        gyroSim.setAngle(gyro.getAngle() + Math.toDegrees(getChassisSpeeds().omegaRadiansPerSecond * DriveConst.LoopPeriod));
        SwerveModuleState[] moduleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(speeds);
        this.setModuleStates(moduleStates);
    }

    @Override
    public void setChassisSpeedsFiledOriented(ChassisSpeeds speeds) {
        this.setChassisSpeeds(ChassisSpeeds.fromFieldRelativeSpeeds(speeds, getRotation2d()));
    }

    @Override
    public void resetGyroSensor() {
        gyro.reset();
    }

    @Override
    public void periodic(){
        odometer.update(getRotation2d(),
        new SwerveModulePosition[]{
            frontLeft.getPosition(),
            frontRight.getPosition(),
            backLeft.getPosition(),
            backRight.getPosition()
        });

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

        SwerveModuleState[] states = {
            frontLeft.getState(),
            frontRight.getState(),
            backLeft.getState(),
            backRight.getState()
        };


        Logger.recordOutput("Drive/Measured", new double[] {
            states[0].angle.getRadians(), states[0].speedMetersPerSecond,
            states[1].angle.getRadians(), states[1].speedMetersPerSecond,
            states[2].angle.getRadians(), states[2].speedMetersPerSecond,
            states[3].angle.getRadians(), states[3].speedMetersPerSecond
        });
        Logger.recordOutput("Drive/States", states);
        Logger.recordOutput("Drive/GyroAngle", getRotation2d());
        Logger.recordOutput("Drive/Pose", getPose());
        Logger.recordOutput("Drive/odometerPose", getOdometerPose());
        Logger.recordOutput("Drive/ChassisSpeed", getChassisSpeeds());
        Logger.recordOutput("Drive/DistanceToHub", StateGroup.getDistanceToHub());

        Pose2d truePose = getPose(); // または drivetrain.getSimPose()

        visionSim.update(truePose);
        visionSim.periodic();

        fuelSim.updateSim();
        Logger.recordOutput("Fuel/Number", fuelSimulation.getFuelStored());
        Logger.recordOutput("Fuel/BlueScore", fuelSimulation.getBlueScore());
        Logger.recordOutput("Fuel/RedScore", fuelSimulation.getRedScore());
    }

    private double getHeading(){
        return Math.IEEEremainder(gyro.getAngle(), 360);
    }
    private Rotation2d getRotation2d(){
        return Rotation2d.fromDegrees(getHeading());
    }

    private Pose2d getPose(){
        //return odometer.getPoseMeters();
        return m_poseEstimator.getEstimatedPosition();
    }

    private Pose2d getOdometerPose(){
        return odometer.getPoseMeters();
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
    public void setAngle(double setAngle, double Xspeed, double Yspeed) {

    }

    @Override
    public void resetPID(){
        
    }
}
