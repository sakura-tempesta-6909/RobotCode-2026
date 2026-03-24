package frc.robot.components.drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.domain.option.DriveOption.DriveSpeed;
import static edu.wpi.first.units.Units.Rotation;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.domain.state.DriveState;
import frc.robot.domain.state.StateGroup;
import frc.robot.usecase.UsecaseConst;
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
     * @param targetDistance Hubを中心とした目標円周の半径（=目標地点のHubからの距離）[m] */
    public static Translation2d calculateTargetPosition(Pose2d currentPosition, double targetDistance){
        Translation2d HubPose = UsecaseUtil.getHubPosition().getTranslation();
        Translation2d currentPositon = currentPosition.getTranslation();

        Translation2d targetPosition;

        enum pattern{
            AroundHub,BlueLeftArea,BlueRightArea
        }

        pattern mode;
        Alliance color = DriverStation.getAlliance().get();
        switch(color){
            /** allianceが赤で */
            case Red:
                /** neutralゾーンまたは敵陣（青側）にいるとき*/
                if(currentPosition.getX() < HubPose.getX()){
                    /* 自陣(赤側)から見て左半分(=青から見て右半分)にいるとき*/
                    if(currentPosition.getY() < HubPose.getY()){
                        mode = pattern.BlueRightArea;
                    }
                    /* 自陣(赤側)から見て右半分(=青から見て左半分)にいるとき*/
                    else{
                        mode = pattern.BlueLeftArea;
                    }
                }
                /* 自陣（赤側）にいるとき*/
                else{
                    mode = pattern.AroundHub;
                }
                break;

            /** allianceが青で */
            case Blue:
                /** neutralゾーンまたは敵陣（赤側）にいるとき*/
                if(currentPosition.getX() > HubPose.getX()){
                    /* 自陣(青側)から見て右半分にいるとき*/
                    if(currentPosition.getY() <= HubPose.getY()){
                        mode = pattern.BlueRightArea;
                    }
                    /* 自陣(青側)から見て左半分にいるとき*/
                    else{
                        mode = pattern.BlueLeftArea;
                    }
                }
                /* 自陣（青側）にいるとき*/
                else{
                    mode = pattern.AroundHub;
                }
                break;
            
            default:
                mode =pattern.AroundHub;

        }

        switch(mode){
            /** AroundHubの時、HubからL[m]の円周上に移動する */
            case AroundHub:
                /** T = H+L( R−H /｜R−H｜) 
                 * T:目標地点の座標
                 * R:現在の座標
                 * H:Hubの座標
                 * L:Hubへの目標距離
                */
                targetPosition = HubPose.plus(
                currentPositon
                    .minus(HubPose)
                    .div(StateGroup.getDistanceToHub())
                    .times(targetDistance)
                );
                break;
            /** BlueLeftArea,BlueRightAreaの時、Hubの真横のうち近い方に移動する */
            case BlueRightArea:
                targetPosition = new Translation2d(HubPose.getX(),HubPose.getY() - targetDistance);
                break;
            case BlueLeftArea:
                targetPosition = new Translation2d(HubPose.getX(),HubPose.getY() + targetDistance);
                break;
            /** default状態の時、fieldの中心に移動する（実際はない） */
            default:
                targetPosition = UsecaseConst.Poses.CenterOfTheField;
        }    
        return targetPosition;
    }
}
