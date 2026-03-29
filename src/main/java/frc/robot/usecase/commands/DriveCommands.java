package frc.robot.usecase.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.components.drive.DriveConst;
import frc.robot.components.drive.DriveParameter;
import frc.robot.components.drive.DriveTools;
import frc.robot.domain.option.DriveOption;
import frc.robot.domain.repository.DriveRepository;
import frc.robot.domain.state.DriveState;
import frc.robot.usecase.UsecaseConst;
import frc.robot.usecase.UsecaseUtil;
import frc.robot.util.Util;

import org.littletonrobotics.junction.Logger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
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
                    driveRepository.setChassisSpeedsFieldOriented(speeds);
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
    public static Command moveToTargetPose(Supplier<Pose2d> targetPoseSupplier) {
    /** deferを使うことでコマンドが実行される瞬間にコマンドを作成するようにする
     *  こうすることでターゲットをコマンドが実行された瞬間の物に固定できる
     */
    return Commands.defer(() -> {
        Pose2d target = targetPoseSupplier.get(); // ←ここで1回だけ取得
        Logger.recordOutput("curent target", target);
        return AutoBuilder.pathfindToPose(
            target,
            UsecaseConst.PathPlannerConst.Unlimited
        );
    }, Set.of());
}

    /** Hubまで移動する
     * 目標値まで到達したら終了
     * 初期化処理:PIDのリセット
     */
    public static Command moveToHub(){
    return Commands.defer(() -> {
        // 現在位置から計算した目標の座標（Translation2d）を求める
        Translation2d targetTrans = DriveTools.calculateTargetPosition(DriveState.drivePosition, DriveParameter.targetdistanceToShoot);
        
        // 【重要】現在位置と目標位置の「距離のズレ」を計算
        double translationError = DriveState.drivePosition.getTranslation().getDistance(targetTrans);
        if (translationError < DriveParameter.Differences.arrowedDifference) {
            // ① すでに到着していれば、その場で回転し続ける（faceToHub）
            return faceToHub(() -> 0.0, () -> 0.0);
        } else {
            // ② まだ遠ければ、移動のためのPathPlannerを生成する（移動＋角度）
            Rotation2d targetRot = UsecaseUtil.calcurateTargetAngleToShoot(UsecaseUtil.getHubPosition().getTranslation(),DriveState.drivePosition, DriveState.driveXYOmegaSpeed);
            Pose2d target = new Pose2d(targetTrans, targetRot);
            return AutoBuilder.pathfindToPose(target, UsecaseConst.PathPlannerConst.Unlimited).andThen(faceToHub(() -> 0.0, () -> 0.0));
        }
    }, Set.of());
}


    /** 目標の角度まで回転する
     * 目標値まで到達したら終了
     * 初期化処理:PIDのリセット
     * @param targetAngle 目標の角度[degree]
     * @param Xspeed x軸方向の速度[m/s]
     * @param Yspeed y軸方向の速度[m/s]
     */
    public static Command setAngle(Supplier<Rotation2d> targetAngleSupplier,DoubleSupplier Xspeed,DoubleSupplier Yspeed) {
        return driveRepository.startRun(() -> {
            driveRepository.resetPID();
        },
        () -> {
            Rotation2d targetAngle = targetAngleSupplier.get();
            driveRepository.setAngle(
                targetAngle.getDegrees(),
                Xspeed.getAsDouble(),
                Yspeed.getAsDouble()
            );
        }
    );
}

    /** Hubに向かった角度まで回転する
     * 目標値まで到達したら終了
     * 初期化処理:PIDのリセット
     * @param xSpeedPercentSupplier x軸のコントローラーの入力[-1~1]
     * @param ySpeedPercentSupplier x軸のコントローラーの入力[-1~1]
     */
    // DriveCommands.java 内の faceToHub を以下のように修正
public static Command faceToHub(DoubleSupplier xSpeedPercentSupplier, DoubleSupplier ySpeedPercentSupplier){
    // startRun を使っている場合の例
    return setAngle(
        () -> UsecaseUtil.calcurateTargetAngleToShoot(
            UsecaseUtil.getHubPosition().getTranslation(),
            DriveState.drivePosition,
            DriveState.driveXYOmegaSpeed
        ),
        () -> Util.deadband(xSpeedPercentSupplier.getAsDouble()) 
            * DriveConst.DriveConstants.kPhysicalMaxSpeedMetersPerSecond,
        () -> Util.deadband(ySpeedPercentSupplier.getAsDouble()) 
            * DriveConst.DriveConstants.kPhysicalMaxSpeedMetersPerSecond
    ).beforeStarting(() -> driveRepository.resetPID());
}

    /** Feedしたい位置に向かった角度まで回転する
     * 目標値まで到達したら終了
     * 初期化処理:PIDのリセット
     * @param xSpeedPercentSupplier x軸のコントローラーの入力[-1~1]
     * @param ySpeedPercentSupplier x軸のコントローラーの入力[-1~1]
     */
    public static Command faceToFeedPosition(DoubleSupplier xSpeedPercentSupplier, DoubleSupplier ySpeedPercentSupplier) {
        double xInput = Util.deadband(xSpeedPercentSupplier.getAsDouble()) * DriveConst.DriveConstants.kPhysicalMaxSpeedMetersPerSecond;
        double yInput = Util.deadband(ySpeedPercentSupplier.getAsDouble()) * DriveConst.DriveConstants.kPhysicalMaxSpeedMetersPerSecond; 

        return setAngle(()-> UsecaseUtil.calcurateTargetAngleToShoot(UsecaseUtil.getFeedPosition(), DriveState.drivePosition, DriveState.driveXYOmegaSpeed), () -> xInput, () -> yInput);
    }

    /** 実験用
     * DriveのkS,kVを測定する
     * このCommandを実行したらコンソールにkS、kVの結果が出てくる */
    public static Command feedforwardCharacterization() {
        List<Double> velocitySamples = new LinkedList<>();
        List<Double> voltageSamples = new LinkedList<>();
        Timer timer = new Timer();

        return Commands.sequence(
                // Reset data
                Commands.runOnce(
                        () -> {
                            velocitySamples.clear();
                            voltageSamples.clear();
                        }),

                // Allow modules to orient 
                driveRepository.run(
                                () -> {
                                    driveRepository.runCharacterization(0.0);
                                }
                                )
                        .withTimeout(2.0),

                // Start timer
                Commands.runOnce(timer::restart),

                // Accelerate and gather data 
                driveRepository.run(
                                () -> {
                                    double voltage = timer.get() * 0.1;
                                    driveRepository.runCharacterization(voltage);
                                    velocitySamples.add(driveRepository.getFFCharacterizationVelocity());
                                    voltageSamples.add(voltage);
                                }).withTimeout(10)

                        // When cancelled, calculate and print results
                        .finallyDo(
                                () -> {
                                    int n = velocitySamples.size();
                                    double sumX = 0.0;
                                    double sumY = 0.0;
                                    double sumXY = 0.0;
                                    double sumX2 = 0.0;
                                    for (int i = 0; i < n; i++) {
                                        sumX += velocitySamples.get(i);
                                        sumY += voltageSamples.get(i);
                                        sumXY += velocitySamples.get(i) * voltageSamples.get(i);
                                        sumX2 += velocitySamples.get(i) * velocitySamples.get(i);
                                    }
                                    double kS = (sumY * sumX2 - sumX * sumXY) / (n * sumX2 - sumX * sumX);
                                    double kV = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);

                                    NumberFormat formatter = new DecimalFormat("#0.00000");
                                    System.out.println("********** Drive FF Characterization Results **********");
                                    System.out.println("\tkS: " + formatter.format(kS));
                                    System.out.println("\tkV: " + formatter.format(kV));
                                }));
    }

}



