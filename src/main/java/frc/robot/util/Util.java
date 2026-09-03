package frc.robot.util;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.components.drive.DriveParameter;
import frc.robot.components.drive.DriveTools;
import frc.robot.domain.DomainUtil;
import frc.robot.domain.state.DriveState;
import frc.robot.domain.state.ExtenderState;
import frc.robot.usecase.UsecaseUtil;
import frc.robot.usecase.commands.DriveCommands;
import org.littletonrobotics.junction.Logger;

public class Util {
    static SlewRateLimiter limiter = new SlewRateLimiter(0.6);
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
        SmartDashboard.putString("Command/Drive", getCommandName(RobotContainer.getDriveInstance().getCurrentCommand()));
        SmartDashboard.putString("Command/Extender", getCommandName(RobotContainer.getExtenderInstance().getCurrentCommand()));
        SmartDashboard.putString("Command/Shooter", getCommandName(RobotContainer.getShooterInstance().getCurrentCommand()));
        SmartDashboard.putString("Command/Indexer", getCommandName(RobotContainer.getIndexerInstance().getCurrentCommand()));
        SmartDashboard.putString("Command/Intake", getCommandName(RobotContainer.getIntakeInstance().getCurrentCommand()));

        Logger.recordOutput("Command/Drive", getCommandName(RobotContainer.getDriveInstance().getCurrentCommand()));
        Logger.recordOutput("Command/Extender", getCommandName(RobotContainer.getExtenderInstance().getCurrentCommand()));
        Logger.recordOutput("Command/Shooter", getCommandName(RobotContainer.getShooterInstance().getCurrentCommand()));
        Logger.recordOutput("Command/Indexer", getCommandName(RobotContainer.getIndexerInstance().getCurrentCommand()));
        Logger.recordOutput("Command/Intake", getCommandName(RobotContainer.getIntakeInstance().getCurrentCommand()));
    }

    /**
     * 今実行されているCommand名を取得する(ぬるぽ対策)
     * @param command 名前を取得したいCommand
     * @return 実行中のCommand名。実行されていないときは"None"を返す
     */
    public static String getCommandName(Command command) {
        return command != null ? command.getName() : "None";
    }
}
