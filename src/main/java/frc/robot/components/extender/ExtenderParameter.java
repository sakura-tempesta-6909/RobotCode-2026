package frc.robot.components.extender;

public final class ExtenderParameter {

    public static final class Power{
        public static final double Neutral = 0;
    }

    public static final class SoftLimit{
        /** Extenderのソフトリミット(リミットスイッチが壊れるかもしれないからね！)[degree] */
        public static final double ForwardSoftLimit = 90;
        public static final double ReverseSoftLimit = 0;
    }

    public  static final class PID{
        public static final double PositionP = 0.09;
        public static final double PositionI = 0;
        public static final double PositionD = 0;
        public static final double PositionIZone = 0;

        public static final double EndexerVelocityP = 0;
        public static final double EndexerVelocityI = 0;
        public static final double EndexerVelocityD = 0;
    }

    
    /** ExtenderをIntake位置に動かすときの最大の力[Percent Output] */
    public static final double MaxPowerToIntakePosition = 1.0;
    /** Extenderを上方向に動かすときの最大の力[Percent Output] */
    public static final double MaxPowerToInitialPosition = -1.0;
    
    
    /** 指定の位置に移動したか判断する時に許す誤差の範囲 | 単位は度[degree]*/
    public static final double allowableError = 2.5;

    /** 初期位置の角度[degree]。地面とextenderの底が並行な時に0,垂直な時90度*/
    public static final double InitialAngle = 90;
    public static final double IntakeAngle = 0;

    public static final double arrowedAngleToJudgeIsInitialAngle = 0.5;

    /** ff制御のちから[Voltage] */
    public static final double FFPower = 0.30;
    public static final double kCosRatio = ExtenderConst.EncoderCountsPerRevolution * 1 / ExtenderConst.GearRatio;


    
}
