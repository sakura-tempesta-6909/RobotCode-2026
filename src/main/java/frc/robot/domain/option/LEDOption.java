package frc.robot.domain.option;

public class LEDOption {
    public enum LEDStateOption {
        s_readyToShoot,
        s_shootablePosition,
        s_shootableSpeed,
        s_readyToIntake,
        s_disable
    }
   public static final RobotOption<LEDStateOption> ledStateOption = new RobotOption<LEDOption.LEDStateOption>(LEDStateOption.s_disable);
    
}
