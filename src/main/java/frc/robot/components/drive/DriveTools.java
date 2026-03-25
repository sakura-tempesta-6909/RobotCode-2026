package frc.robot.components.drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.domain.option.DriveOption.DriveSpeed;
import frc.robot.domain.state.StateGroup;
import frc.robot.usecase.UsecaseUtil;
public class DriveTools {
    public static ChassisSpeeds modifyChassisSpeed(ChassisSpeeds speeds, DriveSpeed speed) {
        switch (speed) {
            case s_fastDrive:
                return new ChassisSpeeds(
                    speeds.vxMetersPerSecond * DriveParameter.Speeds.FastDrive,
                    speeds.vyMetersPerSecond * DriveParameter.Speeds.FastDrive,
                    speeds.omegaRadiansPerSecond * DriveParameter.Speeds.FastThetaDrive  
                );
            case s_midDrive:
                return new ChassisSpeeds(
                    speeds.vxMetersPerSecond * DriveParameter.Speeds.MidDrive,
                    speeds.vyMetersPerSecond * DriveParameter.Speeds.MidDrive,
                    speeds.omegaRadiansPerSecond * DriveParameter.Speeds.MidThetaDrive  
                );
            case s_slowDrive:
                return new ChassisSpeeds(
                    speeds.vxMetersPerSecond * DriveParameter.Speeds.SlowDrive,
                    speeds.vyMetersPerSecond * DriveParameter.Speeds.SlowDrive,
                    speeds.omegaRadiansPerSecond * DriveParameter.Speeds.SlowThetaDrive
                );
            case s_stopDrive:
            default:
                return new ChassisSpeeds(0,0,0);
        }
    }

    

    /** 行くべき場所を計算する 
     * @param currentPosition 今のポジション(x[m],y[m])*/
    public static Translation2d calculateTargetPosition(Pose2d currentPosition){
        Translation2d HubPose = UsecaseUtil.getHubPosition().getTranslation();
        Translation2d currentPositon = currentPosition.getTranslation();

        /** T = H+3( R−H /｜R−H｜) */
        Translation2d targetPosition = HubPose.plus(
            currentPositon
                .minus(HubPose)
                .div(StateGroup.getDistanceToHub())
                .times(3)
        );
        return targetPosition;
    }
}
