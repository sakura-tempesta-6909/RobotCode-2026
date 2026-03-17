// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.components.extender.infrastructure.Extender;
import frc.robot.components.indexer.infrastructure.Indexer;
import frc.robot.components.intake.infrastructure.Intake;
import frc.robot.components.led.infrastructure.LED;
import frc.robot.components.shooter.infrastructure.Shooter;
import frc.robot.components.vision.infrastructure.Vision;
import frc.robot.domain.repository.*;
import frc.robot.mode.Mode;
import frc.robot.usecase.commands.*;
import frc.robot.auto.AutoCommand;
import frc.robot.auto.AutoCommandConfigure;
import frc.robot.components.drive.infrastructure.BasicDrive;
import frc.robot.components.drive.infrastructure.BasicDriveSim;
import frc.robot.components.example.ExampleRepository;
import frc.robot.components.example.infrastructure.Example;
import frc.robot.components.template.infrastructure.Template;
import frc.robot.mode.ClimbMode;
import frc.robot.mode.DriveMode;
import frc.robot.mode.ExampleMode;

public class RobotContainer {
  private static DriveRepository m_drive = RobotBase.isSimulation() ? new BasicDriveSim() : new BasicDrive();
  public static DriveRepository getDriveInstance() {
      return m_drive;
  }

  public static ExampleRepository m_example = new Example();
  public static ExampleRepository getExampleInstance() {
    return m_example;
  }

  private static TemplateRepository m_template = new Template();
  public static TemplateRepository getTemplateInstance() {
      return m_template;
  }

  private static ExtenderRepository m_extender = new Extender();
  public static ExtenderRepository getExtenderInstance(){
    return m_extender;
  }

  private static IndexerRepository m_indexer = new Indexer();
  public  static IndexerRepository getIndexerInstance(){
    return m_indexer;
  }

  private static IntakeRepository m_intake = new Intake();
  public static IntakeRepository getIntakeInstance(){
    return m_intake;
  }

  private static LEDRepository m_led = new LED();
  public static  LEDRepository getLEDInstance(){
    return m_led;
  }

  private static ShooterRepository m_shooter = new Shooter();
  public static ShooterRepository getShooterInstance(){
    return m_shooter;
  }

  private static VisionRepository m_vision = new Vision();
  public static VisionRepository getVisionInstance(){
    return m_vision;
  }

  public RobotContainer() {
    m_drive.buildAuto();
    DriveCommands.init(m_drive);
    ExampleCommands.init(m_example);
    TemplateCommands.init(m_template);
    ExtenderCommands.init(m_extender);
    IndexerCommands.init(m_indexer);
    IntakeCommands.init(m_intake);
    ShooterCommands.init(m_shooter);
    VisionCommands.init(m_vision);

    AutoCommandConfigure.registerCommands();
    AutoCommand.buildAutoChooser();
    
    Mode.setupMode();
    Mode.configureModeBindings();
    mode.configureBindings();
  }

  public static ModeType mode = ModeType.k_drive;

  public enum ModeType {
    k_drive(DriveMode::configureBindings),
    k_example(ExampleMode::configureBindings),
    k_climb(ClimbMode::configureBindings),
    ;

    private final Runnable configureBindings;

    ModeType(Runnable configureBindings) {
        this.configureBindings = configureBindings;
    }

    public void configureBindings() {
        this.configureBindings.run();
    }
  }

  public Command getAutonomousCommand() {
    return AutoCommand.getAutonomousCommand();
  }
}
