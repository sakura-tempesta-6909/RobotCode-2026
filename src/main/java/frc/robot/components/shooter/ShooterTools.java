package frc.robot.components.shooter;

public class ShooterTools {

        public static double rpmToSurfaceSpeed(double Wheelrpm, double WheelDiameterMeter){
        return Wheelrpm * WheelDiameterMeter * 3.14 / 60.0;
    }

}