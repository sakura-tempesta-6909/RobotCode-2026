package frc.robot.components.example.infrastructure;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.components.example.ExampleConst;
import frc.robot.components.example.ExampleRepository;
import frc.robot.components.example.ExampleTools;
import frc.robot.domain.repository.ExampleParameter;
import frc.robot.domain.state.ExampleState;

public class Example implements ExampleRepository {

    private final SparkMax exampleMotor;
    private final SparkClosedLoopController closedLoopController;
    private final RelativeEncoder encoder;

    public Example() {
        exampleMotor = new SparkMax(ExampleConst.Ports.exampleMotorPort, MotorType.kBrushless);
        closedLoopController = exampleMotor.getClosedLoopController();
        encoder = exampleMotor.getEncoder();

        SparkMaxConfig config = new SparkMaxConfig();
        config.closedLoop
        .p(ExampleParameter.PIDGains.positionP)
        .i(ExampleParameter.PIDGains.positionI)
        .d(ExampleParameter.PIDGains.positionD);

        exampleMotor.configure(config,  ResetMode.kResetSafeParameters , PersistMode.kPersistParameters);
    }

    @Override
    public void print_message(String message) {
            System.out.println(message);
    }


    @Override
    public void percentOutput(double output) {
        exampleMotor.set(output);
    }

    @Override
    public void resetPID() {
        closedLoopController.setIAccum(0);
    }

    @Override
    public void moveToTarget(double target) {
        // こういうときは例外的にstate更新 
        ExampleState.targetPosition = target;
        closedLoopController.setSetpoint(ExampleTools.positionToEncoderValue(target), ControlType.kPosition);
    }

    @Override
    public void keepCurrentPosition() {
        // あえて実装しない
    }

    /**
     * Stateへの書き込みを行う
     */
    @Override
    public void periodic() {
        ExampleState.currentPosition = ExampleTools.encoderValueToPosition(encoder.getPosition());

        ExampleState.atTarget = closedLoopController.isAtSetpoint();
    }

}
