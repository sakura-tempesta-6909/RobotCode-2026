package frc.robot.domain.state;

public class ShooterState {
    /** 
     * モーターのRPM
     * 回転数範囲：-5600RPMから5600RPMまで
     */
    public static double motorSpeedRPM;

    /** 
     * シューターのモーターが動作しているか 
     * Boolean型 True動作中 False停止中
     */
    public static boolean isMotorActive;

    /** 
     * シューターの目標RPM
     * 回転数範囲：-5600RPMから5600RPMまで
     */
    public static double targetMotorSpeedRPM;

    /** 
     * シューターのモーターが十分な回転数に達しているかどうか 
     * Boolean型 True達している False未達
     */
    public static boolean isReadyToShoot;
}
