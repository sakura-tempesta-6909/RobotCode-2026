package frc.robot.components.extender;

public class ExtenderTools {
    /** extenderの回転数に対応するモーターの軸の回転数を求める
     * @param rotation 回す回転数[回]
     * @return モーターの軸の回転数[回]
     */
    public static double getRotationsForDistance(double rotation){
        return ExtenderConst.GearRatio * rotation;

    }

    /** extenderの累計回転数に対応するextenderの累計回転数を求める
     * @param position 累計回転数[回]
     * @return extenderの累計回転数[degree]
     */
    public static double calcurateRotation(double position){
        return position * ExtenderConst.GearRatio * 360;
    }
    

}
