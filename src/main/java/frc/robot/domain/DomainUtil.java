package frc.robot.domain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.domain.option.DriveOption;
import frc.robot.domain.state.*;
import org.littletonrobotics.junction.Logger;

public class DomainUtil {
    public static void allSendConsole() {
        SmartDashboard.putNumber("exp c pos", ExampleState.currentPosition);
        SmartDashboard.putString("Drive/Speed", DriveOption.driveSpeed.toString());
        SmartDashboard.putNumber("Extender/CurrentAngle", ExtenderState.currentAngle);
        SmartDashboard.putBoolean("Extender/isIntakeAngle", ExtenderState.isIntakePosition);
        SmartDashboard.putBoolean("Extender/isInitialAngle", ExtenderState.isInitialPosition);
        SmartDashboard.putNumber("Shooter/CurrentSpeed", ShooterState.shooterSurfaceSpeedMps);
        SmartDashboard.putBoolean("Shooter/ReadyToShoot", ShooterState.isReadyToShoot);
        SmartDashboard.putNumber("Shooter/TargetSpeed", ShooterState.targetMotorSpeed);
        SmartDashboard.putNumber("Drive/DistanceToHub", StateGroup.getDistanceToHub());


        // Drive
        Logger.recordOutput("Drive/DistanceToHub", StateGroup.getDistanceToHub());
        Logger.recordOutput("Drive/DistanceToFeedPotiion", StateGroup.getDistanceToFeedPosition());
        Logger.recordOutput("Drive/ChassisSpeed", DriveState.driveXYOmegaSpeed);
        Logger.recordOutput("Drive/CombinedPose", DriveState.drivePosition);
        Logger.recordOutput("Drive/Speed", DriveOption.driveSpeed.toString());
        Logger.recordOutput("Drive/ChassisSpeed", DriveState.driveXYOmegaSpeed);
        Logger.recordOutput("Drive/CombinedPose", DriveState.drivePosition);
        Logger.recordOutput("Drive/Heading", DriveState.heading);
        String[] moduleNames = {"FL", "FR", "BL", "BR"};
        for (int i = 0; i < 4; i++) {
            Logger.recordOutput("Drive/" + moduleNames[i] + "/Drive/busVoltage", DriveState.SwerveMotors.driveBusVoltage[i]);
            Logger.recordOutput("Drive/" + moduleNames[i] + "/Drive/outputCurrent", DriveState.SwerveMotors.driveOutputCurrent[i]);
            Logger.recordOutput("Drive/" + moduleNames[i] + "/Drive/appliedOutput", DriveState.SwerveMotors.driveAppliedOutput[i]);
            Logger.recordOutput("Drive/" + moduleNames[i] + "/Turning/busVoltage", DriveState.SwerveMotors.turningBusVoltage[i]);
            Logger.recordOutput("Drive/" + moduleNames[i] + "/Turning/outputCurrent", DriveState.SwerveMotors.turningOutputCurrent[i]);
            Logger.recordOutput("Drive/" + moduleNames[i] + "/Turning/appliedOutput", DriveState.SwerveMotors.turningAppliedOutput[i]);
        }
        Logger.recordOutput("Drive/ModuleState", DriveState.swerveModuleState);

        // Extender
        Logger.recordOutput("Extender/CurrentAngle", ExtenderState.currentAngle);
        Logger.recordOutput("Extender/isIntakeAngle", ExtenderState.isIntakePosition);
        Logger.recordOutput("Extender/isInitialAngle", ExtenderState.isInitialPosition);
        Logger.recordOutput("Extender/busVoltage", ExtenderState.busVoltage);
        Logger.recordOutput("Extender/outputCurrent", ExtenderState.outputCurrent);
        Logger.recordOutput("Extender/appliedOutput", ExtenderState.appliedOutput);

        // Shooter
        Logger.recordOutput("Shooter/CurrentSpeed", ShooterState.shooterSurfaceSpeedMps);
        Logger.recordOutput("Shooter/ReadyToShoot", ShooterState.isReadyToShoot);
        Logger.recordOutput("Shooter/TargetSpeed", ShooterState.targetMotorSpeed);
        Logger.recordOutput("Shooter/busVoltage", ShooterState.busVoltage);
        Logger.recordOutput("Shooter/outputCurrent", ShooterState.outputCurrent);
        Logger.recordOutput("Shooter/appliedOutput", ShooterState.appliedOutput);

        // Intake 
        Logger.recordOutput("Intake/busVoltage", IntakeState.busVoltage);
        Logger.recordOutput("Intake/outputCurrent", IntakeState.outputCurrent);
        Logger.recordOutput("Intake/appliedOutput", IntakeState.appliedOutput);

        // Indexer LongRoller 
        Logger.recordOutput("Indexer/LongRoller/busVoltage", IndexerState.LongRollerIndexer.busVoltage);
        Logger.recordOutput("Indexer/LongRoller/outputCurrent", IndexerState.LongRollerIndexer.outputCurrent);
        Logger.recordOutput("Indexer/LongRoller/appliedOutput", IndexerState.LongRollerIndexer.appliedOutput);

        // Indexer StarWheel 
        Logger.recordOutput("Indexer/StarWheel/busVoltage", IndexerState.StarWheelIndexer.busVoltage);
        Logger.recordOutput("Indexer/StarWheel/outputCurrent", IndexerState.StarWheelIndexer.outputCurrent);
        Logger.recordOutput("Indexer/StarWheel/appliedOutput", IndexerState.StarWheelIndexer.appliedOutput);

    }
}
