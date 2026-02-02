package frc.robot.components.example;

import edu.wpi.first.math.geometry.Translation3d;

public class ExampleTools {
    /**
     * 位置からエンコーダの値への変換
     * @param position 位置 単位: cm, 正の方向: 上方向, 基準: 地面
     * @return エンコーダの値
     */
    public static double positionToEncoderValue(double position) {
        return position / 30;
    }

    /**
     * エンコーダの値から位置への変換
     * @param encoderValue エンコーダの値
     * @return 位置 単位: cm, 正の方向: 上方向, 基準: 地面
     */
    public static double encoderValueToPosition(double encoderValue) {
        return encoderValue * 30;
    }

    /**
     * Apirltagの位置からexampleの位置への変換
     * @param apriltagPosition apriltagの位置 単位: translation3d 基準: フィールド原点
     * @return 位置 単位: cm, 正の方向: 上方向, 基準: 地面
     */
    public static double apriltagPositionToExamplePosition(Translation3d apriltagPosition) {
        return apriltagPosition.getZ() / 100 + 40; 
    }
}
