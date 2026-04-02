package frc.robot.components.shooter.infrastructure;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import org.littletonrobotics.junction.Logger;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.domain.repository.ShooterRepository;
import frc.robot.components.shooter.ShooterConst;
import frc.robot.domain.state.DriveState;
import frc.robot.domain.state.ShooterState;
import frc.robot.domain.state.StateGroup;
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
        config.closedLoop.d(ShooterParameter.dGain).iZone(ShooterParameter.IZone);
        config.closedLoop.feedForward.kS(ShooterParameter.kSGain);
        config.closedLoop.feedForward.kV(ShooterParameter.kVGain);
        config.closedLoop.allowedClosedLoopError(ShooterTools.mpsToRpm(ShooterParameter.errorToleranceMps),ClosedLoopSlot.kSlot0);
        followerConfig.follow(motor, true);
        config.inverted(true);
        config.smartCurrentLimit(20);

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
     * @param targetMps モーターの表面速度(m/s)
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
        pid.setIAccum(0);
    }

    /**
     * Stateへの書き込みを行う
     */
    @Override
    public void periodic() {

        /** モーター回転数 [RPM] */
        double motorRPM = motor.getEncoder().getVelocity();

        // 現在の表面速度 [m/s] を State に書き込む
        ShooterState.shooterSurfaceSpeedMps = ShooterTools.rpmToSurfaceSpeed(motorRPM);

        /** モーターが動いているか */
        ShooterState.isMotorActive = Math.abs(ShooterState.shooterSurfaceSpeedMps) > ShooterParameter.errorToleranceMps;
        /** 現在の目標値 */
        ShooterState.targetMotorSpeed = ShooterTools.rpmToSurfaceSpeed(pid.getSetpoint());
        /** 目標値に達しているか */
        if (pid.getSetpoint() != ShooterParameter.Neutral) {
            ShooterState.isReadyToShoot = pid.isAtSetpoint();
        } else {
            ShooterState.isReadyToShoot = false;
        }

        /** モーター情報 */
        ShooterState.outputCurrent = motor.getOutputCurrent();  
        ShooterState.appliedOutput = motor.getAppliedOutput();
        ShooterState.busVoltage = motor.getBusVoltage();
        Logger.recordOutput("Shooter/feed", ShooterTools.fuelVelocityToRPM(ShooterTools.distanceToMps(StateGroup.getDistanceToFeedPosition(), DriveState.driveXYOmegaSpeed)));
        Logger.recordOutput("Shooter/shoot", ShooterTools.fuelVelocityToRPM(ShooterTools.distanceToMps(StateGroup.getDistanceToHub(), DriveState.driveXYOmegaSpeed)));
    }
}