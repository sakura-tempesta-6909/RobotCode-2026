package frc.robot.domain.repository;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface IndexerRepository extends Subsystem {
    /**
     * Indexerを任意の速度で動かす(PercentOutput)
     * @param targetSpeed Indexerを動かす速さ | shooterに送る方向が正 | [-1~1](最大速度を基準とした割合です)
     */
    void moveIndexerSpecifiedSpeed(double targetSpeed);
}
