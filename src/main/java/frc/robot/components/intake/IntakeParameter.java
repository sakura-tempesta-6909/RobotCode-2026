package frc.robot.components.intake;

public final class IntakeParameter {
    public static final class Speed{
        /** 停止時のモーターのスピード*/
        public static final double Neutral = 0;
        /** Fuelを取り込む時のスピード | Fuelが取り込まれる方向を正 | [0,1]*/
        public static final double IntakeRollerSpeed = 1.0;
        /** Fuelを吐き出すときのスピード | Fuelを吐き出す方向なので負 | [-1,0]*/
        public static final double OuttakeRollerSpeed = -1.0;
    }
    
}