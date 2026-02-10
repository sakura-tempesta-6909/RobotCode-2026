package frc.robot.domain.repository;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface ExtenderRepository extends Subsystem {
    
    /** Extenderを任意の角度に動かす(Position)  */
    void moveExtenderSpecifiedAngle(double targetAngle);

    /** Extenderを任意の速度で動かす(PercentOutput)  */
    void moveIndexerSpecifiedSpeed(double targetSpeed);

    /** PIDをリセットする */
    void resetPID();

    /** encoderをリセットする */
    void resetEncorder();

}
