package frc.robot.components.shooter;

public final class ShooterParameter {
    public static double pGain = 0.0002;
    public static double iGain = 0.0000002;
    public static double dGain = 0.02;
    public static double kSGain = 0.22738;
    public static double kVGain = 0.002;

    /** アライアンス側のエリアにボールを投げ入れるときのモーターの速度　単位：m/s*/
    public static double feedMps = 20;

    /** 詰まり解消用の逆回転出力（PercentOutput） 範囲: [-1.0 -> 0.0] */
    public static double reverseOutput = -0.3;

    //目標速度への許容誤差・モーターが動作中とみなす最小速度[m/s]
    public static double errorToleranceMps = 0.3;
}
