package frc.robot.usecase.commands;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.domain.state.ExtenderState;
import frc.robot.domain.state.ShooterState;
import frc.robot.Robot;
import frc.robot.components.drive.DriveTools;
import frc.robot.components.drive.infrastructure.SimulationModule.BasicDriveSim;
import frc.robot.components.shooter.ShooterTools;
import frc.robot.domain.state.DriveState;
import frc.robot.domain.state.StateGroup;

public class CommandsGroup {

    /**
     * ExtenderをIntakeできる角度にした後Fuelを回収する
     * @return ↑をするコマンドを返す
     */
    public static Command intake() {
        return Commands.parallel(
            ExtenderCommands.moveToInitialAngle(),
            IntakeCommands.intakeFuel()
        );
    }

    /**
     * TODO fuelを送るためにExtenderを動かす
     * Shooterをシュートできる速度にした後ShooterとIndexerを動かしてシュートする
     * @return ↑をするコマンドを返す
     */
    public static Command shoot() {
    return Commands.either(
        // --- シミュレーション中の動作 ---
        Commands.repeatingSequence(
            Commands.runOnce(() -> BasicDriveSim.fuelSimulation.launchFuel(ShooterTools.distanceToMps(StateGroup.getDistanceToHub(), DriveState.driveXYOmegaSpeed))),
            Commands.waitSeconds(0.3) // ← ここで間隔調整
            ),

        // --- 実機（RoboRIO）での動作 ---
        Commands.parallel(
            ShooterCommands.shootToHub(),
            IndexerCommands.feedToShooter().onlyWhile(() -> ShooterState.isReadyToShoot)
        ),

        // どっちを使うかの判定条件
        Robot::isSimulation
    );
}

    /**
     * シュートできる位置まで移動する
     * @return ↑をするコマンドを返す
     */
    public static Command alignToHub(){
        return DriveCommands.moveToHub();
    }

    /**
     * シュートできる位置に移動した後シュートする
     * @return ↑をするコマンドを返す
     */
    public static Command alignToHubAndShoot() {
        return Commands.sequence(
            alignToHub().until(() -> DriveState.isShootPosition),
            shoot()
        );
    }

    /**
     * 詰まった時用ににShooter,Indexer,Intakeすべてを逆回転する
     * @return ↑をするコマンドを返す
     */
    public static Command outtake() {
        return Commands.parallel(
          ShooterCommands.reverseShooter(),
          IndexerCommands.reverseIndexer(),
          IntakeCommands.outtakeFuel()  
        );
    }

    /**
     * フィード用の速度でシューターを回しながらIndexerも回してフィードする
     * @return ↑をするコマンドを返す
     */
    public static Command feed() {
        return Commands.parallel(
            ShooterCommands.feed(),
            IndexerCommands.feedToShooter().onlyWhile(() -> ShooterState.isReadyToShoot)
        );
    }


    public static Command score() {
        return new SequentialCommandGroup(
            new ParallelCommandGroup(DriveCommands.FollowGoToGoal(), ExampleCommands.moveToGoal()).withTimeout(10),
            TemplateCommands.templateCommand()
        );
    }

    public static Command readytoScore() {
        return new ConditionalCommand(CommandsGroup.score(), new InstantCommand(), StateGroup::readyToScore);
    }
}