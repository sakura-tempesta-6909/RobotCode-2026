package frc.robot.usecase;

/**
 * Usecaseで使うような便利関数の定義
 */
public class UsecaseUtil {
    public static double calcArmPosition(double targetPosition) {
        return targetPosition + UsecaseConst.RobotStructure.DistanceToArm;
    }
}
