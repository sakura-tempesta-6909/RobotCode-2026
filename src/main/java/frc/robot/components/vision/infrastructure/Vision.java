package frc.robot.components.vision.infrastructure;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.domain.repository.VisionRepository;
import frc.robot.domain.repository.VisionRepository.Data;
import org.photonvision.PhotonPoseEstimator;

public class Vision implements VisionRepository {

    public Vision() {
    }

    @Override
    public Data updateVision(PhotonPoseEstimator estimator){
        return new Data(new Pose2d(), 0.0);
    }

    /**
     * Stateへの書き込みを行う
     */
    @Override
    public void periodic() {
    }
    
}
