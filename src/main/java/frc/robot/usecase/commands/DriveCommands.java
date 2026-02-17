package frc.robot.usecase.commands;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

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
import frc.robot.usecase.UsecaseConst;
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
        return AutoBuilder.pathfindToPose(DriveParameter.Poses.inFrontOfGoal, UsecaseConst.PathPlannerConst.Unlimited);
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

    /** 目標の位置まで移動する
     * 目標値まで到達したら終了
     * 初期化処理:PIDのリセット
     */
    public static Command moveToTargetPose(Supplier<Pose2d> targetSupplier){
        return AutoBuilder.pathfindThenFollowPath(null,null);
    }

    /** Hubまで移動する
     * 目標値まで到達したら終了
     * 初期化処理:PIDのリセット
     */
    public static Command moveToHub(){
        return moveToTargetPose (()->(DriveParameter.Poses.TargetPoseOfHub));
    }

    /** 目標の角度まで回転する
     * 目標値まで到達したら終了
     * 初期化処理:PIDのリセット
     */
    public static Command setAngle(DoubleSupplier targetSupplier) {
        return driveRepository.startRun(()->{
            driveRepository.resetPID();
        },()->{
            driveRepository.setAngle(targetSupplier.getAsDouble());
        });
    }

    /** Hubに向かった角度まで回転する
     * 目標値まで到達したら終了
     * 初期化処理:PIDのリセット
     */
    public static Command faceToHub(){
        return setAngle(()->(DriveParameter.Poses.TargetAngleOfHub));
    }

    
}



