package frc.robot.domain.state;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class DriveState {
    /** スタート地点（自allianceから正面を見て一番右手前）を原点(0,0)としたロボットの位置(x[m],y[m])とフィールド上の前向きを0としたロボットの角度[degree]
     * |x軸:フィールドを上から見て奥方向（前方向）を正,Y軸:上から見てフィールドのを左方向を正,角度：ロボットを上から見て反時計回りが正
     * |PathPlannerの座標と一致 (参照→)https://docs.wpilib.org/en/stable/docs/software/basic-programming/coordinate-system.html*/
    public static Pose2d drivePosition;
    /** ロボットの、(x,y)軸方向の運動が0の時を0とした、ロボットの(X,Y)軸方向の速度[m/s]とロボットが回転していない時を0とした、ロボットの回転速度[rad/s] |フィールドを上から見て反時計回りが正 */
    public static ChassisSpeeds driveXYOmegaSpeed;
    /** ロボットがシュート可能な位置かどうか |可能->true,不可->false */
    public static boolean isShootPosition;
    /** スタート地点（自allianceから正面を見て一番右手前）を原点(0,0)としたロボットの目標位置(x[m],y[m])とフィールド上の前向きを0としたロボットの目標角度[degree]
     * |x軸:フィールドを上から見て奥方向（前方向）を正,Y軸:上から見てフィールドのを左方向を正,角度：ロボットを上から見て反時計回りが正
     * |PathPlannerの座標と一致 (参照→)https://docs.wpilib.org/en/stable/docs/software/basic-programming/coordinate-system.html*/
    public static Pose2d targetPosition;
}


