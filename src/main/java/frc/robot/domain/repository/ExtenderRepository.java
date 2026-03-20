package frc.robot.domain.repository;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface ExtenderRepository extends Subsystem {
    
    /** Extenderを任意の角度に動かす(Position) |targetAngle:Extenderが地面に対して並行な時を0とした目標の角度[degree]|地面に対して上に動かす方向を正 
     * @param targetAngle 目標の角度[degree]
    */
    void moveExtenderSpecifiedAngle(double targetAngle);

    /** Extenderを任意の力で動かす(PercentOutput) 
     * @param targetPower : 止まっている時を0とした力(％[-1~1])|地面に対して上に動かす方向を正 */
    void moveExtenderSpecifiedPower(double targetPower);

    /** PIDをリセットする */
    void resetPID();

    /** encoderをリセットする 
     * @param resetPosition Encorderをリセットする際の角度[degree]
     * 初期位置を90°とする
    */
    void resetEncoder(double resetPosition);

    /** 現在の角度を維持する */
    void keepCurrentAngle();

}