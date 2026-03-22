package frc.robot.components.shooter;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
public class ShooterTools {
    static InterpolatingDoubleTreeMap shooterMPSMap;

    /**
     * ShooterParameterのRPMテーブルからマップを初期化する
     * ShooterRPMTable[i][0] = 距離 [m] （マップのキー）
     * ShooterRPMTable[i][1] = RPM      （マップの値）
     */
    static {
        shooterMPSMap = new InterpolatingDoubleTreeMap();
        for (int i = 0; i < ShooterParameter.ShooterRPMTable.length; i++) {
            double distance = ShooterParameter.ShooterRPMTable[i][0];
            double rpm = ShooterParameter.ShooterRPMTable[i][1];
            shooterMPSMap.put(distance, rpm);
        }
    }
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
     * @return 距離に応じたモーターの回転数[m/s]
    */
    public static double distanceToMps(double distance) {
        return rpmToSurfaceSpeed(shooterMPSMap.get(distance));
    }
}
