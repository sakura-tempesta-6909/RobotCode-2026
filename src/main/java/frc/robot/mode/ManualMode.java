package frc.robot.mode;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.RobotContainer;
import frc.robot.domain.option.DriveOption;
import frc.robot.domain.option.DriveOption.DriveOriented;
import frc.robot.domain.option.DriveOption.DriveSpeed;
import frc.robot.usecase.commands.CommandsGroup;
import frc.robot.usecase.commands.DriveCommands;
import frc.robot.usecase.commands.ExtenderCommands;
import frc.robot.usecase.commands.IndexerCommands;
import frc.robot.usecase.commands.IntakeCommands;
import frc.robot.usecase.commands.ShooterCommands;
import frc.robot.usecase.commands.LEDCommands;
import frc.robot.domain.repository.DriveRepository;
import frc.robot.domain.repository.ExtenderRepository;
import frc.robot.domain.repository.ShooterRepository;

//エンコーダーやPIDがバグった時ようのモード、すべてPercentOutPutで動かす
public class ManualMode extends Mode {

    static void configureDefault() {
        RobotContainer.getDriveInstance().setDefaultCommand(
            DriveCommands.ManualDrive(
                () -> -driveController.getLeftY(),
                () -> -driveController.getLeftX(),
                () -> -driveController.getRightX()
            )
        );
        
        DriveOption.driveOriented.setDefault(DriveOriented.s_fieldOriented);
        DriveOption.driveSpeed.setDefault(DriveSpeed.s_fastDrive);
        
        RobotContainer.getExtenderInstance().setDefaultCommand(
            ExtenderCommands.stopExtender()
        );

        RobotContainer.getIndexerInstance().setDefaultCommand(
            IndexerCommands.stopIndexer()
        );

        RobotContainer.getIntakeInstance().setDefaultCommand(
            IntakeCommands.stopIntake()
        );

        RobotContainer.getShooterInstance().setDefaultCommand(
            ShooterCommands.stopShooter()
        );

        RobotContainer.getLEDInstance().setDefaultCommand(
            LEDCommands.set()
        );
    }
    
    public static void configureBindings() {
       //コントローラー0: driveController
        configureDefault();
        driveController.rightBumper().whileTrue(DriveOption.driveOriented.set(DriveOriented.s_robotOriented));
        //Hubに位置を合わせる
        driveController.b().whileTrue(DriveCommands.moveToHub());
        //ロボットをHUBに向ける
        driveController.x().whileTrue(DriveCommands.faceToHub(
            () -> -driveController.getLeftY(),
            () -> -driveController.getLeftX()));
        //ロボットを0度に向ける
        driveController.y().onTrue(DriveCommands.setAngle(
            Rotation2d.fromDegrees(0),
            () -> -driveController.getLeftY(),
            () -> -driveController.getLeftX()));
        //ロボットを180度に向ける
        driveController.a().onTrue(DriveCommands.setAngle(
            Rotation2d.fromDegrees(180),
            () -> -driveController.getLeftY(),
            () -> -driveController.getLeftX()));
        //gyroリセット
        driveController.pov(0).onTrue(DriveCommands.resetGyroSensor());
       
       //コントローラー1: operateController
       //HubへShoot
       operateController.rightTrigger(0.6).whileTrue(CommandsGroup.shoot());
       //Intakeを回す
       operateController.leftTrigger(0.6).whileTrue(CommandsGroup.intake());
       //Extenderを上方向に動かす
       operateController.leftBumper().whileTrue(ExtenderCommands.moveExtenderMaxPowerToInitialPosition());
       //Extenderを下方向に動かす
       operateController.rightBumper().whileTrue(ExtenderCommands.moveExtenderMaxPowerToIntakePosition());
       //outtake
       operateController.b().whileTrue(CommandsGroup.outtake());
       //feed
       operateController.a().whileTrue(CommandsGroup.feed());
       //ExtenderのPIDとエンコーダーをリセットする
       operateController.pov(0).onTrue(
        Commands.parallel(
            ExtenderCommands.resetEncoder(),
            ExtenderCommands.resetPID()
            )
       );
       //ShooterのPIDをリセットする
       operateController.pov(180).onTrue(ShooterCommands.resetPID());
    }
}
