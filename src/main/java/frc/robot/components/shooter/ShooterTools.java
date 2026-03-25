package frc.robot.components.shooter;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterTools {
    static InterpolatingDoubleTreeMap shooterMPSMap;

    /**
     * ShooterParameterのRPMテーブルからマップを初期化する
     * ShooterRPMTable[i][0] = 距離 [m] （マップのキー）
     * ShooterRPMTable[i][1] = RPM      （マップの値）
     */
    static {
        shooterMPSMap = new InterpolatingDoubleTreeMap();
        for (double[] number : ShooterParameter.ShooterRPMTable) {
            shooterMPSMap.put(number[0], number[1]);
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
     * シューターのRPMからFuelの初速度へ変換する
     * @param rpm シューターの回転数（RPM）
     * @return Fuelの初速度（m/s）
     */
    public static double SurfaceRPMToFuelVelocity(double rpm){
        return ShooterConst.RPM_TO_FUEL_VELOCITY_COEFFICIENT * rpm;
    }

    /**
     * Fuelの初速度から必要なシューターのRPMを求める
     * @param power Fuelの初速度（m/s）
     * @return シューターの回転数（RPM）
     */
    public static double fuelVelocityToSurfaceRPM(double power){
        return power / ShooterConst.RPM_TO_FUEL_VELOCITY_COEFFICIENT;
    }

    /**
     * 静止状態のとき距離からFuelの初速を求める
     * @param distance HUBとの距離(m)
     * @return 距離に応じたFuelの初速度[m/s]
     */
    public static double stopDistanceToFuelVelocity(double distance){
        /** Hubへの距離から求めた静止状態のときにFuelの初速度 */
        return SurfaceRPMToFuelVelocity(shooterMPSMap.get(distance));
    }

    /**
     * DriveBaseが動いてることも考慮してFuelを飛ばすべき速度の空間ベクトルを導き出す
     * @param distance HUBとの距離(m)
     * @param speeds 現在の速度 型はChassisSpeeds(m/s)
     * @return x,y,zの値を保持したTranslation3dオブジェクト | 要するに空間ベクトル[m/s]
     */
    public static Translation3d distanceToVector(double distance, ChassisSpeeds speeds) {
        double power = stopDistanceToFuelVelocity(distance);
        SmartDashboard.putNumber("stopMps", power);
        
        // 度数法をラジアンに変換
        double hoodRad = Math.toRadians(ShooterConst.hoodAngle);

        // 各成分の計算
        double xVel = power * Math.cos(hoodRad) - speeds.vxMetersPerSecond;
        double yVel = -speeds.vyMetersPerSecond;
        double zVel = power * Math.sin(hoodRad);

        // Translation3dとして一括で返す
        return new Translation3d(xVel, yVel, zVel);
    }

    /** 
     * 距離をもとにシュート時のm/sを算出したあと自分が動いてる分を引く 
     * @param distance 取得したロボットとゴールの距離　単位：m
     * @param speeds 現在のロボットのスピード 単位: ChassisSpeeds(m/s)
     * @return 距離に応じたFuelの初速度
    */
    public static double distanceToMps(double distance, ChassisSpeeds speeds) {
        Translation3d vector = distanceToVector(distance,speeds);
        double power = vector.getNorm();
        SmartDashboard.putNumber("power", power);
        return power;
    }
}
