package frc.robot.usecase;

import java.util.Optional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.components.drive.DriveParameter;
import frc.robot.components.shooter.ShooterConst;
import frc.robot.components.shooter.ShooterTools;
import frc.robot.domain.state.DriveState;

/**
 * Usecaseで使うような便利関数の定義
 */
public class UsecaseUtil {
    
    public static double calcArmPosition(double targetPosition) {
        return targetPosition + UsecaseConst.RobotStructure.DistanceToArm;
    }

    /** チームを見分けてHubの位置を返す
     * 
     * @return hubのいち[Pose2d]
     */
    public static Pose2d getHubPosition() {
        Optional<Alliance> ally = DriverStation.getAlliance();
        Pose2d hub = UsecaseConst.Hubs.HubPositionForBlue;
        if(ally.isPresent()){
            if(ally.get() == Alliance.Red){
                hub = UsecaseConst.Hubs.HubPositionForRed;
            }
            if(ally.get() == Alliance.Blue){
                hub = UsecaseConst.Hubs.HubPositionForBlue;
            }
        }
        return hub;
    }

    /**
     * 現在地から動いてるときにシュートするときにFuelに与えるべき初速度とその方向を求める
     * @param distance HUBとの距離(m)
     * @param speeds 現在の速度 型はChassisSpeeds(m/s)
     * @return x,y,zの値を保持したTranslation3dオブジェクト | 要するに空間ベクトル[m/s]
     */
    public static Translation3d distanceToVectorWhileMoving(double distance, ChassisSpeeds speeds) {
        double power = ShooterTools.distanceToFuelVelocityWhileStopping(distance);
        SmartDashboard.putNumber("stopMps", power);
        
        // 度数法をラジアンに変換
        double hoodRad = Math.toRadians(ShooterConst.hoodAngle);

        // 各成分の計算
        double xVel = power * Math.cos(hoodRad) - speeds.vxMetersPerSecond;
        double yVel = -speeds.vyMetersPerSecond;
        double zVel = power * Math.sin(hoodRad);

        // Translation3dとして一括で返す
        return new Translation3d(xVel, yVel, zVel);
    }

    /**
     * 現在位置とロボットの速度をもとに、Hubへシュートするための目標角度を計算する
     * @param current 現在のロボットの位置・姿勢（Pose2d）
     * @param speeds 現在のロボットの速度（ChassisSpeeds, m/s）
     * @return Hubへ正しくシュートするための目標角度（Rotation2d）
     */
    public static Rotation2d calcurateTargetAngleToShoot(Pose2d current, ChassisSpeeds speeds) {
        if (current == null) {
            return new Rotation2d(); // もしくは現在の角度を返すなど、null安全な処理を追加
        }
        Pose2d relativePose = getHubPosition().relativeTo(current);
        double Xdifference = relativePose.getX();
        double Ydifference = relativePose.getY();

        double distance = Math.hypot(Xdifference, Ydifference);

        Rotation2d theta = new Rotation2d(Xdifference, Ydifference);

        Translation3d fuelVector = distanceToVectorWhileMoving(distance, speeds);
        // speedsが0の時は0になるので止まっているときに使っても大丈夫
        Rotation2d fuelTheta = new Rotation2d(fuelVector.getX(), fuelVector.getY());
        // HUBに向く角度＋動いてることによる誤差の修正のための角度を目標値とする
        Rotation2d targetTheta = theta.plus(fuelTheta);

        return targetTheta;
    }
}
