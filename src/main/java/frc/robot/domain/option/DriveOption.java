package frc.robot.domain.option;

public class DriveOption {
    public enum DriveOriented {
        /** Robot Oriented で動く */
        s_robotOriented,
        /** Field Oriented で動く */
        s_fieldOriented,
    }
    public static final RobotOption<DriveOriented> driveOriented = new RobotOption<DriveOriented>(DriveOriented.s_fieldOriented);


    public enum DriveSpeed {
        s_fastDrive,
        s_midDrive,
        s_slowDrive,
        s_stopDrive,
    }

    public static final RobotOption<DriveSpeed> driveSpeed = new RobotOption<DriveSpeed>(DriveSpeed.s_fastDrive);
}
