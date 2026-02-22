package frc.robot.components.shooter.infrastructure;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.domain.repository.ShooterRepository;
import frc.robot.components.shooter.ShooterConst;
import frc.robot.domain.state.ShooterState;
import frc.robot.components.shooter.ShooterParameter;
import frc.robot.components.shooter.ShooterTools;

public class Shooter implements ShooterRepository {

    private final SparkMax motor;
    private final SparkClosedLoopController pid;

    public Shooter() {
        motor = new SparkMax(ShooterConst.Ports.ShooterMotor, MotorType.kBrushless);

        SparkMaxConfig config = new SparkMaxConfig();

        config.closedLoop.p(ShooterParameter.pGain);
        config.closedLoop.i(ShooterParameter.iGain);
        config.closedLoop.d(ShooterParameter.dGain);
        config.closedLoop.feedForward.kS(ShooterParameter.kSGain);
        config.closedLoop.feedForward.kV(ShooterParameter.kVGain);

        motor.configure(config, SparkMax.ResetMode.kResetSafeParameters, SparkMax.PersistMode.kPersistParameters);

        pid = motor.getClosedLoopController();
    }

    @Override
    /** 
     * PercentOutputでモーターを任意の速度で動かす
     * 範囲：-1～1 （負方向の最大出力割合―1～正方向の最大出力割合1まで）
     * 正回転でシュート
     * 0で停止
     */
    public void moveShooterSpecifedPower(double targetPower) {
        motor.set(targetPower);
    }

    @Override
    /** 
     * Velocity制御でモーターを任意の表面速度m/sで動かす 
     * 範囲：-30.17m/sから30.17m/sまで
     * 正回転でシュート
     * 0で停止
    */
    public void moveShooterSpecifiedSpeed(double targetSpeed) {
        pid.setReference(targetSpeed, SparkMax.ControlType.kVelocity);
    }

    @Override
    /**
     * PIDをリセット
     */
    public void resetPID() {
        pid.setReference(0, SparkMax.ControlType.kVelocity);
    }

    /**
     * Stateへの書き込みを行う
     */
    @Override
    public void periodic() {
        double WheelRPM = motor.getEncoder().getVelocity();

        /** stateに現在の表面速度を書き込む（m/s）*/
        ShooterState.motorSpeed = ShooterTools.rpmToSurfaceSpeed(WheelRPM);
    }
}
