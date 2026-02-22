package frc.robot.components.shooter;

public class ShooterTools {

        /** stateに現在の表面速度を書き込む（m/s）*/
        public static double rpmToSurfaceSpeed(double Wheelrpm){
        return Wheelrpm * ShooterConst.WheelDiameter * 3.14 / 60.0;
    }
}