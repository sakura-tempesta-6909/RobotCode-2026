package frc.robot.components.shooter;

public class ShooterTools {

    /** 
     * 割合からRPMへ変換
     * @param Ratio モーターを動かす最大RPMに対する割合
    */
    public static double RatioToRPM(double Ratio) {
        return Ratio * ShooterConst.maxRPM;
    }
}
