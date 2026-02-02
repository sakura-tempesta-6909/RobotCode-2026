package frc.robot.domain.option;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;

public class RobotOption<T>{
    private T defaultValue;
    private T value;
    public RobotOption(T value) {
        this.value = value;
    }

    public void setDefault(T option) {
        this.defaultValue = option;
        this.value = option;
    }

    public Command set(T option) {
        return new StartEndCommand(
            () -> {this.value = option;}, 
            () -> {
                if(defaultValue != null) {
                    this.value = defaultValue;
                }
            }
        );
    }

    public T get() {
        return this.value;
    }
}