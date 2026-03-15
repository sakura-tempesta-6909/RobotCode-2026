package frc.robot.domain.state;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.components.drive.DriveParameter;
import frc.robot.usecase.UsecaseConst;

public class StateGroup {
    public static boolean readyToScore() {
        return true;
    }
    
    /**
     *  Shooterの速度とロボットの位置をもとにシュートできるかどうか
     * @return　シュートできるかどうか | 可能->true,不可->false 
     */
    public static boolean readyToShoot() {
        return ShooterState.isReadyToShoot && DriveState.isShootPosition;
    }

    /**
     * Hubまでの距離を計算する
     * @return Hubまでの距離 | 単位[m] |
     */
    public static double getDistanceToHub() {
        Translation2d currentPosition =  DriveState.drivePosition.getTranslation();
        return currentPosition.minus(UsecaseConst.Poses.TargetPoseToHub.getTranslation()).getNorm();
    }

   /**
    *  ロボットとHubの距離がシュートできる範囲に入っているかどうか
    * @return 範囲に入っているかどうか | 可能->true,不可->false 
    */
    public static boolean isShootableRange() {
        double distance =  getDistanceToHub();
        return distance > DriveParameter.Differences.MinShootableRange && distance < DriveParameter.Differences.MaxShootableRange;
    }
}
