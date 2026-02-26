package frc.robot.components.extender;

public final class ExtenderParameter {
    public static final class SoftLimit{
        /** Extenderのソフトリミット(リミットスイッチが壊れるかもしれないからね！)[degree] */
        public static final double ForwardSoftLimit = 90;
        public static final double ReverseSoftLimit = 0;
    }

    public  static final class PID{
        public static final double RaisingP = 0;
        public static final double RaisingI = 0;
        public static final double RaisingD = 0;
        public static final double RaisingIZone = 0;

        public static final double LoweringP = 0;
        public static final double LoweringI = 0;
        public static final double LoweringD = 0;
        public static final double LoweringIZone = 0;

        public static final double EndexerVelocityP = 0;
        public static final double EndexerVelocityI = 0;
        public static final double EndexerVelocityD = 0;
    }
    /** 指定の位置に移動したか判断する時に許す誤差の範囲 | 単位は度[degree]*/
    public static final double allowableError = 2.5;

    /** 初期位置の角度[degree]。地面とextenderの底が並行な時に0,垂直な時90度*/
    public static final double InitialAngle = 90;

    /** ff制御のちから[単位なし] */
    public static final double FFPower = 0.0;


    
}
