package frc.robot.domain.state;

public class ShooterState {
    /** シュート時のモーターのスピード */
    public static double motorSpeed;
    /** シューターのモーターが動作しているか */
    public static boolean isMotorActive;
    /** シューターの目標RPM */
    public static double targetMotorSpeed;
    /** シューターのモーターが十分な回転数に達しているかどうか */
    public static boolean isReadyToShoot;
}
