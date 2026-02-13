package frc.robot.domain.repository;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface IndexerRepository extends Subsystem {
    /**
     * Indexerを任意のパワーで動かす(PercentOutput)
     * @param targetPower Indexerを動かすパワー | shooterに送る方向が正 | [-1~1]((出力のパワーの割合)
     */
    void moveIndexerSpecifiedPower(double targetPower);
}
