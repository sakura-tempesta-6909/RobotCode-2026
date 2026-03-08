package frc.robot.components.shooter;

public final class ShooterParameter {

    /** アライアンス側のエリアにボールを投げ入れるときのモーターの速度　単位：m/s*/
    public static double feedMps = 20;

    /** 詰まり解消用の逆回転出力（PercentOutput） 範囲: [-1.0 -> 0.0] */
    public static double reverseOutput = -0.3;
}
