package frc.robot.components.drive.infrastructure;

import frc.robot.components.drive.DriveConst;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.RobotBase;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.SimCameraProperties;
import org.photonvision.simulation.VisionSystemSim;
import org.photonvision.targeting.PhotonPipelineResult;
import org.littletonrobotics.junction.Logger;
import edu.wpi.first.math.geometry.Rotation2d;

import java.util.Optional;

/**
 * PhotonVisionを使用してロボットの自己位置推定を行うクラス
 */
public class Vision {
    /** 左カメラの最新データ取得時のタイムスタンプ | [s] */
    double leftCameraTimestamp;
    /** 右カメラの最新データ取得時のタイムスタンプ | [s] */
    double rightCameraTimestamp;

    /** 左カメラから推定されたロボットの位置（値がない場合はEmpty） | フィールド座標系 */
    Optional<Pose2d> leftCameraPose = Optional.empty();
    /** 右カメラから推定されたロボットの位置（値がない場合はEmpty） | フィールド座標系 */
    Optional<Pose2d> rightCameraPose = Optional.empty();

    /** フィールド全体を管理する仮想ビジョンシステム */
    private final VisionSystemSim visionSim;

    /** 仮想左カメラ */
    private final PhotonCameraSim leftCameraSim;
    /** 仮想右カメラ */
    private final PhotonCameraSim rightCameraSim;

    public final PhotonPoseEstimator leftEstimator;
    public final PhotonPoseEstimator rightEstimator;

    public final PhotonCamera leftCamera;
    public final PhotonCamera rightCamera;

    /**
     * Visionクラスのコンストラクタ。カメラとEstimatorの初期化を行う
     */
    public Vision(){
        leftEstimator = new PhotonPoseEstimator(DriveConst.Vision.kTagLayout, DriveConst.Vision.kRobotToLeftCamera);
        rightEstimator = new PhotonPoseEstimator(DriveConst.Vision.kTagLayout, DriveConst.Vision.kRobotToRightCamera);

        leftCamera = new PhotonCamera("leftCamera");
        rightCamera = new PhotonCamera("rightCamera");

        if (RobotBase.isSimulation()) {
            visionSim = new VisionSystemSim("main");

            // フィールド上の AprilTag レイアウトをシムに登録
            visionSim.addAprilTags(DriveConst.Vision.kTagLayout);

            // 左カメラのプロパティ設定
            SimCameraProperties leftProps = buildCameraProperties(
                    DriveConst.Vision.kLeftCameraResW,
                    DriveConst.Vision.kLeftCameraResH,
                    DriveConst.Vision.kLeftCameraFovDeg,
                    DriveConst.Vision.kLeftCameraAvgErrorPx,
                    DriveConst.Vision.kLeftCameraErrorStdDevPx,
                    DriveConst.Vision.kLeftCameraFps,
                    DriveConst.Vision.kLeftCameraAvgLatencyMs,
                    DriveConst.Vision.kLeftCameraLatencyStdDevMs);

            // 右カメラのプロパティ設定
            SimCameraProperties rightProps = buildCameraProperties(
                    DriveConst.Vision.kRightCameraResW,
                    DriveConst.Vision.kRightCameraResH,
                    DriveConst.Vision.kRightCameraFovDeg,
                    DriveConst.Vision.kRightCameraAvgErrorPx,
                    DriveConst.Vision.kRightCameraErrorStdDevPx,
                    DriveConst.Vision.kRightCameraFps,
                    DriveConst.Vision.kRightCameraAvgLatencyMs,
                    DriveConst.Vision.kRightCameraLatencyStdDevMs);

            leftCameraSim  = new PhotonCameraSim(leftCamera,  leftProps);
            rightCameraSim = new PhotonCameraSim(rightCamera, rightProps);

            // カメラをロボット上の取り付け位置（Transform3d）と共にシムに追加
            visionSim.addCamera(leftCameraSim,  DriveConst.Vision.kRobotToLeftCamera);
            visionSim.addCamera(rightCameraSim, DriveConst.Vision.kRobotToRightCamera);

            // AdvantageScope / Glass でのデバッグ描画を有効化
            leftCameraSim.enableDrawWireframe(true);
            rightCameraSim.enableDrawWireframe(true);
        } else {
            // 実機ではシム関連オブジェクトは不要
            visionSim     = null;
            leftCameraSim  = null;
            rightCameraSim = null;
        }
    }

    /**
     * シミュレーション時にロボットの真値 Pose を渡して仮想カメラ映像を更新する。
     * Drive の simulationPeriodic() などから毎周期呼び出す。
     *
     * @param robotPose シミュレーション上のロボットの真値 Pose（フィールド座標系）
     */
    public void updateSimulation(Pose2d robotPose) {
        if (RobotBase.isSimulation() && visionSim != null) {
            visionSim.update(robotPose);
        }
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
     * 左カメラの未読の結果を取得し、ロボットの位置（Pose）とタイムスタンプを更新する
     */
    private void updateLeftCamera(){
        if (!leftCamera.isConnected()) {
            leftCameraPose = Optional.empty();
            return;
        }
        for(PhotonPipelineResult result: leftCamera.getAllUnreadResults()){
            // 複数のタグが見えている場合の推定
            Optional<EstimatedRobotPose> visionEst = leftEstimator.estimateCoprocMultiTagPose(result);
            if (visionEst.isEmpty()) {
                // 単一タグのみの場合、最も信頼できるポーズを推定
                visionEst = leftEstimator.estimateLowestAmbiguityPose(result);
            }
            visionEst.ifPresent(
                    est -> {
                        leftCameraPose = Optional.of(est.estimatedPose.toPose2d());
                        leftCameraTimestamp = est.timestampSeconds;
                    });
        }
    }

    /**
     * 右カメラの未読の結果を取得し、ロボットの姿勢（Pose）とタイムスタンプを更新する
     */
    private void updateRightCamera(){
        if (!rightCamera.isConnected()) {
            rightCameraPose = Optional.empty();
            return;
        }
        // 未読のパイプライン結果をすべて処理する
        for(PhotonPipelineResult result: rightCamera.getAllUnreadResults()){
            // 複数のタグが見えている場合の推定
            Optional<EstimatedRobotPose> visionEst = rightEstimator.estimateCoprocMultiTagPose(result);
            if (visionEst.isEmpty()) {
                // 単一タグのみの場合、最も信頼できるポーズを推定
                visionEst = rightEstimator.estimateLowestAmbiguityPose(result);
            }
            visionEst.ifPresent(
                    est -> {
                        rightCameraPose = Optional.of(est.estimatedPose.toPose2d());
                        rightCameraTimestamp = est.timestampSeconds;
                    });
        }
    }

    /**
     * 各カメラのデータを更新する。Driveのperiodicなどで毎回に実行する
     */
    public void periodic(){
        updateLeftCamera();
        updateRightCamera();
        
        // AdvantageKitでのロギング処理
        // Optionalの中身がある場合だけPoseを送り、ない場合は空のPoseを送る（または送らない）
        Logger.recordOutput("Vision/LeftEstimatedPose", leftCameraPose.orElse(new Pose2d()));
        Logger.recordOutput("Vision/RightEstimatedPose", rightCameraPose.orElse(new Pose2d()));
        
        // デバッグ用：見えているかどうかをBooleanで送ると画角判定に便利
        Logger.recordOutput("Vision/LeftHasTarget", leftCameraPose.isPresent());
        Logger.recordOutput("Vision/RightHasTarget", rightCameraPose.isPresent());

        if (RobotBase.isSimulation() && visionSim != null) {
            // Field2dは直接recordOutputできないため、各カメラのターゲット情報をログに出す
            Logger.recordOutput("Vision/SimRobotPose", visionSim.getRobotPose());
        }
    }
    
}
