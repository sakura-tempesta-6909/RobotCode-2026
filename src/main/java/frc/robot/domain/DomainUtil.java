package frc.robot.domain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.domain.option.DriveOption;
import frc.robot.domain.state.ExampleState;

public class DomainUtil {
    public static void allSendConsole() {
        SmartDashboard.putNumber("exp c pos", ExampleState.currentPosition);

        SmartDashboard.putString("Drive speed", DriveOption.driveSpeed.toString());
    }
}
