package frc.robot.mode;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.RobotContainer;
import frc.robot.domain.option.DriveOption;
import frc.robot.domain.option.DriveOption.DriveOriented;
import frc.robot.domain.option.DriveOption.DriveSpeed;
import frc.robot.usecase.commands.*;

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
        driveController.y().whileTrue(DriveCommands.setAngle(
            () -> Rotation2d.kZero,
            () -> -driveController.getLeftY(),
            () -> -driveController.getLeftX()));
        //ロボットを180度に向ける
        // driveController.a().whileTrue(DriveCommands.setAngle(
        //     () -> Rotation2d.k180deg,
        //     () -> -driveController.getLeftY(),
        //     () -> -driveController.getLeftX()));
        //gyroリセット
        driveController.pov(0).onTrue(DriveCommands.resetGyroSensor());
        //HubへShoot: shootToHub,feedToShooter
        driveController.rightTrigger(0.6).whileTrue(CommandsGroup.shoot());
        //Intake: moveToIntakeAngle,intakeFuel
        driveController.leftTrigger(0.6).whileTrue(CommandsGroup.intake());
        // Extenderをシャカシャカする
        driveController.a().whileTrue(CommandsGroup.keepExtenderPreferedAngle());
        // visionを使うか使わないかを切り替える(デフォルトは使う)
        driveController.pov(180).onTrue(DriveCommands.toggleVisionEnabled());
        
        //コントローラー1: operateController
        //HubへShoot: shootToHub,feedToShooter
        operateController.rightTrigger(0.6).whileTrue(CommandsGroup.shoot());
        //Intake: moveToIntakeAngle,intakeFuel
        operateController.leftTrigger(0.6).whileTrue(CommandsGroup.intake());
        //feed: feed,feedToShooter
        operateController.rightBumper().whileTrue(CommandsGroup.feed());
        //Extenderを初期位置に戻す
        operateController.leftBumper().onTrue(ExtenderCommands.moveToInitialAngle());
        // Extenderをシャカシャカする
        operateController.a().whileTrue(CommandsGroup.keepExtenderPreferedAngle());
        // ロボットのPoseがズレた時用　Shooterを3000RPMで動かす
        operateController.b().whileTrue(CommandsGroup.shoot3000RPM());
    }
}
