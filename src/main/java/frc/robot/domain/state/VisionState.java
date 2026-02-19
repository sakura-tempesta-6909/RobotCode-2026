package frc.robot.domain.state;

import edu.wpi.first.math.geometry.Pose2d;

public class VisionState {
    /** 左カメラのみから計算したロボットの位置*/
    public static Pose2d leftCameraPose;
    /** 右カメラのみから計算したロボットの位置*/
    public static Pose2d rightCameraPose;
    /** 左のカメラのタイムスタンプ*/
    public static double leftCameraTimestamp;
    /** 右のカメラのタイムスタンプ*/
    public static double rightCameraTimestamp;
    /** 左のカメラが接続されているか*/
    public static boolean leftCameraConnected;
    /** 右のカメラが接続されているか*/
    public static boolean rightCameraConnected;
    /** 左のカメラにタグが映ってるか*/
    public static boolean leftHasTarget;
    /** 右のカメラにタグが映ってるか*/
    public static boolean rightHasTarget;
}
