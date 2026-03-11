package frc.robot.components.extender;

public class ExtenderTools {
    /** extenderの回転数に対応するモーターの軸の回転数を求める
     * @param targetAngle 目標の回転角度[degree]
     * @return モーターの軸の回転数[rotation]
     */
    public static double getTargetRotationsForMotorShaft(double targetAngle){
        return ExtenderConst.GearRatio * targetAngle /360;

    }

    /** extenderの累計回転数に対応するextenderの累計回転数を求める
     * @param position 累計回転数[回]
     * @return extenderの累計回転角度[degree]
     */
    public static double calcurateRotation(double position){
        return position * ExtenderConst.GearRatio * 360;
    }
    

}
