package frc.robot.domain.repository;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface ExtenderRepository extends Subsystem {
    
    /** Extenderを任意の角度に動かす(Position) |targetAngle:Extenderが地面に対して並行な時を0とした目標の角度[degree]|地面に対して上に動かす方向を正 */
    void moveExtenderSpecifiedAngle(double targetAngle);

    /** Extenderを任意の速度で動かす(PercentOutput) |targetspeed: 止まっている時を0とした目標のスピード(％[-1~1])|地面に対して上に動かす方向を正 */
    void moveIndexerSpecifiedSpeed(double targetSpeed);

    /** PIDをリセットする */
    void resetPID();

    /** encoderをリセットする */
    void resetEncorder();

}
