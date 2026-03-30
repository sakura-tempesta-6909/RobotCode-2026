package frc.robot.components.indexer;

public final class IndexerParameter {
    public static final class LongRollerIndexer{
        /** Shooterに送る時のスピード | 送る方に回す方向が正 | [0, 1]*/
        public static final double FeedSpeed = 1.0;
        /** 逆回転する時のスピード | shooterに送るのと逆の方向なので負 | [-1, 0]*/
        public static final double ReverseSpeed = -1.0;
        /** 停止時のモーターのスピード | 停止なので0 | [0, 1] */
        public static final double Neutral = 0;
        /** Indexerの電流制限 | 単位[A] */
        public static int CurrentLimit = 80;
    }
    public static final class StarWheelIndexer{
        /** Shooterに送る時のスピード | 送る方に回す方向が正 | [0, 1]*/
        public static final double FeedSpeed = 1.0;
        /** 逆回転する時のスピード | shooterに送るのと逆の方向なので負 | [-1, 0]*/
        public static final double ReverseSpeed = -1.0;
        /** 停止時のモーターのスピード | 停止なので0 | [0, 1] */
        public static final double Neutral = 0;
        /** Indexerの電流制限 | 単位[A] */
        public static int CurrentLimit = 80;
    }
}
