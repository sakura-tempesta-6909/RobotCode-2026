package frc.robot.components.shooter;

public class ShooterTools {

    /** 割合からRPMへ変換　*/
    public static double RatioToRPM(double supplierRatio) {
        return supplierRatio * ShooterConst.maxRPM;
    }
}
