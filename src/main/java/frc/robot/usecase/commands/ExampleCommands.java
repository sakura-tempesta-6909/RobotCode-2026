package frc.robot.usecase.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.components.example.ExampleConst;
import frc.robot.components.example.ExampleRepository;
import frc.robot.components.example.ExampleTools;
import frc.robot.domain.option.ExampleOption;
import frc.robot.domain.repository.ExampleParameter;
import frc.robot.domain.state.Example2State;
import frc.robot.domain.state.ExampleState;

public class ExampleCommands{
    private static ExampleRepository exampleRepository;

    public static void init(ExampleRepository tr) {
        exampleRepository = tr;
    }

    /**
     * RunCommandは割込・強制終了まで永遠に処理をし続ける。
     * つまり、終了次第、次の瞬間にまた処理が再開。
     * 
     * ただ、 {@link Command#withTimeout(double)} や {@link Command#until(BooleanSupplier)} を後につけて終了時間・条件の設定もできる
     */
    public static Command permanently_print_intake_message(DoubleSupplier supplier) {
        return exampleRepository.run(() -> {
            switch (ExampleOption.speedOption.get()) {
                case k_fast:
                    exampleRepository.percentOutput(supplier.getAsDouble() * ExampleParameter.Speed.fastSpeed);
                    break;
                case k_mid:
                default:
                    exampleRepository.percentOutput(supplier.getAsDouble() * ExampleParameter.Speed.midSpeed);
                    break;
            }
        });
    }

    /**
     * Commands.startRunは初期化処理がついたRunCommandで、割込・強制終了まで永遠に処理をし続ける。
     * つまり、終了次第、次の瞬間にまた処理が再開。
     * 
     * PIDなど初期化処理が必要なものに便利
     */
    public static Command moveToTarget(DoubleSupplier targetSupplier) {
        return exampleRepository.startRun(
            exampleRepository::resetPID, 
            () -> exampleRepository.moveToTarget(targetSupplier.getAsDouble())).until(() -> ExampleState.atTarget);
    }

    /**
     * こういう書き方でシンプルに出来る。
     * 少しわかりやすく書くと、
     * public static Command moveToTargetBasedOnApriltag() {
     *      Command command = moveToTarget2Command(() -> {
     *          double apriltagPosition = Example2State.apriltagPosition
     *          double examplePosition = ExampleTools.apriltagPositionToExamplePosition(apriltagPosition);
     *          return examplePosition
     *     });
     *     return command;  
     * }
     */
    public static Command moveToTargetBasedOnApriltag() {
        return moveToTarget(() -> ExampleTools.apriltagPositionToExamplePosition(Example2State.apriltagPosition));
    }

    public static Command moveToGoal() {
        return moveToTarget(() -> ExampleConst.Goals.goal);
    }

    /**
     * 引数もないシンプルな場合はこのように簡略化して書くことも出来ます
     * @return
     */
    public static Command keepCurrentPosition() {
        return exampleRepository.run(exampleRepository::keepCurrentPosition);
    }

    /** コマンドが発生した瞬間の位置 */
    public static double initialPosition;
    /**
     * FunctionalCommandは、初期化、メイン処理、終了時処理、終了条件を全部指定できます。
     * これを使えば、10cm上げるコマンドなんかも簡単に書けたりします。
     * @return
     */
    public static Command move10cmUp() {
        return new FunctionalCommand(() -> {
            initialPosition = ExampleState.currentPosition;
        }, 
        () -> {
            exampleRepository.moveToTarget(initialPosition + 10);
        },
        (interrupted) -> {
            // 終了時は何もしない
        }, 
        () -> ExampleState.atTarget, 
        exampleRepository);
    }

    /*
     * こういうSequentialCommandGroupとかいうものを使えば、10cm上げた後にその位置をキープとかも書けたりします
     */
    public static Command move10cmUpAndKeep() {
        return new SequentialCommandGroup(
            move10cmUp(),
            keepCurrentPosition()
        );
    }
}
