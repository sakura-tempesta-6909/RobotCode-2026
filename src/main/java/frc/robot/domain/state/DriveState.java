package frc.robot.domain.state;

import edu.wpi.first.math.geometry.Pose2d;

public class DriveState {
    /** スタート地点を原点(0,0)としたロボットの位置(x[m],y[m])|x軸:フィールドを上から見て奥方向（前方向）を正,Y軸:上から見てフィールドのを左方向を正  */
    public static Pose2d drivePosition;
    /** フィールド上の前向きを0としたロボットの角度|単位は度数法|ロボットを上から見て反時計回りが正  */
    public static double currentAngle;
    /** 停止状態を0としたロボットの速度[m/s] */
    public static double driveSpeed;
    /** ロボットの、x軸方向の運動が0の時を0とした、ロボットのX軸方向の速度[m/s] */
    public static double driveXSpeed;
    /** ロボットの、y軸方向の運動が0の時を0とした、ロボットのY軸方向の速度[m/s] */
    public static double driveYSpeed;
    /** ロボットが回転していない時を0とした、ロボットの回転速度[rad/s] |フィールドを上から見て反時計回りが正 */
    public static double driveRotationSpeed;
    /** ロボットがシュート可能な位置かどうか |可能->true,不可->true */
    public static boolean isShootPosition;
    
}
