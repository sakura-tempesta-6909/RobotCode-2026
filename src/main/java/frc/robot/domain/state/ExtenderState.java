package frc.robot.domain.state;

import edu.wpi.first.math.geometry.Pose2d;

public class ExtenderState {
    /** Extenderのモーターが動作しているか|動いている->true,停止->false*/
    public static boolean isMotorActive;
    /** 底面が地面と平行な場合を0度としたExtenderの角度[degree]|0<=currentAngle<=90|ロボット側に回転するのが正方向*/
    public static double currentAngle;
    /** intakeできる位置にExtenderがあるかないか|可能->true,不可->false */
    public static boolean isIntakePosition;
    /** extenderが初期位置(地面に対して鉛直方向)にあるかどうか|ある->true,ない->false */
    public static boolean isInitialPosition;
    /** extenderが低い側（intakeposition方向）のリミットスイッチにあたってるどうか|ある->true,ない->false */
    public static boolean lowerLimit;
    /** extenderが高い側（initialposition方向）のリミットスイッチにあたってるどうか|ある->true,ない->false */
    public static boolean upperLimit;
    /** extenderが目標値付近に達しているか */
    public static boolean isTargetPosition;
}


