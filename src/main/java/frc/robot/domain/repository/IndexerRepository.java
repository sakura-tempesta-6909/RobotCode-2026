package frc.robot.domain.repository;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface IndexerRepository extends Subsystem {
    /** Indexerを任意の速度で動かす(PercentOutput) */
    void moveIndexerSpecifiedSpeed(double targetSpeed);
}
