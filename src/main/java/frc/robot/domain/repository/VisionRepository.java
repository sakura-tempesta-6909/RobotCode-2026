package frc.robot.domain.repository;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.photonvision.PhotonPoseEstimator;

public interface VisionRepository extends Subsystem {

    record Data(Pose2d estimatedPose, double timeStanp) {}
    /**
     * 引数に追加されたphotonPoseEstimatorからPoseとtimestanpの情報を抜き取る
     * @param estimator photonPoseEstimator
     */
    Data updateVision(PhotonPoseEstimator estimator);
}
