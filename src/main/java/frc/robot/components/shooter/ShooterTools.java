package frc.robot.components.shooter;

public class ShooterTools {

    /** 
     * 割合からRPMへ変換
     * @param Ratio モーターを動かす最大RPMに対する割合
     * @return モーターの回転数[RPM]
     * 
     * 変換式:
     * RPM = ratio × maxRPM
    */
    public static double RatioToRPM(double Ratio) {
        return Ratio * ShooterConst.maxRPM;
    }

    /** 
     * 距離をもとにシュート時のRPMを算出する 
     * @param distance 取得したロボットとゴールの距離
     * @return 距離に応じたモーターの回転数[RPM]
     */
    public static double distanceToRPM(double distance) {
        //計算式を入れる
        return 0.0;
    }
}
