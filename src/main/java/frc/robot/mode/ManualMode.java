package frc.robot.mode;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.RobotContainer;
import frc.robot.domain.option.DriveOption;
import frc.robot.domain.option.DriveOption.DriveOriented;
import frc.robot.mode.DriveMode;
import frc.robot.usecase.commands.CommandsGroup;
import frc.robot.usecase.commands.DriveCommands;
import frc.robot.usecase.commands.ExtenderCommands;
import frc.robot.usecase.commands.ShooterCommands;
import frc.robot.domain.repository.DriveRepository;
import frc.robot.domain.repository.ExtenderRepository;
import frc.robot.domain.repository.ShooterRepository;

//エンコーダーやPIDがバグった時ようのモード、すべてPercentOutPutで動かす
public class ManualMode extends Mode {
    private static final ExtenderRepository extenderRepository = RobotContainer.getExtenderInstance();
    private static final ShooterRepository shooterRepository = RobotContainer.getShooterInstance();

    public static void configureBindings() {
       //コントローラー0: driveController
        DriveMode.configureDefault();
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
       operateController.leftBumper().whileTrue(
        new RunCommand(() -> extenderRepository.moveIndexerSpecifiedPower(-0.5)));
       //Extenderを下方向に動かす
       operateController.rightBumper().whileTrue(
        new RunCommand(() -> extenderRepository.moveIndexerSpecifiedPower(0.5)));
       //outtake
       operateController.b().whileTrue(CommandsGroup.outtake());
       //feed
       operateController.a().whileTrue(CommandsGroup.feed());
       //ExtenderのPIDとエンコーダーをリセットする
       operateController.pov(0).onTrue(new InstantCommand(() -> {
        extenderRepository.resetEncorder(90);
        extenderRepository.resetPID();}));
       //ShooterのPIDをリセットする
       operateController.pov(180).onTrue(new InstantCommand(shooterRepository::resetPID));
    }
}
