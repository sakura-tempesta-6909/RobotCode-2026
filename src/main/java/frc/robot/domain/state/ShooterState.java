package frc.robot.domain.state;

public class ShooterState {
    /** 
     * シューターの表面速度（m/s）
     * 正回転でシュートする
     * 0で停止
     */
    public static double shooterSurfaceSpeedMps;

    /** 
     * シューターのモーターが動作しているか 
     * Boolean型 True動作中 False停止中
     */
    public static boolean isMotorActive;

    /** 
     * シューターの表面速度の目標（m/s）
     * 正方向でシュートする
     * 0で停止
     */
    public static double targetSurfaceSpeedMps;

    /** 
     * シューターのモーターが十分な回転数に達しているかどうか 
     * Boolean型 True:十分な回転数以上 False:十分な回転数未満
     */
    public static boolean isReadyToShoot;
}