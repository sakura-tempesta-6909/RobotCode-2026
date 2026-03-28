package frc.robot.components.extender;

import frc.robot.domain.state.ExtenderState;

public class ExtenderTools {
    /** extenderの角度に対応するモーターの軸の回転数を求める
     * @param targetAngle 目標の回転角度[degree]
     * @return モーターの軸の回転数[rotation]
     */
    public static double getRotationsOfMotorShaft(double targetAngle){
        return ExtenderConst.GearRatio * targetAngle /360;

    }

    /** extenderの累計回転数に対応するextenderの角度を求める
     * @param position 回転数[rotation]
     * @return extenderの回転角度[degree]
     */
    public static double getAngleOfExtender(double position){
        return position / ExtenderConst.GearRatio * 360;
    }
    

}
