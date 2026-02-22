package frc.robot.components.extender;

public class ExtenderTools {
    /** extenderの回転数に対応するモーターの軸の回転数を求める
     * @param rotation 回す回転数
     */
    public static double getRotationsForDistance(double rotation){
        return  ExtenderConst.GearRatio * rotation;

    }

    public static double calcurateRotation(double position){
        return position * ExtenderConst.GearRatio;
    }
    

}
