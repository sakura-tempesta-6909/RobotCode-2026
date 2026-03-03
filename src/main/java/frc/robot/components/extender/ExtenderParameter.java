package frc.robot.components.extender;

public final class ExtenderParameter {
    /** デフォルトの状態のextenderの底面と地面の角度 [degree]*/
    public static final double InitialAngle = 90;
    /** Intakeできるextenderの底面と地面の角度[degree] */
    public static final double IntakeAngle = 0;

    public static final class SpeedAndPower {
        /** 初期位置の状態の方向に最大の力で動かす */
        public static final double MaxPowerToIntakePosition = 1;
        /** Intakeする角度の方向に最大の力で動かす */
        public static final double MaxPowerToInitialPosition = -1;
    }
}
