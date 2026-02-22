package frc.robot.domain.state;

public class StateGroup {
    public static boolean readyToScore() {
        return true;
    }

    public static boolean readyToShoot() {
        return ShooterState.isReadyToShoot && DriveState.isShootPosition;
    }
}
