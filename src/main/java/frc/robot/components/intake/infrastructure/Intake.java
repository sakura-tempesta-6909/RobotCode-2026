package frc.robot.components.intake.infrastructure;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.domain.repository.IntakeRepository;
import frc.robot.components.intake.IntakeConst;
import frc.robot.domain.state.IntakeState;

public class Intake implements IntakeRepository {
    private final SparkMax IntakeMotor;
    private final SparkMaxConfig IntakeMotorConfig;
    private final RelativeEncoder IntakeEncoder;

    public Intake() {
        IntakeMotor = new SparkMax(IntakeConst.Ports.intakeMotor, MotorType.kBrushless);
        IntakeMotorConfig = new SparkMaxConfig();
        IntakeEncoder = IntakeMotor.getEncoder();

        IntakeMotorConfig.inverted(false);
        IntakeMotorConfig.idleMode(IdleMode.kCoast);
        IntakeMotorConfig.smartCurrentLimit(40);

        IntakeMotor.configure(IntakeMotorConfig,ResetMode.kNoResetSafeParameters,PersistMode.kNoPersistParameters);
    }
    
    /**
     * Intakeを任意のスピードで動かす(PercentOutPut)
     * @param targetSpeed Intakeを動かすスピード | Fuelを回収する方向が正 | [-1~1] | 止まっているときを0
     */
    @Override
    public void moveIntakeSpecifiedSpeed(double targetSpeed){
        IntakeMotor.set(targetSpeed);
    }
    
    /**
     * Stateへの書き込みを行う
     */
    @Override
    public void periodic() {
        IntakeState.motorSpeed = IntakeEncoder.getVelocity() / IntakeConst.maxRPM;
        IntakeState.isMotorActive = Math.abs(IntakeEncoder.getVelocity()) > IntakeConst.Threshold;

        // モーター情報
        IntakeState.outputCurrent = IntakeMotor.getOutputCurrent();
        IntakeState.appliedOutput = IntakeMotor.getAppliedOutput();
        IntakeState.busVoltage = IntakeMotor.getBusVoltage();
    }

}
