package frc.robot.components.example;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface ExampleRepository extends Subsystem{

    /**
     * メッセージを表示する
     * @param message
     */
    void print_message(String message);

    /**
     * PercentOutputでモータを動かす
     * @param output モータ出力 [-1, 1] 正の方向: 上方向, 0 = 停止
     */
    void percentOutput(double output);

    /**
     * PIDのリセット
     */
    void resetPID();

    /**
     * 目標値までexampleを持っていく
     * @param target 目標値 単位: cm, 正の方向: 上方向, 基準: 地面
     */
    void moveToTarget(double target);

    /**
     * 現在位置を維持する
     */
    void keepCurrentPosition();
}
