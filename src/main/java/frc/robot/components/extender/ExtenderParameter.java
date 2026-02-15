package frc.robot.components.extender;

public final class ExtenderParameter {
    public static final class SoftLimit{
        /** Liftのソフトリミット(リミットスイッチが壊れるかもしれないからね！) */
        public static final double ForwardSoftLimit = 0;
        public static final double ReverseSoftLimit = 90;
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
    }
    /** 指定の位置に移動したか判断する時に許す誤差の範囲 | 単位は度(degree)*/
    public static final double allowableError = 2.5;
    /** モーターを動かす最大のRPM
     *  これをもとにパーセントで制御している| 単位はRPM*/
    public static final double ExtenderMotorMaxRPM = 5676;

    public static final double InitialAngle = 90;

    public static final double FFPower = 0.0;


    
}
