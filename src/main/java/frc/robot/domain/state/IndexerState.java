package frc.robot.domain.state;

public class IndexerState {
    public class LongRollerIndexer{
        /** モーターのスピード | shooterに送る方向が正 | [-1, 1] */
        public static double motorSpeed;
        /** モーターが動いているか | 動いてたらtrue*/
        public static boolean isMotorActive;
    }
    public class StarWheelIndexer{
        /** モーターのスピード | shooterに送る方向が正 | [-1, 1] */
        public static double motorSpeed;
        /** モーターが動いているか | 動いてたらtrue*/
        public static boolean isMotorActive;
    }
}
