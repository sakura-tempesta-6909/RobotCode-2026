package frc.robot.domain.state;

public class IndexerState {
    public class LongRollerIndexer{
        /** モーターのスピード | shooterに送る方向が正 | [-1, 1] */
        public static double motorSpeed;
        /** モーターが動いているか | 動いてたらtrue*/
        public static boolean isMotorActive;
        /** モーターの出力電流 [A] */
        public static double outputCurrent;
        /** モーターの出力 [-1, 1] */
        public static double appliedOutput;
        /** バス電圧 [V] */
        public static double busVoltage;
    }
    public class StarWheelIndexer{
        /** モーターのスピード | shooterに送る方向が正 | [-1, 1] */
        public static double motorSpeed;
        /** モーターが動いているか | 動いてたらtrue*/
        public static boolean isMotorActive;
            /** モーターの出力電流 [A] */
        public static double outputCurrent;
        /** モーターの出力 [-1, 1] */
        public static double appliedOutput;
        /** バス電圧 [V] */
        public static double busVoltage;

    }
}
