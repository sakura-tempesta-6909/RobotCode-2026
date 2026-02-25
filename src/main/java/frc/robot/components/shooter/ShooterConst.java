package frc.robot.components.shooter;

public final class ShooterConst {
    public static final class Ports{
        /** ShooterのCANID */
        public static final int ShooterMotor = 16;
    }
    /** Wheelの直径　単位m */
    public static double WheelDiameter = 0.1016;

    /**
     * ウィールのシュート方向への最大表面速度m/s 
     *　符号を入れ替えると逆方向への最大RPM
     */
    public static double maxMps = 30.17;

    /**
     * モーターのシュート方向への最大RPM
     * 符号を入れ替えると逆方向への最大RPM
     */
    public static double maxRPM = 5676;
}
