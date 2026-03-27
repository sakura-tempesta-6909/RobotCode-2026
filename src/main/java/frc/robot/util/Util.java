package frc.robot.util;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.components.drive.DriveParameter;
import frc.robot.components.drive.DriveTools;
import frc.robot.domain.DomainUtil;
import frc.robot.domain.state.DriveState;
import frc.robot.domain.state.ExtenderState;
import frc.robot.usecase.UsecaseUtil;
import frc.robot.usecase.commands.DriveCommands;

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

        // SmartDashboard.putString("Controller Mode",  RobotContainer.mode.toString());
        // SmartDashboard.putString("Command/Drive", RobotContainer.getDriveInstance().getCurrentCommand().toString());
        // SmartDashboard.putString("Command/Extender", RobotContainer.getExtenderInstance().getCurrentCommand().toString());
        // SmartDashboard.putString("Command/Shooter", RobotContainer.getShooterInstance().getCurrentCommand().toString());
        // SmartDashboard.putString("Command/Indexer", RobotContainer.getIndexerInstance().getCurrentCommand().toString());
        // SmartDashboard.putString("Command/Intake", RobotContainer.getIntakeInstance().getCurrentCommand().toString());

        // Logger.recordOutput("Command/Drive", RobotContainer.getDriveInstance().getCurrentCommand().toString());
        // Logger.recordOutput("Command/Extender", RobotContainer.getExtenderInstance().getCurrentCommand().toString());
        // Logger.recordOutput("Command/Shooter", RobotContainer.getShooterInstance().getCurrentCommand().toString());
        // Logger.recordOutput("Command/Indexer", RobotContainer.getIndexerInstance().getCurrentCommand().toString());
        // Logger.recordOutput("Command/Intake", RobotContainer.getIntakeInstance().getCurrentCommand().toString());
        Logger.recordOutput("target Pose", new Pose2d(DriveTools.calculateTargetPosition(DriveState.drivePosition,DriveParameter.targetdistanceToShoot),UsecaseUtil.calcurateTargetAngleToShoot(DriveState.drivePosition,DriveState.driveXYOmegaSpeed)));
    }
}
