package frc.robot.components.shooter;

public final class ShooterConst {
    public static final class Ports{
        /** ShooterのCANID */
        public static final int ShooterMotor = 16;
    }
    /** Wheelの直径 [m] */
    public static double WheelDiameter = 0.1016;

     /** モーターの最大回転数 [RPM] */
    public static double maxRPM = 5676;

    /** 
     * モーター最大RPM時のホイール表面速度 [m/s] 
     * RPMからm/sに直すときに使う可能性があるから残す
    */
    public static final double maxSurfaceSpeed = 30.17;
}
