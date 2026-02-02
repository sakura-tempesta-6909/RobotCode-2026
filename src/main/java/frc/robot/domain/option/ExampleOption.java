package frc.robot.domain.option;

public class ExampleOption {
    public enum SpeedOption {
        k_fast,
        k_mid,
    }
    public static final RobotOption<SpeedOption> speedOption = new RobotOption<ExampleOption.SpeedOption>(SpeedOption.k_fast);

}
