package frc.robot.domain.state;

public class ShooterState {
    /** シュート時のモーターのスピード 
     * -1～1の範囲（負方向の最大出力―1から正方向の最大出力1まで）
    */
    public static double motorSpeed;

    /** シューターのモーターが動作しているか 
     * Boolean型 True動作中 False停止中
    */
    public static boolean isMotorActive;

    /** シューターの目標RPM */
    public static double targetMotorSpeed;

    /** シューターのモーターが十分な回転数に達しているかどうか 
     * Boolean型 True達している False未達
    */
    public static boolean isReadyToShoot;
}
