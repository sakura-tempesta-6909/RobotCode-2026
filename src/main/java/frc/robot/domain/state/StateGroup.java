package frc.robot.domain.state;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.components.drive.DriveParameter;

public class StateGroup {
    public static boolean readyToScore() {
        return true;
    }

    public static boolean readyToShoot() {
        return ShooterState.isReadyToShoot && DriveState.isShootPosition;
    }

    public static double currentPositionToHub() {
        Translation2d currentPosition =  DriveState.drivePosition.getTranslation();
        return currentPosition.minus(DriveParameter.Poses.inFrontOfGoal.getTranslation()).getNorm();
    }
}
