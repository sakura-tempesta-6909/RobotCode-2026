package frc.robot.components.drive.infrastructure;

import frc.robot.components.drive.DriveConst;

import edu.wpi.first.math.geometry.Pose2d;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.targeting.PhotonPipelineResult;

import java.util.Optional;

public class Vision {
    double leftCameraTimestamp;
    double rightCameraTimestamp;

    Optional<Pose2d> leftCameraPose = Optional.empty();
    Optional<Pose2d> rightCameraPose = Optional.empty();

    public final PhotonPoseEstimator leftEstimator;
    public final PhotonPoseEstimator rightEstimator;

    public final PhotonCamera leftCamera;
    public final PhotonCamera rightCamera;

    public  Vision(){
        leftEstimator = new PhotonPoseEstimator(DriveConst.Vision.kTagLayout,DriveConst.Vision.kRobotToLeftCamera);
        rightEstimator = new PhotonPoseEstimator(DriveConst.Vision.kTagLayout,DriveConst.Vision.kRobotToRightCamera);

        leftCamera = new PhotonCamera("leftCamera");
        rightCamera = new PhotonCamera("rightCamera");
    }

    private  void updateLeftCamera(){
        if (!leftCamera.isConnected()) {
            leftCameraPose = Optional.empty();
            return;
        }
        for(PhotonPipelineResult result: leftCamera.getAllUnreadResults()){
            Optional<EstimatedRobotPose> visionEst = leftEstimator.estimateCoprocMultiTagPose(result);
            if (visionEst.isEmpty()) {
                visionEst = leftEstimator.estimateLowestAmbiguityPose(result);
            }
            visionEst.ifPresent(
                    est -> {
                        leftCameraPose = Optional.of(est.estimatedPose.toPose2d());
                        leftCameraTimestamp = est.timestampSeconds;
                    });
        }
    }

    private  void updateRightCamera(){
        for(PhotonPipelineResult result: rightCamera.getAllUnreadResults()){
            Optional<EstimatedRobotPose> visionEst = rightEstimator.estimateCoprocMultiTagPose(result);
            if (visionEst.isEmpty()) {
                visionEst = rightEstimator.estimateLowestAmbiguityPose(result);
            }
            visionEst.ifPresent(
                    est -> {
                        rightCameraPose = Optional.of(est.estimatedPose.toPose2d());
                        rightCameraTimestamp = est.timestampSeconds;
                    });
        }
    }

    public void periodic(){
        updateLeftCamera();
        updateRightCamera();
    }
}
