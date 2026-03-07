package frc.robot.mode;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;
import frc.robot.domain.option.DriveOption;
import frc.robot.domain.option.DriveOption.DriveOriented;
import frc.robot.mode.DriveMode;
import frc.robot.usecase.commands.CommandsGroup;
import frc.robot.usecase.commands.DriveCommands;
import frc.robot.usecase.commands.ExtenderCommands;
import frc.robot.usecase.commands.ShooterCommands;
import frc.robot.domain.repository.ExtenderRepository;
import frc.robot.domain.repository.ShooterRepository;

//エンコーダーやPIDがバグった時ようのモード、すべてPercentOutPutで動かす
public class ManualMode extends Mode {
    private static final ExtenderRepository extender = RobotContainer.getExtenderInstance();
    private static final ShooterRepository shooter = RobotContainer.getShooterInstance();

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
            Rotation2d.fromDegrees(0),
            () -> -driveController.getLeftY(),
            () -> -driveController.getLeftX()));
        //ロボットを180度に向ける
        driveController.a().whileTrue(DriveCommands.setAngle(
            Rotation2d.fromDegrees(180),
            () -> -driveController.getLeftY(),
            () -> -driveController.getLeftX()));
        //gyroリセット
        driveController.pov(0).onTrue(DriveCommands.resetGyroSensor());
       
       //コントローラー1: operateController
       //HubへShoot
       operateController.rightTrigger().whileTrue(CommandsGroup.shoot());
       //Intakeを回す
       operateController.leftTrigger().whileTrue(CommandsGroup.intake());
       //Extenderを上方向に動かす
       operateController.leftBumper().whileTrue(
        new InstantCommand(() -> extender.moveIndexerSpecifiedPower(-0.5)));
       operateController.leftBumper().onFalse(
        new InstantCommand(() -> extender.moveIndexerSpecifiedPower(0)));
       //Extenderを下方向に動かす
       operateController.rightBumper().whileTrue(
        new InstantCommand(() -> extender.moveIndexerSpecifiedPower(0.5)));
       operateController.rightBumper().onFalse(
        new InstantCommand(() -> extender.moveIndexerSpecifiedPower(0)));
       //outtake
       operateController.b().whileTrue(CommandsGroup.outtake());
       //feed
       operateController.b().whileTrue(CommandsGroup.feed());
       //ExtenderのPIDとエンコーダーをリセットする
       operateController.pov(0).onTrue(new InstantCommand(() -> {
        extender.resetPID();
        extender.resetEncorder(); }));
       //ShooterのPIDをリセットする
       operateController.pov(180).onTrue(new InstantCommand(() -> {
        shooter.resetPID(); })); 
    }
}
