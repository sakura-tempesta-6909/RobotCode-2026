package frc.robot.components.intake;

public final class IntakeConst {
    public static final class Ports{
        /** IntakeのモーターのCANID */
        public static final int intakeMotor = 13;
    }
    /** モーターの最大RPM */
    public static final double maxRPM = 5676;
    /** モーターの動作判定のしきい値 */
    public static final double Threshold = 0.1;
}
