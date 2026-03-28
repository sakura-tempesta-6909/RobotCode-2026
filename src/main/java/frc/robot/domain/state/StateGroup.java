package frc.robot.domain.state;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.components.drive.DriveParameter;
import frc.robot.components.extender.ExtenderParameter;
import frc.robot.usecase.UsecaseConst;
import frc.robot.usecase.UsecaseUtil;

public class StateGroup {
    public static boolean readyToScore() {
        return true;
    }
    

    /**
     * 目標までの距離を計算する
     * @return 目標までの距離　| 単位[m] |
     */
    public static double getDistanceToTarget(Translation2d targetPose) {
        Translation2d currentPosition = DriveState.drivePosition.getTranslation();
        return currentPosition.minus(targetPose).getNorm();
    }
    
    /**
     * Hubまでの距離を計算する
     * @return Hubまでの距離 | 単位[m] |
     */
    public static double getDistanceToHub() {
        return getDistanceToTarget(UsecaseUtil.getHubPosition().getTranslation());
    }

    /**
     * Feedする位置までの距離を計算する
     * @return　Feedする位置までの距離　| 単位[m] |
     */
    public static double getDistanceToFeedPosition() {
        return getDistanceToTarget(UsecaseUtil.getFeedPosition());
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
