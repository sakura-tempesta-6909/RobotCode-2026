package frc.robot.domain.repository;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface IntakeRepository extends Subsystem {
/**
     * PersentOutputでモーターを任意の速度で動かす
     * @param targetSpeed intakeを動かすスピード | Fuelを回収する方が正 | [-1~1] | 止まっているときを0
     */
    void moveIntakeSpecifiedSpeed(double targetSpeed);
}
