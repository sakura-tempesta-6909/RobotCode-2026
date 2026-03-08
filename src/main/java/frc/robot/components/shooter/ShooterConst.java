package frc.robot.components.shooter;

public final class ShooterConst {
    public static final class Ports{
        /** ShooterのCANID */
        public static final int ShooterMotor = 16;
        
        /** ShooterのFollowMotor側のCANID */
        public static final int ShooterFollowerMotor = 17;
    }
    /** Wheelの直径　単位m */
    public static double WheelDiameter = 0.1016;

    /** Wheelの最大表面速度 */
    public static double wheelMaxMps = 30.17;

    /**Motorの最大RPM */
    public static double motorMaxRPM = 5627;
}
