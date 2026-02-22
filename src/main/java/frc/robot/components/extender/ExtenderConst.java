package frc.robot.components.extender;

import com.revrobotics.spark.ClosedLoopSlot;

public final class ExtenderConst {
    public static final class Ports{
        /** extenderのモーターのCANID */
        public static final int extenderMotor = 14;
        /** extenderの展開の上限と下限のスイッチ */
        public static final int upperExtenderLimitSwitch = 1;
        public static final int lowerExtenderLimitSwitch = 2;
        

    }
    public static final class Slot{
        /** 上げる時のPIDのslot */
        public static final ClosedLoopSlot ExtenderRaisingSlot = ClosedLoopSlot.kSlot0;
        /** 下ろす時のPIDのslot */
        public static final ClosedLoopSlot ExtenderLoweringSlot = ClosedLoopSlot.kSlot1;

    }
    /** extenderのモーターのギア比。モーターの軸に対するextenderの軸 */
    public static final double GearRatio = 1;

    /** モーターを動かす最大のRPM
     *  これをもとにパーセントで制御している| 単位はRPM*/
    public static final double ExtenderMotorMaxRPM = 5676;
    /** モーターを動かす最小のRPM（動いているとき）*/
    public static final double ExtenderMotorMinRotation = 0.1;

    
}
