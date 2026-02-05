package frc.robot.components.indexer;

public final class IndexerParameter {
    public static final class Speed{
        /** Shooterに送る時のスピード | 送る方に回す方向が正 | [0, 1]*/
        public static final double IndexerRollerFeedSpeed = 0.5;
        /** 逆回転する時のスピード | shooterに送るのと逆の方向なので負 | [-1, 0]*/
        public static final double IndexerRollerReverseSpeed = -0.5;
        /** 停止時のモーターのスピード | 停止なので0 | [0, 1] */
        public static final double Neutral = 0;
    }
}
