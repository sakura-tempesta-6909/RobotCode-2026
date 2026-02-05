package frc.robot.domain.repository;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface LEDRepository extends Subsystem {
    /** LEDを任意の色に変える */
    void changeLight(int red,int green, int blue);
    /** LEDを任意の色で点滅させる */
    void flashLight(int red,int green, int blue);

}
