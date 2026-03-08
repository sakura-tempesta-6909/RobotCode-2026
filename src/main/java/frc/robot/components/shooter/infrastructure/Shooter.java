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
    private final SparkMax followerMotor;
    private final SparkClosedLoopController pid;

    public Shooter() {
        motor = new SparkMax(ShooterConst.Ports.ShooterMotor, MotorType.kBrushless);
        followerMotor = new SparkMax(ShooterConst.Ports.ShooterFollowerMotor, MotorType.kBrushless);

        SparkMaxConfig config = new SparkMaxConfig();
        SparkMaxConfig followerConfig = new SparkMaxConfig();

        config.closedLoop.p(ShooterParameter.pGain);
        config.closedLoop.i(ShooterParameter.iGain);
        config.closedLoop.d(ShooterParameter.dGain);
        config.closedLoop.feedForward.kS(ShooterParameter.kSGain);
        config.closedLoop.feedForward.kV(ShooterParameter.kVGain);
        followerConfig.follow(motor);

        motor.configure(config, SparkMax.ResetMode.kResetSafeParameters, SparkMax.PersistMode.kPersistParameters);
        followerMotor.configure(followerConfig, SparkMax.ResetMode.kResetSafeParameters, SparkMax.PersistMode.kPersistParameters);

        pid = motor.getClosedLoopController();
    
    }

    @Override
    /** 
     * PercentOutputでモーターを任意の速度で動かす
     * @param targetPower 出力割合 [-1.0 - 1.0]
     * 正方向でシュート方向
     * 0で停止
     */
    public void moveShooterSpecifiedPower(double targetPower) {
        motor.set(targetPower);
    }

    @Override
    /** 
     * Velocity制御でモーターを任意の表面速度で動かす 
     * @param targetMps モーターの表面速度
     * 正方向でシュート方向
     * 0で停止
    */
    public void moveShooterSpecifiedSpeed(double targetMps) {
        pid.setReference(ShooterTools.mpsToRpm(targetMps), SparkMax.ControlType.kVelocity);
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

        /** モーター回転数 [RPM] */
        double motorRPM = motor.getEncoder().getVelocity();

        /** stateに現在の表面速度を書き込む（m/s）*/
        ShooterState.shooterSurfaceSpeedMps = ShooterTools.rpmToSurfaceSpeed(motorRPM);
    }
}
