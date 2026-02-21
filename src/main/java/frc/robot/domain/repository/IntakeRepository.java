package frc.robot.domain.repository;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface IntakeRepository extends Subsystem {
    /**
     * PersentOutputでモーターを任意の速度で動かす
     * 範囲: -1~1 (負方向の最大出力割合―1～正方向の最大出力割合1まで）
     * 止まっているときを0
     * Fuelを回収する方向を正
     */
    void moveIntakeSpecifiedSpeed(double targetSpeed);
}
