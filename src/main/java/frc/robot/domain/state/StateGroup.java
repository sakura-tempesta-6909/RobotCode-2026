package frc.robot.domain.state;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.usecase.UsecaseConst;

public class StateGroup {
    public static boolean readyToScore() {
        return true;
    }

    public static boolean readyToShoot() {
        return ShooterState.isReadyToShoot && DriveState.isShootPosition;
    }

    public static double getDistanceToHub() {
        Translation2d currentPosition =  DriveState.drivePosition.getTranslation();
        return currentPosition.minus(UsecaseConst.Poses.TargetPoseToHub.getTranslation()).getNorm();
    }
}
