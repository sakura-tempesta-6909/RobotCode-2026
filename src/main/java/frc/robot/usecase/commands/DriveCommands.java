package frc.robot.usecase.commands;

import java.lang.annotation.Target;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import com.ctre.phoenix6.swerve.jni.SwerveJNI.DriveState;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.components.drive.DriveConst;
import frc.robot.components.drive.DriveParameter;
import frc.robot.components.drive.DriveTools;
import frc.robot.domain.option.DriveOption;
import frc.robot.domain.repository.DriveRepository;
import frc.robot.domain.state.*;
import frc.robot.usecase.UsecaseConst;
import frc.robot.usecase.UsecaseUtil;
import frc.robot.util.Util;

public class DriveCommands{
    private static DriveRepository driveRepository;

    public static void init(DriveRepository dr) {
        driveRepository = dr;
    }

    public static Command resetGyroSensor() {
        return new InstantCommand(driveRepository::resetGyroSensor);
    }

    public static Command ManualDrive(DoubleSupplier xSpeedPercentSupplier, DoubleSupplier ySpeedPercentSupplier, DoubleSupplier thetaSpeedPercentSupplier) {
        return driveRepository.run(() -> {
            ChassisSpeeds speeds = DriveTools.modifyChassisSpeed(new ChassisSpeeds(
                    Util.deadband(xSpeedPercentSupplier.getAsDouble()) * DriveConst.DriveConstants.kPhysicalMaxSpeedMetersPerSecond,
                    Util.deadband(ySpeedPercentSupplier.getAsDouble()) * DriveConst.DriveConstants.kPhysicalMaxSpeedMetersPerSecond,
                    Util.deadband(thetaSpeedPercentSupplier.getAsDouble())  * DriveConst.DriveConstants.kPhysicalMaxAngularSpeedRadiansPerSecond
            ), DriveOption.driveSpeed.get());
            switch (DriveOption.driveOriented.get()) {
                case s_robotOriented:
                    driveRepository.setChassisSpeeds(speeds);
                    break;
                case s_fieldOriented:
                default:
                    driveRepository.setChassisSpeedsFiledOriented(speeds);
                    break;
            }
        });
    }

    /**
     * 目標値に対してのPathを現在地を元に自動生成して動く
     * ただし、WaypointはGUIの方で設定したものを利用できないので、注意は必要
     * @return
     */
    public static Command GoToGoal() {
        return AutoBuilder.pathfindToPose(UsecaseConst.Poses.inFrontOfGoal, UsecaseConst.PathPlannerConst.Unlimited);
    }

    /**
     * 設定済みのPathに従う。
     * ただし、まずは設定済みPathの開始点までのPathを(現在地を元に)自動生成して、それに従って動いてから、設定済みPathに対して動く。
     * ゴールの大体近くに行ってから、このCommandを動かすことで、いい感じにAlignすることができる (特に、進入角度が大事なものは有用)
     */
    public static Command FollowGoToGoal() {
        PathPlannerPath path;
        try {
            path = PathPlannerPath.fromPathFile("GoToGoal");
        } catch(Exception e) {
            return new InstantCommand();
        }
        return AutoBuilder.pathfindThenFollowPath(path, UsecaseConst.PathPlannerConst.Unlimited);
    }


    /**
     * 目標値に対してのPathを現在地を元に自動生成して動く
     * ただし、WaypointはGUIの方で設定したものを利用できないので、注意は必要
     * @return
     */
    public static Command moveToTargetPose(Pose2d targetPose) {
        return AutoBuilder.pathfindToPose(targetPose, UsecaseConst.PathPlannerConst.Unlimited);
    }

    /** Hubまで移動する
     * 目標値まで到達したら終了
     * 初期化処理:PIDのリセット
     */
    public static Command moveToHub(){
        return moveToTargetPose (UsecaseConst.Poses.TargetPoseToHub);

    }

    /** 目標の角度まで回転する
     * 目標値まで到達したら終了
     * 初期化処理:PIDのリセット
     */
    public static Command setAngle(DoubleSupplier targetAngle) {
        return driveRepository.startRun(()->{
            driveRepository.resetPID();
        },()->{
            driveRepository.setAngle(targetAngle.getAsDouble());
        });
    }

    /** Hubに向かった角度まで回転する
     * 目標値まで到達したら終了
     * 初期化処理:PIDのリセット
     */
    public static Command faceToHub(){
        /** DriveInfraが来てから現在の角度書く */
        return setAngle(() -> UsecaseUtil.calcurateTargetAngle());
    }

    
}



