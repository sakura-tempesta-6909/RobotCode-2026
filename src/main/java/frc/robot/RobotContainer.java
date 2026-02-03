// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.mode.Mode;
import frc.robot.usecase.commands.DriveCommands;
import frc.robot.usecase.commands.ExampleCommands;
import frc.robot.usecase.commands.TemplateCommands;
import frc.robot.auto.AutoCommand;
import frc.robot.auto.AutoCommandConfigure;
import frc.robot.components.drive.infrastructure.BasicDrive;
import frc.robot.components.example.ExampleRepository;
import frc.robot.components.example.infrastructure.Example;
import frc.robot.components.template.infrastructure.Template;
import frc.robot.domain.repository.DriveRepository;
import frc.robot.domain.repository.TemplateRepository;
import frc.robot.mode.ClimbMode;
import frc.robot.mode.DriveMode;
import frc.robot.mode.ExampleMode;

public class RobotContainer {
  private static DriveRepository m_drive = new BasicDrive();
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

  public RobotContainer() {
    m_drive.buildAuto();
    DriveCommands.init(m_drive);
    ExampleCommands.init(m_example);
    TemplateCommands.init(m_template);

    AutoCommandConfigure.registerCommands();
    
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
