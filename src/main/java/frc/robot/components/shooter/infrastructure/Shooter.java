package frc.robot.components.shooter.infrastructure;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.domain.repository.ShooterRepository;
import frc.robot.components.shooter.ShooterConst;
import frc.robot.domain.state.ShooterState;

public class Shooter implements ShooterRepository {

    private final SparkMax motor;
    private final SparkClosedLoopController pid;

    private static final int MotorCAN_ID = ShooterConst.Ports.ShooterMotor;

    public Shooter() {
        motor = new SparkMax(MotorCAN_ID, MotorType.kBrushless);

        SparkMaxConfig config = new SparkMaxConfig();

        config.closedLoop.p(0);
        config.closedLoop.i(0);
        config.closedLoop.d(0);
        config.closedLoop.velocityFF(0);

        motor.configure(config, SparkMax.ResetMode.kResetSafeParameters, SparkMax.PersistMode.kPersistParameters);

        pid = motor.getClosedLoopController();
    }

    @Override
    public void moveShooterSpecifedPower(double targetPower) {
        motor.set(targetPower);
    }

    @Override
    public void moveShooterSpecifiedSpeed(double targetSpeed) {
        pid.setReference(targetSpeed, SparkMax.ControlType.kVelocity);
    }

    @Override
    public void resetPID() {
        pid.setReference(0, SparkMax.ControlType.kVelocity);
    }

    /**
     * Stateへの書き込みを行う
     */
    @Override
    public void periodic() {
        double WheelRPM = motor.getEncoder().getVelocity();
        double WheelDiameter = 0.1016;

        //RPMとウィールの直径から表面速度を計算する
        ShooterState.motorSpeed = WheelRPM * WheelDiameter * 3.14 / 60;
    }
}
