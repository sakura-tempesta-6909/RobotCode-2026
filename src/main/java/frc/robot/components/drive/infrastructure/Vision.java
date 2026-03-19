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
    public double leftCameraTimestamp;
    /** 右カメラの最新データ取得時のタイムスタンプ | [s] */
    public double rightCameraTimestamp;

    /** 左カメラから推定されたロボットの位置（値がない場合はEmpty） | フィールド座標系 */
    public Optional<Pose2d> leftCameraPose = Optional.empty();
    /** 右カメラから推定されたロボットの位置（値がない場合はEmpty） | フィールド座標系 */
    public Optional<Pose2d> rightCameraPose = Optional.empty();


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
    }


    /**
     * 左カメラの未読の結果を取得し、ロボットの位置（Pose）とタイムスタンプを更新する
     */
    protected void updateLeftCamera(){
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
    protected  void updateRightCamera(){
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
    }
    
}
