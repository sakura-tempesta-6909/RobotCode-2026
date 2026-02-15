package frc.robot.components.extender;

public class ExtenderTools {
    public static double getRotationsForDistance(double rotation){
        return  ExtenderConst.GearRatio * rotation;

    }
    public static double getDistanceToTarget(double targetAngle){
        return targetAngle -  ExtenderParameter.InitialAngle;
    }

}
