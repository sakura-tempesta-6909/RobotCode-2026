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

    /** shooterがfuelを飛ばすときの角度 | 弧度法 */
    public static double hoodAngle = 45;
}
