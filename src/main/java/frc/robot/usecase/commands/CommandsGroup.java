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
import frc.robot.components.shooter.ShooterParameter;
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
            ExtenderCommands.moveToIntakeAngle(),
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
            Commands.waitUntil(() -> ShooterState.isReadyToShoot)
                .andThen(IndexerCommands.feedToShooter())
            ),

        // どっちを使うかの判定条件
        Robot::isSimulation
    );
}
    /**
     * Poseが事故ったときよう　3000RPM固定で打つ
     * @return ↑をするコマンドを返す
     */
    public static Command shoot3500RPM() {
        return Commands.parallel(
            ShooterCommands.moveShooterSpecifiedSpeed(() -> ShooterTools.rpmToSurfaceSpeed(ShooterParameter.StrongRPM)),
            IndexerCommands.feedToShooter()
        );
    }

    public static Command shoot3000RPM() {
        return Commands.parallel(
            ShooterCommands.moveShooterSpecifiedSpeed(() -> ShooterTools.rpmToSurfaceSpeed(ShooterParameter.WeekRPM)),
            IndexerCommands.feedToShooter()
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
            alignToHub(),
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

    public static Command intakePreload() {
        return Commands.sequence(
            ExtenderCommands.moveToIntakeAngle().until(() -> ExtenderState.isIntakePosition),
            (new ParallelCommandGroup(
            IntakeCommands.intakeFuel(),
            ShooterCommands.reverseShooter(),
            IndexerCommands.reverseIndexer()                
            )
        ));
    }


    public static Command score() {
        return new SequentialCommandGroup(
            new ParallelCommandGroup(DriveCommands.FollowGoToGoal(), ExampleCommands.moveToGoal()).withTimeout(10),
            TemplateCommands.templateCommand()
        );
    }

    public static Command shakeExtender(){
        return Commands.parallel(
            IntakeCommands.intakeFuel(),
            ExtenderCommands.shakeExtender()
        );
    }

    public static Command keepExtenderPreferedAngle(){
        return Commands.parallel(
            IntakeCommands.intakeFuel(),
            ExtenderCommands.keeppreferedAngle()
        );
    }

    public static Command readytoScore() {
        return new ConditionalCommand(CommandsGroup.score(), new InstantCommand(), StateGroup::readyToScore);
    }
}