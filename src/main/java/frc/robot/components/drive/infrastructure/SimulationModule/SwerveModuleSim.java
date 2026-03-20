package frc.robot.components.drive.infrastructure.SimulationModule;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import frc.robot.components.drive.DriveConst;
import frc.robot.components.drive.DriveConst.DriveConstants;
import frc.robot.components.drive.DriveConst.ModuleConstants;
import frc.robot.components.drive.DriveConst.SwerveModuleConst;
import frc.robot.components.drive.DriveParameter;

public class SwerveModuleSim {
    private final DCMotorSim driveMotorSim;
    private final DCMotorSim turningMotorSim;

    private final PIDController turningPidController;

    public SwerveModuleSim(SwerveModuleConst moduleConstant) {
        // シュミレーション用のモーターの設定
        double driveGearRatio = 1.0 / ModuleConstants.kDriveMotorGearRatio;
        driveMotorSim = new DCMotorSim(
            LinearSystemId.createDCMotorSystem(DCMotor.getNEO(1), 0.025, driveGearRatio),
            DCMotor.getNEO(1)
        );
        double turnGearRatio = 1.0 / ModuleConstants.kTurningMotorGearRatio;
        turningMotorSim = new DCMotorSim(
            LinearSystemId.createDCMotorSystem(DCMotor.getNEO(1), 0.004, turnGearRatio),
            DCMotor.getNEO(1)
        );

        turningPidController = new PIDController(DriveParameter.Module.kPTurning, 0, 0);
        turningPidController.enableContinuousInput(-Math.PI, Math.PI);

    }

    public double getDrivePosition() {
        // rad/sをm/sに変換
        return driveMotorSim.getAngularPositionRad() * (ModuleConstants.kWheelDiameterMeters / 2.0);
    }

    public double getTurningPosition() {
        return turningMotorSim.getAngularPositionRad();
    }

    public double getDriveVelocity() {
        // rad/sをm/sに変換
        return driveMotorSim.getAngularVelocityRadPerSec() * (ModuleConstants.kWheelDiameterMeters / 2.0);
    }

    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(getDrivePosition(), new Rotation2d(getTurningPosition()));
    }

    public void resetEncoders() {
        driveMotorSim.setState(0, 0);
        turningMotorSim.setState(0, 0);
    }

    public SwerveModuleState getState() {
        return new SwerveModuleState(getDriveVelocity(), new Rotation2d(getTurningPosition()));
    }

    /**
     * wheelを動かす
     * @param state
     */
    public void setDesiredState(SwerveModuleState state) {
        if (Math.abs(state.speedMetersPerSecond) < 0.001) {
            stop();
            return;
        }

        state.optimize(getState().angle);

        // シュミレーション用のモーターに出力を設定
       double driveOutput = (state.speedMetersPerSecond / DriveConstants.kPhysicalMaxSpeedMetersPerSecond);
       driveMotorSim.setInput(driveOutput * 12.0);

       double turnOutput = turningPidController.calculate(getTurningPosition(), state.angle.getRadians());
       turningMotorSim.setInput(turnOutput * 12.0);

       // シュミレーションを更新する
        driveMotorSim.update(DriveConst.LoopPeriod);
        turningMotorSim.update(DriveConst.LoopPeriod);
    }

    public void stop() {
        driveMotorSim.setInput(0);
        turningMotorSim.setInput(0);
    }
}