package frc.robot.components.extender;

import com.revrobotics.spark.ClosedLoopSlot;

public final class ExtenderConst {
    public static final class Ports{
        /** extenderのモーターのCANID */
        public static final int extenderMotor = 16;
        /** extenderの展開の上限と下限のスイッチ */
        public static final int upperExtenderLimitSwitch = 1;
        public static final int lowerExtenderLimitSwitch = 2;
        

    }
    public static final class Slot{
        /** 上げる時のPIDのslot */
        public static final ClosedLoopSlot ExtenderRaisingSlot = ClosedLoopSlot.kSlot0;
        /** 下ろす時のPIDのslot */
        public static final ClosedLoopSlot ExtenderLoweringSlot = ClosedLoopSlot.kSlot1;
        /** 速度の時のPIDのslot */
        public static final ClosedLoopSlot ExtenderVelocitySlot = ClosedLoopSlot.kSlot2;
    }
    /** extenderのモーターのギア比。モーターの軸に対するextenderの軸 
     * モーターのギア比(36) × モーターとでかいシャフトのギア比(1.0) × でかいシャフトとextenderのギア比(50/22)
    */
    public static final double GearRatio = 36 * 1.0 * (50/22);;

    /** モーターを動かす最大のRPM
     *  これをもとにパーセントで制御している| 単位はRPM*/
    public static final double ExtenderMotorMaxRPM = 5676;
    /** モーターを動いていると判定する最小の速度[RPM]（動いているとき）*/
    public static final double ExtenderMotorMinRotation = 0.1;
    /** エンコーダーの初期位置[degree] */
    public static final double InitialEncoderPosition = 90.0;
    /** 1回転あたりのエンコーダーのカウント数 */
    public static final double EncoderCountsPerRevolution = 409;
    
}
