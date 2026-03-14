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
        return (targetMps * 60) / ShooterConst.wheelDiameter * Math.PI;
    }
}