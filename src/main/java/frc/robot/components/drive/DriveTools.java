package frc.robot.components.drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.domain.option.DriveOption.DriveSpeed;
import static edu.wpi.first.units.Units.Rotation;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.domain.state.DriveState;
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
     * @param currentPosition 今のポジション(x[m],y[m])
     * @param distanceToHub 目標位置のHubからの距離[m]
     * */
    public static Translation2d calculateTargetPosition(Pose2d currentPosition, double distanceToShoot){
        
        Translation2d HubPose = UsecaseUtil.getHubPosition().getTranslation();
        Translation2d currentPositon = currentPosition.getTranslation();

        /** T = H+L( R−H /｜R−H｜) 
         * T:目標の座標
         * H:Hubの中心座標
         * R:ロボットの座標
        */
        Translation2d targetPosition;

        /** Y座標がHubより手前ならHubから半径3mの円周上に移動する*/
        if(currentPosition.getY() < UsecaseUtil.getHubPosition().getY()){
            targetPosition = HubPose.plus(
            currentPositon
                .minus(HubPose)
                .div(StateGroup.getDistanceToHub())
                .times(distanceToShoot)
            );
        }else{
            /** Y座標がHubより奥でかつ*/
            /** X座標がHubより手前なら手前側のY座標がHubと同じ地点（自陣から見てHubの左横）に移動する*/
            if(currentPosition.getX() < UsecaseUtil.getHubPosition().getX()){
                targetPosition = new Translation2d(UsecaseUtil.getHubPosition().getX() -distanceToShoot ,UsecaseUtil.getHubPosition().getY());
            }else{
                /** X座標がHubより奥なら奥側のY座標がHubと同じ地点（自陣から見てHubの右横）に移動する*/
                targetPosition = new Translation2d(UsecaseUtil.getHubPosition().getX() +distanceToShoot ,UsecaseUtil.getHubPosition().getY());

            }

        }
        return targetPosition;
    }


}
