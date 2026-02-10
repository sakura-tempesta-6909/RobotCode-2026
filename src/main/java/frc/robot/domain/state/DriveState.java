package frc.robot.domain.state;

import edu.wpi.first.math.geometry.Pose2d;

public class DriveState {
    /** スタート地点（上から見て右下）を原点(0,0)としたロボットの位置(x[m],y[m])とフィールド上の前向きを0としたロボットの角度[degree]|x軸:フィールドを上から見て奥方向（前方向）を正,Y軸:上から見てフィールドのを左方向を正,角度：ロボットを上から見て反時計回りが正|PathPlannerの座標と一致 */
    public static Pose2d drivePosition;
    /** 停止状態を0としたロボットの速度[m/s] */
    public static double driveSpeed;
    /** ロボットの、x,y軸方向の運動が0の時を0とした、ロボットのX,Y軸方向の速度[m/s] */
    public static double[] artifactPositions = {Double.NaN, Double.NaN};
    /** ロボットが回転していない時を0とした、ロボットの回転速度[rad/s] |フィールドを上から見て反時計回りが正 */
    public static double driveRotationSpeed;
    /** ロボットがシュート可能な位置かどうか |可能->true,不可->false */
    public static boolean isShootPosition;
}

