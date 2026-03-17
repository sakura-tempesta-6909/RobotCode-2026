package frc.robot.components.shooter;

public class ShooterTools {

    /**
     * RPMからウィールの表面速度に変換する
     * @param wheelRPM　受け取るRPM
     * @return Wheelの表面速度（m/s）
    */
    public static double rpmToSurfaceSpeed(double wheelRpm){
        return wheelRpm * ShooterConst.wheelDiameter * Math.PI / 60.0;
    }

    /**
     *  Wheelの表面速度からRPMへ変換する
     * @param targetMps 受け取った目標のWheelの表面速度(m/s)
     * @return モーターのRPM 
     */
    public static double mpsToRpm(double targetMps) {
        return (targetMps * 60) / (ShooterConst.wheelDiameter * Math.PI);
    }
    
    /** 
     * 距離をもとにシュート時のm/sを算出する 
     * @param distance 取得したロボットとゴールの距離　単位：m
     * @return 距離に応じたモーターの回転数[m/s] 30は仮値
    */
    public static double distanceToMps(double distance) {
        //計算式を入れる
        return 0.0;
    }
}
