package frc.robot.domain.state;

public class IntakeState {
    /** モーターのスピード | インテークに取り込む方向を正 | [-1, 1] */
    public static double motorSpeed;
    /** モーターが動作しているか | 動いてたらtrue */
    public static boolean isMotorActive;
    /** モーターの出力電流 [A] */
    public static double outputCurrent;
    /** モーターの出力 [-1, 1] */
    public static double appliedOutput;
    /** 電圧 [V] */
    public static double busVoltage;
}
