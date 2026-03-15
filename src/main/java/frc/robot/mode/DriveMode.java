package frc.robot.mode;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.RobotContainer;
import frc.robot.components.extender.infrastructure.Extender;
import frc.robot.domain.option.DriveOption;
import frc.robot.domain.option.DriveOption.DriveOriented;
import frc.robot.domain.option.DriveOption.DriveSpeed;
import frc.robot.usecase.commands.DriveCommands;
import frc.robot.usecase.commands.IndexerCommands;
import frc.robot.usecase.commands.IntakeCommands;
import frc.robot.usecase.commands.ExtenderCommands;
import frc.robot.usecase.commands.ShooterCommands;
import frc.robot.usecase.commands.CommandsGroup;

public class DriveMode extends Mode {
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
    }

    public static void configureBindings() {
        //コントローラー0: driveController
        configureDefault();
        driveController.rightBumper().whileTrue(DriveOption.driveOriented.set(DriveOriented.s_robotOriented));
        //Hubに位置を合わせる
        driveController.b().whileTrue(DriveCommands.moveToHub());
        //ロボットをHUBに向ける
        driveController.x().whileTrue(DriveCommands.faceToHub(
            () -> 0,
            () -> 0));
        //ロボットを0度に向ける
        driveController.y().whileTrue(DriveCommands.setAngle(
            Rotation2d.fromDegrees(0),
            () -> 0,
            () -> 0));
        //ロボットを180度に向ける
        driveController.a().whileTrue(DriveCommands.setAngle(
            Rotation2d.fromDegrees(180),
            () -> 0,
            () -> 0));
        //gyroリセット
        driveController.pov(0).onTrue(DriveCommands.resetGyroSensor());

        //コントローラー1: operateController
        //HubへShoot: shootToHub,feedToShooter
        operateController.rightTrigger(0.6).whileTrue(CommandsGroup.shoot());
        //Intake: moveToIntakeAngle,intakeFuel
        operateController.leftTrigger(0.6).whileTrue(CommandsGroup.intake());
        //feed: feed,feedToShooter
        operateController.rightBumper().whileTrue(CommandsGroup.feed());
        //Extenderを初期位置に戻す
        operateController.leftBumper().onTrue(ExtenderCommands.moveToInitialAngle());
    }
}
