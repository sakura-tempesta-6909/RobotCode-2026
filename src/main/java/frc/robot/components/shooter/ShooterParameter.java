package frc.robot.components.shooter;

public final class ShooterParameter {

    /** アライアンス側に投げ入れるときのモーターの出力割合　範囲-1から1まで */
    public static double feedSpeed = 0.5;
    
    /** シュート時のモーターの出力割合 範囲-1から1まで*/
    public static double shootSpeed = 1.0;

    /** 逆回転させるときのモーターの出力割合 */
    public static double reverseSpeed = -0.3;
}
