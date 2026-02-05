package frc.robot.domain.state;

import edu.wpi.first.math.geometry.Pose2d;

public class ExtenderState {
    /** Extenderのmotorのスピード[割合]|-1.0<=IntakeMotorSpeed<=1.0|0が停止、1.0がロボット本体側に動く方向の最大速度、-1.0が反対方向の最大速度  */
    public static double motorSpeed;
    /** Extenderのモーターが動作しているか|動いている->true,停止->false*/
    public static boolean isMotorActive;
    /** 地面と並行な角度を0度としたExtenderの角度[degree]|0<=currentAngle<=90(鉛直方向、起動時に90度になる) */
    public static double currentAngle;
    /** intakeできる位置にExtenderがあるかないか|可能->true,不可->false */
    public static boolean isIntakeAngle;
    /** extenderが初期位置(地面に対して鉛直方向)にあるかどうか|ある->true,ない->false */
    public static boolean isInitialAngle;
    
}


