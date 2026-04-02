package frc.robot.components.shooter;

public final class ShooterParameter {
    public static double pGain = 0.00000001;
    public static double iGain = 0.000000;
    public static double dGain = 0.0;
    public static double kSGain = 0.152738;
    public static double kVGain = 0.002;
    public static double IZone = 3000;

    /** アライアンス側のエリアにボールを投げ入れるときのモーターの速度　単位：m/s*/
    public static double feedMps = 20;

    /** 詰まり解消用の逆回転出力（PercentOutput） 範囲: [-1.0 -> 0.0] */
    public static double reverseOutput = -0.5;

    //目標速度への許容誤差・モーターが動作中とみなす最小速度[m/s]
    public static double errorToleranceMps = 0.1;

    /** ロボットのPoseがずれた時に使うシュートの速度　[m/s] */
    public static double DefalutRPM = 3000;

    /**
     * シューターのRPMマップ
     * [距離(m), RPM] で設定
     */
    public static final double[][] ShooterRPMTable = {
        {2.5, 2850.0},
        {2.6, 2900.0},
        {2.7, 2950.0},
        {3.0, 3000.0},
        {3.1, 3100.0},
        {3.5, 3400.0},
        {4.0, 3800.0}
    };

    /** ゼロ */
    public static double Neutral = 0.0;
}
