package frc.robot.components.drive.infrastructure;

import frc.robot.components.drive.DriveConst;

import edu.wpi.first.math.geometry.Pose2d;

import org.photonvision.simulation.*;

import org.littletonrobotics.junction.Logger;
import edu.wpi.first.math.geometry.Rotation2d;

public class VisionSim extends Vision{

    private final VisionSystemSim visionSim;

    private final PhotonCameraSim leftCameraSim;
    private final PhotonCameraSim rightCameraSim;

    public VisionSim() {

        visionSim = new VisionSystemSim("main");

        visionSim.addAprilTags(DriveConst.Vision.kTagLayout);

        SimCameraProperties leftProps = buildCameraProperties(
                DriveConst.Vision.kLeftCameraResW,
                DriveConst.Vision.kLeftCameraResH,
                DriveConst.Vision.kLeftCameraFovDeg,
                DriveConst.Vision.kLeftCameraAvgErrorPx,
                DriveConst.Vision.kLeftCameraErrorStdDevPx,
                DriveConst.Vision.kLeftCameraFps,
                DriveConst.Vision.kLeftCameraAvgLatencyMs,
                DriveConst.Vision.kLeftCameraLatencyStdDevMs);

        SimCameraProperties rightProps = buildCameraProperties(
                DriveConst.Vision.kRightCameraResW,
                DriveConst.Vision.kRightCameraResH,
                DriveConst.Vision.kRightCameraFovDeg,
                DriveConst.Vision.kRightCameraAvgErrorPx,
                DriveConst.Vision.kRightCameraErrorStdDevPx,
                DriveConst.Vision.kRightCameraFps,
                DriveConst.Vision.kRightCameraAvgLatencyMs,
                DriveConst.Vision.kRightCameraLatencyStdDevMs);

        leftCameraSim = new PhotonCameraSim(leftCamera, leftProps);
        rightCameraSim = new PhotonCameraSim(rightCamera, rightProps);

        visionSim.addCamera(leftCameraSim, DriveConst.Vision.kRobotToLeftCamera);
        visionSim.addCamera(rightCameraSim, DriveConst.Vision.kRobotToRightCamera);

        leftCameraSim.enableDrawWireframe(true);
        rightCameraSim.enableDrawWireframe(true);
    }

    private static SimCameraProperties buildCameraProperties(
            int resW, int resH, double fovDeg,
            double avgErrorPx, double errorStdDevPx,
            double fps, double avgLatencyMs, double latencyStdDevMs) {

        SimCameraProperties props = new SimCameraProperties();
        props.setCalibration(resW, resH, Rotation2d.fromDegrees(fovDeg)); 
        props.setCalibError(avgErrorPx, errorStdDevPx);
        props.setFPS(fps);
        props.setAvgLatencyMs(avgLatencyMs);
        props.setLatencyStdDevMs(latencyStdDevMs);
        return props;
    }

    /**
     * シミュレーション時にロボットの真値 Pose を渡して仮想カメラ映像を更新する。
     * Drive の simulationPeriodic() などから毎周期呼び出す。
     *
     * @param robotPose シミュレーション上のロボットの真値 Pose（フィールド座標系）
     */
    public void update(Pose2d robotPose) {
        visionSim.update(robotPose);
    }

    public void periodic() {
        updateLeftCamera();
        updateRightCamera();

        // 真のロボット位置
        Logger.recordOutput(
            "Vision/SimRobotPose",
            visionSim.getRobotPose()
        );

        // 左カメラ
        boolean leftHasTarget =
            leftCamera.getLatestResult().hasTargets();

        Logger.recordOutput(
            "Vision/LeftHasTarget",
            leftHasTarget
        );

        // 右カメラ
        boolean rightHasTarget =
            rightCamera.getLatestResult().hasTargets();

        Logger.recordOutput(
            "Vision/RightHasTarget",
            rightHasTarget
        );

        // Vision.javaで計算されたPoseをそのまま出す
        Logger.recordOutput("Vision/LeftEstimatedPose", leftCameraPose.orElse(new Pose2d()));
        Logger.recordOutput("Vision/RightEstimatedPose", rightCameraPose.orElse(new Pose2d()));
    }
}