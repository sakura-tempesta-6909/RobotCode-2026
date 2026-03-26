package frc.robot.components.shooter;

public final class ShooterConst {
    public static final class Ports{
        /** ShooterのCANID */
        public static final int ShooterMotor = 18;
        
        /** ShooterのFollowMotor側のCANID */
        public static final int ShooterFollowerMotor = 17;
    }
    /** Wheelの直径　単位m */
    public static double wheelDiameter = 0.1016;

    /**Motorの最大RPM */
    public static double motorMaxRPM = 5676;

    /** shooterがfuelを飛ばすときの角度 | 度数法 */
    public static double hoodAngle = 50;

    /**
     * シューターのRPMからFuelの初速度に変換する係数
     * 単位：[m/s per RPM]
     */
    public static final double RPM_TO_FUEL_VELOCITY_COEFFICIENT = 0.00238;
}
