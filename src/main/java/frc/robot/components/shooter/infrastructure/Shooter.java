package frc.robot.components.shooter.infrastructure;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.domain.repository.ShooterRepository;
import frc.robot.components.shooter.ShooterConst;

public class Shooter implements ShooterRepository {

    private final SparkMax motor;
    private final SparkClosedLoopController pid;

    private static final int MotorCAN_ID = ShooterConst.Ports.ShooterMotor;

    public Shooter() {
        motor = new SparkMax(MotorCAN_ID, MotorType.kBrushless);

        SparkMaxConfig config = new SparkMaxConfig();

        config.closedLoop
            .p(0.0002)
            .i(0.0)
            .d(0.0)
            .velocityFF(0.00018);

        motor.configure(config, SparkMax.ResetMode.kResetSafeParameters,
                               SparkMax.PersistMode.kPersistParameters);

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
        // 新APIではI積分リセットはconfig経由などになる
    }

    /**
     * Stateへの書き込みを行う
     */
    @Override
    public void periodic() {
    }
}
