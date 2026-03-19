package frc.robot.components.drive.infrastructure;


import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.components.drive.DriveConst.DriveConstants;
import frc.robot.components.drive.DriveConst.SwerveModuleConst;

public class SwerveModule {
    private final SparkMax driveMotor;
    private final SparkMax turningMotor;

    private final RelativeEncoder driveEncoder;
    private final RelativeEncoder turningEncoder;

    private final CANcoder absoluteEncoder;

    SparkClosedLoopController drivePidController,turningPidController;    

    public SwerveModule(SwerveModuleConst moduleConstant){
        absoluteEncoder = new CANcoder(moduleConstant.encoderID);

        driveMotor = new SparkMax(moduleConstant.driveMotorID, MotorType.kBrushless);
        turningMotor = new SparkMax(moduleConstant.turningMotorID, MotorType.kBrushless);

        driveMotor.configure(moduleConstant.driveConfig, ResetMode.kResetSafeParameters , PersistMode.kPersistParameters);
        turningMotor.configure(moduleConstant.turningConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        driveEncoder = driveMotor.getEncoder();
        turningEncoder = turningMotor.getEncoder();

        drivePidController = driveMotor.getClosedLoopController();
        turningPidController = turningMotor.getClosedLoopController();

        // すぐリセットすると値がブレる可能性があるため
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            resetEncoders();
        });
        thread.start();
        
    }

    public double getDrivePosition(){
        return driveEncoder.getPosition();
    }

    public double getTurningPosition(){
        return turningEncoder.getPosition();
    }

    public double getDriveVelocity(){
        return driveEncoder.getVelocity();
    }

    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(getDrivePosition(), new Rotation2d(getTurningPosition()));
    }

    public double getAbsoluteEncoderRad(){
        return absoluteEncoder.getAbsolutePosition().getValueAsDouble() * 2 * Math.PI;
    }

    public void resetEncoders(){
        driveEncoder.setPosition(0);
        turningEncoder.setPosition(getAbsoluteEncoderRad());
    }

    public SwerveModuleState getState(){
        return new SwerveModuleState(getDriveVelocity(), new Rotation2d(getTurningPosition()));
    }
    
    /**
     * wheelを動かす
     * @param state
     */
    public void setDesiredState(SwerveModuleState state){
        if(Math.abs(state.speedMetersPerSecond) < 0.001){
           stop();
            return;
        }

        state.optimize(getState().angle);
        drivePidController.setSetpoint(state.speedMetersPerSecond, SparkMax.ControlType.kVelocity);
 
        double setPoint = state.angle.getDegrees();
        double currentAngle = Math.toDegrees(turningEncoder.getPosition()); 
        
        setPoint = ((setPoint % 360) + 360) % 360; // setPoint を 0～359 に正規化
        setPoint += Math.floor(currentAngle / 360) * 360; // 現在の角度と合わせて調整
        
        if (currentAngle - setPoint > 180) {
            setPoint += 360;
        } else if (setPoint - currentAngle > 180) {
            setPoint -= 360;
        }
        
        turningPidController.setSetpoint(Math.toRadians(setPoint), SparkMax.ControlType.kPosition);
    }
    
    public void stop() {
        driveMotor.set(0);
        // turningMotor.set(0);
    }

    public void runCharacterization(double output){
        driveMotor.setVoltage(output);
        turningMotor.set(0);
    }

}

