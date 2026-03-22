package frc.robot.domain;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.domain.option.DriveOption;
import frc.robot.domain.state.DriveState;
import frc.robot.domain.state.ExampleState;
import frc.robot.domain.state.ExtenderState;
import frc.robot.domain.state.ShooterState;
import org.littletonrobotics.junction.Logger;

public class DomainUtil {
    static Field2d field;
    public DomainUtil() {
        field = new Field2d();
    }
    public static void allSendConsole() {
        field.setRobotPose(DriveState.drivePosition);
        SmartDashboard.putNumber("exp c pos", ExampleState.currentPosition);
        SmartDashboard.putString("Drive/Speed", DriveOption.driveSpeed.toString());
        SmartDashboard.putData("Field", field);
        SmartDashboard.putBoolean("Drive/isShoot", DriveState.isShootPosition);
        SmartDashboard.putNumber("Extender/CurrentAngle", ExtenderState.currentAngle);
        SmartDashboard.putBoolean("Extender/isIntakeAngle", ExtenderState.isIntakePosition);
        SmartDashboard.putBoolean("Extender/isInitialAngle", ExtenderState.isInitialPosition);
        SmartDashboard.putNumber("Shooter/CurrentSpeed", ShooterState.shooterSurfaceSpeedMps);
        SmartDashboard.putBoolean("Shooter/ReadyToShoot", ShooterState.isReadyToShoot);

        Logger.recordOutput("Drive/Speed", DriveOption.driveSpeed.toString());
        Logger.recordOutput("Drive/isShoot", DriveState.isShootPosition);
        Logger.recordOutput("Extender/CurrentAngle", ExtenderState.currentAngle);
        Logger.recordOutput("Extender/isIntakeAngle", ExtenderState.isIntakePosition);
        Logger.recordOutput("Extender/isInitialAngle", ExtenderState.isInitialPosition);
        Logger.recordOutput("Shooter/CurrentSpeed", ShooterState.shooterSurfaceSpeedMps);
        Logger.recordOutput("Shooter/ReadyToShoot", ShooterState.isReadyToShoot);


    }
}
