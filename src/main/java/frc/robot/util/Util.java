package frc.robot.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.domain.DomainUtil;

public class Util {
    public static double deadband(double input){
        if(input < 0.15 && input > -0.15){
            return 0.0;
        } else {
            return input;
        }
    }

    public static void allSendConsole(){
        DomainUtil.allSendConsole();

        SmartDashboard.putString("Controller Mode",  RobotContainer.mode.toString());

    }
}
