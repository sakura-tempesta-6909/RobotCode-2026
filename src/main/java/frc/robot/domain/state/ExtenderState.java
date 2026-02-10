package frc.robot.domain.state;

import edu.wpi.first.math.geometry.Pose2d;

public class ExtenderState {
    /** Extenderのモーターが動作しているか|動いている->true,停止->false*/
    public static boolean isMotorActive;
    /** 起動時の収納時を0度としたExtenderの角度[degree]|0<=currentAngle<=90(展開時に90度になる) */
    public static double currentAngle;
    /** intakeできる位置にExtenderがあるかないか|可能->true,不可->false */
    public static boolean isIntakeAngle;
    /** extenderが初期位置(地面に対して鉛直方向)にあるかどうか|ある->true,ない->false */
    public static boolean isInitialAngle;
    
}


