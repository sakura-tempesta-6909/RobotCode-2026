package frc.robot.components.shooter;

public final class ShooterParameter {

    /** アライアンス側のエリアにボールを投げ入れるときのモーターの出力割合　範囲: [-1.0, 1.0]*/
    public static double feedRatio = 0.5;
    
    /** シュート時のモーターの出力割合 範囲: [-1.0, 1.0]*/
    public static double shootRatio = 1.0;

    /** 詰まり解消用の逆回転出力（PercentOutput） 範囲: [-1.0, 1.0] */
    public static double reverseOutput = -0.3;
}
