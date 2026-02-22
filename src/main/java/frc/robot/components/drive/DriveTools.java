package frc.robot.components.drive;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.domain.option.DriveOption.DriveSpeed;

import static edu.wpi.first.units.Units.Rotation;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.domain.state.DriveState;

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

    /** シュート位置に近づいているか計算する 
     * @param targetPosition :これから向かうシュートする場所(x[m],y[m])
     * @param currentPosition :現在の場所(x[m],y[m])
     * @return shoot可能な場所にいるか */ 
    public static boolean isShootPosition(Pose2d targetPosition, Pose2d currentPosition){
        Pose2d pose = targetPosition;
        Pose2d current = currentPosition;
        Pose2d relativePose = pose.relativeTo(current);
        double arrowedDifference = DriveParameter.Differences.arrowedDifference;
        
        double Xdifference = relativePose.getX();
        double Ydifference = relativePose.getY();
        boolean isShootPosition = Math.abs(Xdifference) < arrowedDifference && Math.abs(Ydifference) < arrowedDifference;
        return isShootPosition;

    }

    /** 行くべき場所を計算する 
     * @param currentPosition 今のポジション(x[m],y[m])*/
    public static Pose2d calculateTargetPosition(Pose2d currentPosition){
        /*TODO
         https://sakuratempesta6909.sharepoint.com/:w:/s/frc/IQCfFKi-SVNCSYFRP7-lMi7UAYWVTCfZH4Fo4sKpiDRmdCw?e=dUodWL
         上のやつを用いてターゲットの座標を出す*/
        Pose2d targetPosition = null;
        return targetPosition;
    }
}
