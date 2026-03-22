package frc.robot.components.shooter;

import edu.wpi.first.math.kinematics.ChassisSpeeds;

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

    public static double stopDistanceToMps(double distance){
        // Hubへの距離から静止状態のときにFuelの初速度計算する計算式を入れる
        return 7.0;
    }

    public static double[] distanceToVector(double distance, ChassisSpeeds speeds){
        double power = stopDistanceToMps(distance);
        double xVel = power * Math.cos(ShooterConst.hoodAngle) - speeds.vxMetersPerSecond;
        double yVel = -speeds.vyMetersPerSecond;
        double zVel = power * Math.sin(ShooterConst.hoodAngle);

        double vector[] = {xVel,yVel,zVel};

        return vector;
    }

    /** 
     * 距離をもとにシュート時のm/sを算出したあと自分が動いてる分を引く 
     * @param distance 取得したロボットとゴールの距離　単位：m
     * @param speeds 現在のロボットのスピード 単位: ChassisSpeeds(m/s)
     * @return 距離に応じたモーターの回転数[m/s] 30は仮値
    */
    public static double distanceToMps(double distance, ChassisSpeeds speeds) {
        double[] vector = distanceToVector(distance,speeds);
        double power = Math.sqrt(vector[0]*vector[0] + vector[1]*vector[1] + vector[2]*vector[2]);
        return power;
    }
}
